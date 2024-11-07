import 'package:flutter/material.dart';

import '../helper/method_channels.dart';

class CallScreen extends StatefulWidget {
  final String contactName;
  final String contactNumber;

  const CallScreen({
    super.key,
    required this.contactName,
    required this.contactNumber,
  });

  @override
  State<CallScreen> createState() => _CallScreenState();
}

class _CallScreenState extends State<CallScreen> {
  bool _onSpeaker = false;
  bool _onMute = false;
  bool _onHold = false;

  String _callStatus = 'Calling...';

  void _onSpeakerPressed() {
    setState(() {
      _onSpeaker = !_onSpeaker;
    });
    onSpeaker(_onSpeaker);
  }

  void _onMutePressed() {
    setState(() {
      _onMute = !_onMute;
    });
    muteCall(_onMute);
  }

  void _onHoldPressed() {
    setState(() {
      _onHold = !_onHold;
      _callStatus = _onHold ? 'On Hold' : 'Calling...';
    });
    holdCall(_onHold);
  }

  @override
  void initState() {
    super.initState();
    initiateCall(widget.contactNumber);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.cyan.shade50,
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Column(
            children: [
              Text(
                widget.contactName,
                style:
                    const TextStyle(fontSize: 30, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 8),
              Text(
                widget.contactNumber,
                style: const TextStyle(fontSize: 24, color: Colors.black87),
              ),
              const SizedBox(height: 20),
              Text(
                _callStatus,
                style: TextStyle(fontSize: 20, color: Colors.cyan.shade900),
              ),
            ],
          ),
          const SizedBox(height: 150),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              _buildIconButton(
                _onSpeaker ? Icons.volume_up : Icons.volume_off,
                'Speaker',
                _onSpeakerPressed,
              ),
              _buildIconButton(
                _onMute ? Icons.mic_off : Icons.mic,
                'Mute',
                _onMutePressed,
              ),
              _buildIconButton(
                _onHold ? Icons.pause : Icons.play_arrow,
                'Hold',
                _onHoldPressed,
              ),
            ],
          ),
          const SizedBox(height: 80),
          _buildEndCallButton(context),
        ],
      ),
    );
  }

  Widget _buildIconButton(IconData icon, String label, Function() onPressed) {
    return Column(
      children: [
        IconButton(
          icon: Icon(icon, size: 50, color: Colors.cyan.shade900),
          onPressed: onPressed,
        ),
        Text(
          label,
          style: const TextStyle(
            fontSize: 16,
            color: Colors.black,
            fontWeight: FontWeight.bold,
          ),
        ),
      ],
    );
  }

  Widget _buildEndCallButton(BuildContext context) {
    return ElevatedButton(
      onPressed: () {
        endCall(widget.contactNumber);
        Navigator.pop(context);
      },
      style: ElevatedButton.styleFrom(
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(50),
        ),
        minimumSize: const Size(180, 0),
        padding: const EdgeInsets.all(20),
        backgroundColor: Colors.red,
      ),
      child: const Icon(
        Icons.call_end,
        size: 30,
        color: Colors.white,
      ),
    );
  }
}
