import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../helper/native_event_listner.dart';
import '../helper/permissions.dart';
import '../helper/platform_channel.dart';
import '../provider/auth.dart';
import '../screens/contact/add_contact.dart';
import '../screens/contact/contacts.dart';
import 'call_logs.dart';
import 'dial_pad.dart';

class Home extends StatefulWidget {
  const Home({super.key});

  @override
  State<Home> createState() => _HomeState();
}

class _HomeState extends State<Home> {
  int _selectedIndex = 0;

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

  // void handleWebEvents(String event) {
  //   if (event == 'INIT_SUCCESS') {
  //     print("VOX_SDK RIsbah is calml initialie  ");
  //   }
  // }

  @override
  void initState() {
    super.initState();
    final AuthProvider authProvider =
        Provider.of<AuthProvider>(context, listen: false);

    initVoxSDK(context);

    // js.context["onInitJSEvent"] = handleWebEvents;

    if (!kIsWeb) {
      Permissions.requestAllPermissions();

      NativeEventListener.startListening(
        (event) {
          switch (event.keys.first) {
            case 'currentLoginStatus':
              authProvider.setLogin(event['currentLoginStatus']);
              break;
          }
        },
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    final AuthProvider authProvider = Provider.of<AuthProvider>(context);

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
        child: authProvider.isLogin
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
      ),
    );
  }
}
