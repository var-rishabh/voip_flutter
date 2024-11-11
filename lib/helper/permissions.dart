import 'package:permission_handler/permission_handler.dart';

class Permissions {
  static Future<void> requestAllPermissions() async {
    await _requestStoragePermission();
    await _requestExternalStoragePermission();
    await _requestMicrophonePermission();
    await _requestPhonePermission();
  }

  static Future<void> _requestStoragePermission() async {
    final status = await Permission.photos.status;
    if (!status.isGranted) {
      PermissionStatus result = await Permission.photos.request();
      PermissionStatus result2 = await Permission.storage.request();
      if (result.isGranted) {
        print('VOX_SDK Storage permission granted');
      } else {
        print('VOX_SDK Storage permission denied');
      }
    } else {
      print('VOX_SDK Storage permission already granted');
    }
  }

  static Future<void> _requestExternalStoragePermission() async {
    final status = await Permission.manageExternalStorage.status;
    if (!status.isGranted) {
      PermissionStatus result =
          await Permission.manageExternalStorage.request();
      if (result.isGranted) {
        print('VOX_SDK External Storage permission granted');
      } else {
        print('VOX_SDK External Storage permission denied');
      }
    } else {
      print('VOX_SDK External Storage permission already granted');
    }
  }

  static Future<void> _requestMicrophonePermission() async {
    final status = await Permission.microphone.status;
    if (!status.isGranted) {
      PermissionStatus result = await Permission.microphone.request();
      if (result.isGranted) {
        print('VOX_SDK Microphone permission granted');
      } else {
        print('VOX_SDK Microphone permission denied');
      }
    } else {
      print('VOX_SDK Microphone permission already granted');
    }
  }

  static Future<void> _requestPhonePermission() async {
    final status = await Permission.phone.status;
    if (!status.isGranted) {
      PermissionStatus result = await Permission.phone.request();
      if (result.isGranted) {
        print('VOX_SDK Phone permission granted');
      } else {
        print('VOX_SDK Phone permission denied');
      }
    } else {
      print('VOX_SDK Phone permission already granted');
    }
  }
}
