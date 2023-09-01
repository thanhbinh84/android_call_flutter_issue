import 'dart:ui';

import 'package:flutter/services.dart';
import 'package:memory_issue/callback_dispatcher.dart';

class InitiateCalls {

  static const MethodChannel _channel = MethodChannel('main_channel');

  static void test(void Function(String s) callback) async   {

    final List<dynamic> args = <dynamic>[
      PluginUtilities.getCallbackHandle(callbackDispatcher)?.toRawHandle(),
      PluginUtilities.getCallbackHandle(callback)?.toRawHandle()
    ];
    // await _channel.invokeMethod('run', args);
    await _channel.invokeMethod('test', args);
  }
}