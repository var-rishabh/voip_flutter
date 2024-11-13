import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:voxflut/screens/call_screen.dart';

class DialPad extends StatefulWidget {
  final String? number;

  const DialPad({
    super.key,
    this.number,
  });

  @override
  State<DialPad> createState() => _DialPadState();
}

class _DialPadState extends State<DialPad> {
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
    if (_enteredNumber.length == 13) {
      Navigator.push(
        context,
        MaterialPageRoute(
          builder: (context) => CallScreen(
            contactName: "Unknown",
            contactNumber: _enteredNumber,
          ),
        ),
      );
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text("Please enter a valid number"),
        ),
      );
    }
  }

  Widget _buildNumberButton(String number) {
    return ElevatedButton(
      onPressed: () => _onKeyPress(number),
      style: ElevatedButton.styleFrom(
        backgroundColor: Colors.cyan.shade100,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(100),
        ),
        minimumSize: const Size(100, 100),
      ),
      child: Text(
        number,
        style: const TextStyle(
          fontSize: 35,
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

    return SizedBox(
      width: double.infinity,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 20),
            child: ConstrainedBox(
              constraints: const BoxConstraints(maxWidth: 370),
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
          ),
          const SizedBox(height: 50),
          ConstrainedBox(
            constraints: const BoxConstraints(maxWidth: kIsWeb ? 400 : 400),
            child: Wrap(
              spacing: 30,
              runSpacing: 30,
              alignment: WrapAlignment.center,
              children: [
                ...List.generate(9, (index) {
                  return _buildNumberButton('${index + 1}');
                }),
                ElevatedButton(
                  onPressed: () => {
                    setState(() {
                      _enteredNumber = "+91";
                    })
                  },
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.red.shade400,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(100),
                    ),
                    minimumSize: const Size(100, 100),
                  ),
                  child: const Icon(
                    Icons.delete,
                    size: 40,
                    color: Colors.white,
                  ),
                ),
                _buildNumberButton('0'),
                ElevatedButton(
                  onPressed: () => _onCall(),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.green.shade400,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(100),
                    ),
                    minimumSize: const Size(100, 100),
                  ),
                  child: const Icon(
                    Icons.call,
                    size: 40,
                    color: Colors.white,
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
