import 'package:flutter/material.dart';

class Dialer extends StatefulWidget {
  const Dialer({super.key});

  @override
  State<Dialer> createState() => _DialerState();
}

class _DialerState extends State<Dialer> {
  @override
  Widget build(BuildContext context) {
    return const Center(
      child: Text("Dialer"),
    );
  }
}
