import 'package:flutter/services.dart';

const platform = MethodChannel('com.vox/call');

Future<void> initVoxSDK() async {
  try {
    await platform.invokeMethod('initVoxSDK');
  } on PlatformException catch (e) {
    print('Failed to initialize SDK: ${e.message}');
  }
}

Future<void> addContact(String name, String number) async {
  try {
    await platform.invokeMethod('addContact', {
      'name': name,
      'number': number,
    });
  } on PlatformException catch (e) {
    print('Failed to add contact: ${e.message}');
  }
}

Future<Map<dynamic, dynamic>> getContacts() async {
  try {
    Map<dynamic, dynamic> result = await platform.invokeMethod('getContacts');
    var sortedEntries = result.entries.toList()
      ..sort((a, b) => a.value.toLowerCase().compareTo(b.value.toLowerCase()));
    result = Map.fromEntries(sortedEntries);

    return result;
  } on PlatformException catch (e) {
    print('Failed to get contacts: ${e.message}');
    return {};
  }
}

Future<void> deleteContact(String number) async {
  try {
    await platform.invokeMethod('deleteContact', {
      'number': number,
    });
  } on PlatformException catch (e) {
    print('Failed to delete contact: ${e.message}');
  }
}
