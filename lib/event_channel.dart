import 'dart:async';

import 'package:flutter/services.dart';

class EventChannelTutorial {
  static const MethodChannel _channel = MethodChannel('event_channel_tutorial');

  // New
  static const EventChannel _randomNumberChannel = EventChannel('random_number_channel');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  // New
  static Stream<int> get getRandomNumberStream {
    return _randomNumberChannel.receiveBroadcastStream().cast();
  }
}