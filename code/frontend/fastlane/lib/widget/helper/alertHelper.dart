import 'package:flutter/material.dart';

/// The `AlertHelper` class provides a static method `show` to display an alert
/// dialog with an icon, text, and a specified duration.
class AlertHelper {
  static void show(
      BuildContext context, Icon icon, String text, Duration duration) {
    showDialog(
      context: context,
      builder: (context) {
        return FutureBuilder(
          future: Future.delayed(duration).then((value) => true),
          builder: (context, snapshot) {
            if (snapshot.hasData) {
              Navigator.of(context).pop();
            }

            return AlertDialog(
                title: Row(children: [
              icon,
              const SizedBox(width: 10),
              Flexible(child: Text(text)),
            ]));
          },
        );
      },
    );
  }
}
