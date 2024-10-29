import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:voxflut/screens/call_logs.dart';
import 'package:voxflut/screens/contacts.dart';
import 'package:voxflut/screens/dialer.dart';

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
  int _selectedIndex = 0;

  List<Widget> tabs = [
    const CallLogs(),
    const Dialer(),
    const Contacts(),
  ];

  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
    });
  }

  void _initCS() async {
    try {
      final String result = await platform.invokeMethod('initCS');
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
        bottomNavigationBar: BottomNavigationBar(
          elevation: 2,
          onTap: _onItemTapped,
          currentIndex: _selectedIndex,
          selectedItemColor: Colors.white,
          unselectedItemColor: Colors.grey,
          backgroundColor: Colors.blue.shade900,
          items: [
            BottomNavigationBarItem(
              icon: Icon(
                Icons.call,
                color: _selectedIndex == 0 ? Colors.white : Colors.grey,
              ),
              label: 'Call Logs',
            ),
            BottomNavigationBarItem(
              icon: Icon(
                Icons.dialpad,
                color: _selectedIndex == 1 ? Colors.white : Colors.grey,
              ),
              label: 'Dialer',
            ),
            BottomNavigationBarItem(
              icon: Icon(
                Icons.contacts,
                color: _selectedIndex == 2 ? Colors.white : Colors.grey,
              ),
              label: 'Contacts',
            ),
          ],
        ),
        body: tabs[_selectedIndex],
      ),
    );
  }
}
