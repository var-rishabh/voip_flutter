import 'package:flutter/services.dart';

const platform = MethodChannel('com.vox/call');

Future<void> initVoxSDK() async {
  try {
    final String result = await platform.invokeMethod('initVoxSDK');
    print(result);
  } on PlatformException catch (e) {
    print('Failed to initialize SDK: ${e.message}');
  }
}
