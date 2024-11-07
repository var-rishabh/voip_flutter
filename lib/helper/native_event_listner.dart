import 'package:flutter/services.dart';

class NativeEventListener {
  static const MethodChannel _channel = MethodChannel('com.vox/call');

  static void startListening(Function(dynamic) callback) {
    _channel.setMethodCallHandler((call) async {
      Map<dynamic, dynamic> args = {};
      args[call.method] = call.arguments;
      callback(args);
    });
  }
}
