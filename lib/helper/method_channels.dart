import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../models/call_log.dart';

const platform = MethodChannel('com.vox/call');

Future<void> initVoxSDK(BuildContext context) async {
  try {
    await platform.invokeMethod('initVoxSDK');
  } on PlatformException catch (e) {
    print('Failed to initialize SDK: ${e.message}');
  }
}

Future<void> initiateCall(String number) async {
  try {
    await platform.invokeMethod('initiateCall', {
      'number': number,
    });
  } on PlatformException catch (e) {
    print('Failed to initiate call: ${e.message}');
  }
}

Future<void> onSpeaker(bool onSpeaker) async {
  try {
    await platform.invokeMethod('onSpeaker', {
      'onSpeaker': onSpeaker,
    });
  } on PlatformException catch (e) {
    print('Failed to turn on speaker: ${e.message}');
  }
}

Future<void> muteCall(bool onMute) async {
  try {
    await platform.invokeMethod('muteCall', {
      'onMute': onMute,
    });
  } on PlatformException catch (e) {
    print('Failed to mute call: ${e.message}');
  }
}

Future<void> holdCall(bool onHold) async {
  try {
    await platform.invokeMethod('holdCall', {
      'onHold': onHold,
    });
  } on PlatformException catch (e) {
    print('Failed to hold call: ${e.message}');
  }
}

Future<void> endCall(String number) async {
  try {
    await platform.invokeMethod('endCall', {
      'number': number,
    });
  } on PlatformException catch (e) {
    print('Failed to end call: ${e.message}');
  }
}

Future<List<CallLog>> getCallLogs() async {
  try {
    List<CallLog> callLogs = [];

    Map<dynamic, dynamic> result = await platform.invokeMethod('getCallLogs');

    result.forEach((key, value) {
      // print("VOX_SDK: CAll Duration : " + value[4]);
      CallLog callLog = CallLog(
        callId: key,
        calleName: value[0],
        calleNumber: value[1],
        duration: CallLog.calculateDuration(value[4]),
        callBoundType: CallLog.getCallBoundType(value[2]),
        timeEpoch: value[3],
        time: CallLog.convertEpochToTime(value[3]),
      );

      callLogs.add(callLog);
    });

    callLogs.sort(
        (a, b) => int.parse(b.timeEpoch).compareTo(int.parse(a.timeEpoch)));

    return callLogs;
  } on PlatformException catch (e) {
    print('Failed to get contacts: ${e.message}');
    return [];
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
