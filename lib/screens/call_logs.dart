import 'package:flutter/material.dart';

import '../../helper/native_event_listner.dart';
import '../helper/method_channels.dart';
import '../models/call_log.dart';
import './call_screen.dart';

class CallLogs extends StatefulWidget {
  const CallLogs({super.key});

  @override
  State<CallLogs> createState() => _CallLogsState();
}

class _CallLogsState extends State<CallLogs> {
  List<CallLog> _callLogList = [];

  Future<void> _fetchCallLogs() async {
    List<CallLog> result = await getCallLogs();
    setState(() {
      _callLogList = result;
    });
  }

  @override
  void initState() {
    super.initState();
    _fetchCallLogs();

    NativeEventListener.startListening(
      (event) {
        switch (event.keys.first) {
          case 'callLogsUpdated':
            _fetchCallLogs();
            break;
        }
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: _callLogList.isEmpty
          ? const Center(child: Text('No call logs available.'))
          : ListView.builder(
              itemCount: _callLogList.length,
              itemBuilder: (context, index) {
                CallLog callLog = _callLogList[index];
                return ListTile(
                  leading: Icon(
                    callLog.callBoundType == 1
                        ? Icons.call_made
                        : (callLog.callBoundType == 2
                            ? Icons.call_received
                            : Icons.phone),
                    color: callLog.callBoundType == 1
                        ? Colors.green
                        : callLog.callBoundType == 2
                            ? Colors.blue
                            : Colors.red,
                  ),
                  title: Text(
                    callLog.calleName == callLog.calleNumber
                        ? "Unknown"
                        : callLog.calleName,
                    style: const TextStyle(fontWeight: FontWeight.bold),
                  ),
                  subtitle: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(callLog.calleNumber),
                      Text(callLog.time),
                    ],
                  ),
                  trailing: Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Text(
                        callLog.duration,
                        style: const TextStyle(
                          color: Colors.blueGrey,
                          fontSize: 16,
                        ),
                      ),
                      const SizedBox(width: 20),
                      GestureDetector(
                        onTap: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (context) => CallScreen(
                                contactName:
                                    callLog.calleName == callLog.calleNumber
                                        ? "Unknown"
                                        : callLog.calleName,
                                contactNumber: callLog.calleNumber,
                              ),
                            ),
                          );
                        },
                        child: Icon(
                          Icons.call,
                          color: Colors.cyan.shade800,
                        ),
                      ),
                    ],
                  ),
                );
              },
            ),
    );
  }
}
