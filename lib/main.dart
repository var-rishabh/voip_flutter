import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

const platform = MethodChannel('com.vox/call');

class _MyAppState extends State<MyApp> {
  void _initCS() async {
    try {
      final String result = await platform.invokeMethod('initCS');
      print(result);
    } on PlatformException catch (e) {
      print('Failed to get platform version: ${e.message}');
    }
  }

  void _callNative() async {
    try {
      final String result = await platform.invokeMethod('callNative');
      print(result);
    } on PlatformException catch (e) {
      print('Failed to get platform version: ${e.message}');
    }
  }

  @override
  void initState() {
    super.initState();
    _initCS();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Vox',
              style: TextStyle(
                color: Colors.white,
                fontWeight: FontWeight.bold,
              )),
          backgroundColor: Colors.blue.shade900,
        ),
        backgroundColor: Colors.blue.shade50,
        body: Center(
          child: Text('No Call Logs',
              style: TextStyle(
                fontSize: 20,
                color: Colors.blue.shade900,
                fontWeight: FontWeight.bold,
              )),
        ),
        floatingActionButtonLocation: FloatingActionButtonLocation.centerFloat,
        floatingActionButton: FloatingActionButton(
          onPressed: () {
            _callNative();
          },
          backgroundColor: Colors.blue.shade900,
          child: const Icon(
            Icons.call,
            color: Colors.white,
            size: 30,
          ),
        ),
      ),
    );
  }
}
