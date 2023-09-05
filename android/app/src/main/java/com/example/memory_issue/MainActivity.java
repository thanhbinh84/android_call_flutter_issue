package com.example.memory_issue;

import android.content.Intent;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.Objects;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.common.EventChannel;

public class MainActivity extends FlutterActivity {

    private static final String CHANNEL = "main_channel";
    private static int RQ_CODE = 1;
    private MethodChannel.Result _result;
    public static final String STREAM = "eventChannel";
    static public EventChannel.EventSink attachEvent;
    final String TAG_NAME = "From_Native";

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);

        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(
                        (call, result) -> {
                            _result = result; //Store
                            if (call.method.equals("test")) {
                                Intent intent = new Intent(this, SecondActivity.class);
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
                    }

                    @Override
                    public void onCancel(Object args) {
                        Log.w(TAG_NAME, "Cancelling listener");
                        attachEvent = null;
                        System.out.println("StreamHandler - onCanceled: ");
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // attachEvent.endOfStream();
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
