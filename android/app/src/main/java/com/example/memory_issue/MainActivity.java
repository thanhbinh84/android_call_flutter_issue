package com.example.memory_issue;

import android.content.Intent;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MainActivity extends FlutterActivity {

    private static final String CHANNEL = "main_channel";
    private static int RQ_CODE = 1;
    private MethodChannel.Result _result;
    public static final String CALLBACK_HANDLE_KEY = "callback_handle_key";
    public static final String CALLBACK_DISPATCHER_HANDLE_KEY = "callback_dispatcher_handle_key";

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
