import 'package:flutter/material.dart';

import 'helper/method_channels.dart';
import 'helper/native_event_listner.dart';
import 'screens/call_logs.dart';
import 'screens/contact/add_contact.dart';
import 'screens/contact/contacts.dart';
import 'screens/dial_pad.dart';

void main() {
  runApp(
    const MaterialApp(
      debugShowCheckedModeBanner: false,
      home: MyApp(),
    ),
  );
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  int _selectedIndex = 2;

  bool _isLogin = false;

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
    NativeEventListener.startListening(
      (event) {
        switch (event.keys.first) {
          case 'currentLoginStatus':
            setState(() {
              _isLogin = event['currentLoginStatus'];
            });
            break;
        }
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text(
            'Vox X Runo',
            style: TextStyle(
              color: Colors.white,
              fontWeight: FontWeight.bold,
            ),
          ),
          backgroundColor: Colors.cyan.shade900,
          actions: [
            _selectedIndex == 2
                ? IconButton(
                    onPressed: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => const AddContact(),
                        ),
                      );
                    },
                    icon: const Icon(
                      Icons.add,
                      color: Colors.white,
                      size: 30,
                    ),
                  )
                : const SizedBox(),
          ],
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
          padding: const EdgeInsets.all(8),
          child: _isLogin
              ? tabs[_selectedIndex]
              : Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      CircularProgressIndicator(
                        color: Colors.cyan.shade500,
                      ),
                      const SizedBox(height: 30),
                      Text('Restart the App to login again.',
                          style: TextStyle(
                            color: Colors.cyan.shade900,
                            fontSize: 16,
                            fontWeight: FontWeight.bold,
                          )),
                    ],
                  ),
                ),
        ));
  }
}
