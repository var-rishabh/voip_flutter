import 'package:flutter/material.dart';

import 'helper/method_channels.dart';
import 'screens/call_logs.dart';
import 'screens/contacts.dart';
import 'screens/dial_pad.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  int _selectedIndex = 1;

  List<Widget> tabs = [
    const CallLogs(),
    const DialPad(),
    const Contacts(),
  ];

  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
    });
  }

  @override
  void initState() {
    super.initState();
    initVoxSDK();
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
          backgroundColor: Colors.cyan.shade900,
        ),
        backgroundColor: Colors.cyan.shade50,
        bottomNavigationBar: BottomNavigationBar(
          elevation: 2,
          onTap: _onItemTapped,
          currentIndex: _selectedIndex,
          selectedItemColor: Colors.white,
          unselectedItemColor: Colors.grey,
          backgroundColor: Colors.cyan.shade900,
          items: const [
            BottomNavigationBarItem(
              icon: Icon(Icons.call),
              label: 'Call Logs',
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.dialpad),
              label: 'Dialer',
            ),
            BottomNavigationBarItem(
              icon: Icon(Icons.contacts),
              label: 'Contacts',
            ),
          ],
        ),
        body: Padding(
          padding: const EdgeInsets.symmetric(
            horizontal: 12,
            vertical: 8,
          ),
          child: tabs[_selectedIndex],
        ),
      ),
    );
  }
}
