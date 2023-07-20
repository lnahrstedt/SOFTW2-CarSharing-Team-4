import 'package:flutter/material.dart';

/// The FastlaneNavigationController class provides a static method to push a widget onto the navigation
/// stack with zero transition duration.
class FastlaneNavigationController {
  static void push(Widget widget, context) {
    Navigator.push(
      context,
      PageRouteBuilder(
        pageBuilder: (context, animation1, animation2) => widget,
        transitionDuration: Duration.zero,
        reverseTransitionDuration: Duration.zero,
      ),
    );
  }
}
