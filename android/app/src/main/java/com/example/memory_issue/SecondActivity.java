package com.example.memory_issue;

import static com.example.memory_issue.MainActivity.CALLBACK_DISPATCHER_HANDLE_KEY;
import static com.example.memory_issue.MainActivity.CALLBACK_HANDLE_KEY;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.view.FlutterCallbackInformation;
import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterNativeView;
import io.flutter.view.FlutterRunArguments;

public class SecondActivity extends AppCompatActivity {
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

    private void callFlutterFunction() {
        long callbackDispatcherHandle = getIntent().getLongExtra(CALLBACK_DISPATCHER_HANDLE_KEY, 0);

        FlutterCallbackInformation flutterCallbackInformation =
                FlutterCallbackInformation.lookupCallbackInformation(callbackDispatcherHandle);

        FlutterRunArguments flutterRunArguments = new FlutterRunArguments();
        flutterRunArguments.bundlePath = FlutterMain.findAppBundlePath();
        flutterRunArguments.entrypoint = flutterCallbackInformation.callbackName;
        flutterRunArguments.libraryPath = flutterCallbackInformation.callbackLibraryPath;

        FlutterNativeView backgroundFlutterView = new FlutterNativeView(this, true);
        backgroundFlutterView.runFromBundle(flutterRunArguments);

        MethodChannel mBackgroundChannel = new MethodChannel(backgroundFlutterView, "background_channel");

        long callbackHandle = getIntent().getLongExtra(CALLBACK_HANDLE_KEY, 0);

        final ArrayList<Object> l = new ArrayList<>();
        l.add(callbackHandle);
        l.add("Hi, I am transferred from java to dart world");

        mBackgroundChannel.invokeMethod("", l);
    }
}