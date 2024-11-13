import 'package:flutter/material.dart';

class AuthProvider extends ChangeNotifier {
  bool _isLogin = false;

  bool get isLogin => _isLogin;

  void setLogin(bool value) {
    _isLogin = value;
    notifyListeners();
  }
}
