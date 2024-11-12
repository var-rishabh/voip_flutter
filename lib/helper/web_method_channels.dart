@JS()
library my_lib;

import 'dart:js_util';

import 'package:js/js.dart';

@JS()
external initializeCalmSDK();

@JS()
external loginCalmSDK();

initVoxSDK() async {
  try {
    var promise = initializeCalmSDK();
    var result = await promiseToFuture(promise);
    if (result == 'success') {
      loginSDK();
    }
  } catch (e) {
    print("Error initializing Calm SDK: $e");
  }
}

loginSDK() async {
  try {
    var promise = loginCalmSDK();
    var result = await promiseToFuture(promise);
    print(result);
  } catch (e) {
    print("Error Log in from Calm SDK: $e");
  }
}
