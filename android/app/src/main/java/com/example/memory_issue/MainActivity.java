package com.example.memory_issue;

import android.content.Intent;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import android.os.Bundle;
import android.os.Handler;

import java.util.Objects;

import io.flutter.Log;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.common.EventChannel;

public class MainActivity extends FlutterActivity {

    private static final String CHANNEL = "main_channel";
    private static int RQ_CODE = 1;
    private MethodChannel.Result _result;
    public static final String CALLBACK_HANDLE_KEY = "callback_handle_key";
    public static final String CALLBACK_DISPATCHER_HANDLE_KEY = "callback_dispatcher_handle_key";

    public static final String STREAM = "com.chamelalaboratory.demo.flutter_event_channel/eventChannel";
    private EventChannel.EventSink attachEvent;
    final String TAG_NAME = "From_Native";
    private int count = 1;
    private Handler handler;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int TOTAL_COUNT = 100;
            if (count > TOTAL_COUNT) {
                attachEvent.endOfStream();
            } else {
                double percentage = ((double) count / TOTAL_COUNT);
                Log.w(TAG_NAME, "\nParsing From Native:  " + percentage);
                attachEvent.success(percentage);
            }
            count++;
            handler.postDelayed(this, 200);
        }
    };

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(
                        (call, result) -> {
                            _result = result; //Store
                            if (call.method.equals("test")) {
                                Intent intent = new Intent(this, SecondActivity.class);
                                ArrayList args = call.arguments();
                                long mCallbackDispatcherHandle = (long) args.get(0);
                                long callbackHandle = (long) args.get(1);
                                intent.putExtra(CALLBACK_HANDLE_KEY, callbackHandle);
                                intent.putExtra(CALLBACK_DISPATCHER_HANDLE_KEY, mCallbackDispatcherHandle);
                                startActivityForResult(intent, RQ_CODE);
                            }
                        }
                );

        new EventChannel(Objects.requireNonNull(getFlutterEngine()).getDartExecutor(), STREAM).setStreamHandler(
                new EventChannel.StreamHandler() {
                    @Override
                    public void onListen(Object args, final EventChannel.EventSink events) {
                        Log.w(TAG_NAME, "Adding listener");
                        attachEvent = events;
                        count = 1;
                        handler = new Handler();
                        runnable.run();
                    }

                    @Override
                    public void onCancel(Object args) {
                        Log.w(TAG_NAME, "Cancelling listener");
                        handler.removeCallbacks(runnable);
                        handler = null;
                        count = 1;
                        attachEvent = null;
                        System.out.println("StreamHandler - onCanceled: ");
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        handler = null;
        attachEvent = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.printf("onActivityResult %d %d; %s%n", requestCode, resultCode, this);
        if (requestCode == RQ_CODE && _result != null) {
            _result.success(resultCode);
            System.out.println("_result " + _result.toString());
        } else
            System.out.println("_result null");
    }


}
