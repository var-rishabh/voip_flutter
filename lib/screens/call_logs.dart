import 'package:flutter/material.dart';

class CallLogs extends StatefulWidget {
  const CallLogs({super.key});

  @override
  State<CallLogs> createState() => _CallLogsState();
}

class _CallLogsState extends State<CallLogs> {
  @override
  Widget build(BuildContext context) {
    return const Center(
      child: Text("Call Logs"),
    );
  }
}
