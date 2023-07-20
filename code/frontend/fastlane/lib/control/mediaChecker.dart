import 'package:flutter/material.dart';

/// The MediaController class provides static methods to determine the screen size category based on the
/// width of the MediaQuery context.
class MediaController {
  static bool isBigScreen(context) {
    return MediaQuery.of(context).size.width > 780;
  }

  static bool isMediumScreen(context) {
    return MediaQuery.of(context).size.width < 780 && !isSmallScreen(context);
  }

  static bool isSmallScreen(context) {
    return MediaQuery.of(context).size.width < 501;
  }
}