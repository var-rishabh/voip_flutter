import 'package:flutter/material.dart';

import '../../helper/method_channels.dart';
import '../../helper/native_event_listner.dart';
import '../call_screen.dart';

class Contacts extends StatefulWidget {
  const Contacts({super.key});

  @override
  State<Contacts> createState() => _ContactsState();
}

class _ContactsState extends State<Contacts> {
  Map<dynamic, dynamic> _contacts = {};
  bool? _updateContactsSuccess;

  void _getContacts() async {
    final contacts = await getContacts();
    setState(() {
      _contacts = contacts;
    });
  }

  void showUpdateContactMessage() {
    if (_updateContactsSuccess != null) {
      if (_updateContactsSuccess!) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            duration: Duration(seconds: 1),
            content: Text("Contacts updated successfully."),
          ),
        );
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            duration: Duration(seconds: 1),
            content: Text("Failed to update contacts."),
          ),
        );
      }
      _updateContactsSuccess = null;
    }
  }

  @override
  void initState() {
    super.initState();
    _getContacts();
    NativeEventListener.startListening(
      (event) {
        switch (event.keys.first) {
          case 'contactAdded':
            if (event['contactAdded']) {
              setState(() {
                _updateContactsSuccess = true;
              });
            } else {
              setState(() {
                _updateContactsSuccess = false;
              });
            }
            showUpdateContactMessage();
            _getContacts();
            break;

          case "contactDeleted":
            if (event["contactDeleted"]) {
              setState(() {
                _updateContactsSuccess = true;
              });
            } else {
              setState(() {
                _updateContactsSuccess = false;
              });
            }
            showUpdateContactMessage();
        }
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: _contacts.isEmpty
          ? const Text('No contacts found.')
          : ListView.builder(
              itemCount: _contacts.length,
              itemBuilder: (context, index) {
                final number = _contacts.keys.elementAt(index);
                final name = _contacts.values.elementAt(index);
                return ListTile(
                  visualDensity: VisualDensity.compact,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(10),
                  ),
                  leading: CircleAvatar(
                    backgroundColor: Colors.cyan.shade900,
                    child: const Icon(
                      Icons.person,
                      color: Colors.white,
                    ),
                  ),
                  title: Text(name),
                  subtitle: Text("+91 ${number.substring(3)}"),
                  trailing: Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      IconButton(
                        padding: const EdgeInsets.all(0),
                        constraints: null,
                        icon: const Icon(Icons.call),
                        onPressed: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) => CallScreen(
                                contactName: name,
                                contactNumber: "+91$number",
                              ),
                            ),
                          );
                        },
                      ),
                      IconButton(
                        padding: const EdgeInsets.all(0),
                        constraints: null,
                        icon: const Icon(Icons.delete),
                        onPressed: () {
                          deleteContact(number);
                          _getContacts();
                        },
                      ),
                    ],
                  ),
                );
              },
            ),
    );
  }
}
