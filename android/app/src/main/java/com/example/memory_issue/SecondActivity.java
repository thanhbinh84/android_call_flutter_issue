package com.example.memory_issue;

import static com.example.memory_issue.MainActivity.CALLBACK_DISPATCHER_HANDLE_KEY;
import static com.example.memory_issue.MainActivity.CALLBACK_HANDLE_KEY;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

import io.flutter.Log;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.view.FlutterCallbackInformation;
import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterNativeView;
import io.flutter.view.FlutterRunArguments;

public class SecondActivity extends FlutterActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViewById(R.id.textView).setOnClickListener(view -> {
            setResult(1);
            finish();
        });
        findViewById(R.id.textView1).setOnClickListener(view -> {
//            for (int i = 0; i < 200; i++) {
//                String[] arrays = new String[10000000];
//                System.out.println(i);
//            }
            callFlutterFunction();
        });
    }
    
    final String TAG_NAME = "From_Native";
    private int count = 1;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void callFlutterFunction() {
        MainActivity.attachEvent.success(100.8);
    }
}