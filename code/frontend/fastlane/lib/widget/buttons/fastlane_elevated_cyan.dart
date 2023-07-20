import 'package:flutter/material.dart';

/// The `FastlaneElevatedButtonCyan` class is a stateful widget that represents a cyan colored elevated
/// button with a label and an `onPressed` callback function.
class FastlaneElevatedButtonCyan extends StatefulWidget {
  const FastlaneElevatedButtonCyan(
      {super.key, required this.onPressed, required this.label});

  final String label;
  final Function onPressed;

  @override
  State<StatefulWidget> createState() => _FastlaneElevatedButtonCyanState();
}

/// The `_FastlaneElevatedButtonCyanState` class is a stateful widget that builds an elevated button
/// with a cyan background color and black text.
class _FastlaneElevatedButtonCyanState
    extends State<FastlaneElevatedButtonCyan> {
  @override
  Widget build(BuildContext context) {
    return ElevatedButton(
        style: ElevatedButton.styleFrom(
            padding: const EdgeInsets.fromLTRB(20, 10, 20, 10),
            backgroundColor: const Color(0xFF1FFFF2),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(8.0),
              side: const BorderSide(color: Colors.black),
            )),
        child: Text(
          widget.label,
          style: const TextStyle(color: Colors.black),
        ),
        onPressed: () {
          widget.onPressed();
        });
  }
}
