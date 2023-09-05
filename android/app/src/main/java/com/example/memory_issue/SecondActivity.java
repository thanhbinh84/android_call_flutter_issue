package com.example.memory_issue;

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
            callFlutterFunction();
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void callFlutterFunction() {
        MainActivity.attachEvent.success(100.8);
    }
}