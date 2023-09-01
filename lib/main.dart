import 'dart:async';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:memory_issue/event_channel.dart';
import 'package:path_provider/path_provider.dart';

import 'initiate_calls_to_dart_in_bg.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {

  static const stream = EventChannel('com.chamelalaboratory.demo.flutter_event_channel/eventChannel');

  late StreamSubscription _streamSubscription;
  double _currentValue = 0.0;

  void _startListener() {
    _streamSubscription = stream.receiveBroadcastStream().listen(_listenStream);
  }

  void _cancelListener() {
    _streamSubscription.cancel();
    setState(() {
      _currentValue = 0;
    });
  }

  void _listenStream(value) {
    debugPrint("Received From Native:  $value\n");
    setState(() {
      _currentValue = value;
    });
  }

  static const platform = MethodChannel('com.example.app');
  void _incrementCounter() async {
    // final result = await platform.invokeMethod('test');
    // print('result: $result');
    print("count = $count");
    InitiateCalls.test(callback);
  }

  @override
  void initState() {
    _startListener();
    super.initState();
  }

  static int? count;

  static void callback(String s) async {
    print("I am from main.dart");
    count = 1;
    print(s);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              'You have pushed the button this many times: $_currentValue',
            ),

          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: const Icon(Icons.add),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }

}
