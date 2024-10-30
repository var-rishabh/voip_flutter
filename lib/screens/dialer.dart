import 'package:flutter/material.dart';

class Dialer extends StatefulWidget {
  final String? number;

  const Dialer({
    super.key,
    this.number,
  });

  @override
  State<Dialer> createState() => _DialerState();
}

class _DialerState extends State<Dialer> {
  String _enteredNumber = "+91";

  void _onKeyPress(String value) {
    if (_enteredNumber.length >= 13) {
      return;
    }
    setState(() {
      _enteredNumber += value;
    });
  }

  void _onBackspace() {
    setState(() {
      if (_enteredNumber.length > 3) {
        _enteredNumber = _enteredNumber.substring(0, _enteredNumber.length - 1);
      }
    });
  }

  void _onCall() {
    if (_enteredNumber.isNotEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text("Calling $_enteredNumber...")),
      );
    }
  }

  Widget _buildNumberButton(String number) {
    return ElevatedButton(
      onPressed: () => _onKeyPress(number),
      style: ElevatedButton.styleFrom(
        backgroundColor: Colors.cyan.shade100,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(40),
        ),
        minimumSize: const Size(120, 100),
      ),
      child: Text(
        number,
        style: const TextStyle(
          fontSize: 40,
          fontWeight: FontWeight.bold,
          color: Colors.black,
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    if (widget.number != null) {
      _enteredNumber = widget.number ?? "+91";
    }

    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 20),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                "+91 ${_enteredNumber.substring(3)}",
                style: const TextStyle(
                  fontSize: 40,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(width: 30),
              GestureDetector(
                onTap: _onBackspace,
                child: const Icon(Icons.backspace, size: 25),
              ),
            ],
          ),
        ),
        const SizedBox(height: 50),
        Wrap(
          spacing: 15,
          runSpacing: 15,
          alignment: WrapAlignment.center,
          children: [
            ...List.generate(9, (index) {
              return _buildNumberButton('${index + 1}');
            }),
            _buildNumberButton('+'),
            _buildNumberButton('0'),
            ElevatedButton(
              onPressed: () => _onCall(),
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.green.shade400,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(40),
                ),
                minimumSize: const Size(120, 100),
              ),
              child: const Icon(
                Icons.call,
                size: 40,
                color: Colors.white,
              ),
            ),
          ],
        ),
      ],
    );
  }
}
