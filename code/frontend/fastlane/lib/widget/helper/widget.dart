import 'package:flutter/material.dart';

/// The FastlaneWidget class is a stateless widget that simply returns its child
/// widget.
class FastlaneWidget extends StatelessWidget {
  const FastlaneWidget({super.key, required this.child});

  final Widget child;

  @override
  Widget build(BuildContext context) {
    return child;
  }
}
