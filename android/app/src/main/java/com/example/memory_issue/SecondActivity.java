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
        createEventChannel();
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

    public static final String STREAM = "com.chamelalaboratory.demo.flutter_event_channel/eventChannel";
    private EventChannel.EventSink attachEvent;
    final String TAG_NAME = "From_Native";
    private int count = 1;

    private void createEventChannel() {
        new EventChannel(Objects.requireNonNull(getFlutterEngine()).getDartExecutor(), STREAM).setStreamHandler(
                new EventChannel.StreamHandler() {
                    @Override
                    public void onListen(Object args, final EventChannel.EventSink events) {
                        Log.w(TAG_NAME, "Adding listener");
                        attachEvent = events;
                        attachEvent.success(1.5);
                        count = 1;
                    }

                    @Override
                    public void onCancel(Object args) {
                        Log.w(TAG_NAME, "Cancelling listener");
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
        attachEvent = null;
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