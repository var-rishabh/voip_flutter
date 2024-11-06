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

Future<void> addContact(String name, String number) async {
  try {
    final String result = await platform.invokeMethod('addContact', {
      'name': name,
      'number': number,
    });
    print(result);
  } on PlatformException catch (e) {
    print('Failed to add contact: ${e.message}');
  }
}

Future<Map<dynamic, dynamic>> getContacts() async {
  try {
    final Map<dynamic, dynamic> result =
        await platform.invokeMethod('getContacts');
    print("VOX_SDK: $result");
    return result;
  } on PlatformException catch (e) {
    print('Failed to get contacts: ${e.message}');
    return {};
  }
}

Future<void> deleteContact(String number) async {
  try {
    final String result = await platform.invokeMethod('deleteContact', {
      'number': number,
    });
    print(result);
  } on PlatformException catch (e) {
    print('Failed to delete contact: ${e.message}');
  }
}
