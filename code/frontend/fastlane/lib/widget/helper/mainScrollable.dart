import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';

/// The `FastlaneScrollable` class is a widget that provides a scrollable view with
/// customizable scrolling direction and disables scrollbars while allowing
/// scrolling with touch and mouse devices.
class FastlaneScrollable extends StatelessWidget {
  const FastlaneScrollable(
      {super.key, required this.child, required this.direction});

  final Widget child;
  final Axis direction;

  @override
  Widget build(BuildContext context) {
    return ScrollConfiguration(
        behavior: ScrollConfiguration.of(context).copyWith(
          scrollbars: false,
          dragDevices: {
            PointerDeviceKind.touch,
            PointerDeviceKind.mouse,
          },
        ),
        child: SingleChildScrollView(
          scrollDirection: direction,
          child: child,
        ));
  }
}
