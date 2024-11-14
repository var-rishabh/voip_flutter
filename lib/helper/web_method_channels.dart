import 'dart:js_util';

import 'package:flutter/material.dart';
import 'package:js/js.dart';
import 'package:provider/provider.dart';

import '../provider/auth.dart';

@JS()
external initializeCalmSDK();

@JS()
external loginCalmSDK();

initVoxSDK(BuildContext context) async {
  try {
    var promise = initializeCalmSDK();
    var result = await promiseToFuture(promise);
    if (context.mounted) {
      if (result == 'success') {
        loginSDK(context);
      }
    }
  } catch (e) {
    print("Error initializing Calm SDK: $e");
  }
}

loginSDK(BuildContext context) async {
  try {
    var promise = loginCalmSDK();
    var result = await promiseToFuture(promise);
    if (context.mounted) {
      if (result == 'success') {
        print("Logged in successfully");
        Provider.of<AuthProvider>(context, listen: false).setLogin(true);
      }
    }
  } catch (e) {
    print("Error logging in to Calm SDK: $e");
  }
}
