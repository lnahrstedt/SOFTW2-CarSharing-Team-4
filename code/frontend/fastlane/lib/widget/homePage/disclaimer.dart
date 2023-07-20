import 'package:flutter/material.dart';

/// The `Disclaimer` class is a widget that displays a disclaimer text in the center
/// of the screen.
class Disclaimer extends StatelessWidget {
  const Disclaimer({super.key});

  @override
  Widget build(BuildContext context) {
    return const Center(
      child: SizedBox(
        width: 500,
        child: Text(
            softWrap: true,
            textAlign: TextAlign.center,
            "Das Projekt Fastlane ist ein Hochschulprojekt, "
            "bei dem die verwendeten Bilder ausschließlich dem Design dienen "
            "und nicht unser Eigentum sind. "
            "Die Bilder wurden aus den Konfiguratoren "
            "der jeweiligen Automarken entnommen "
            "und werden nicht für kommerzielle Zwecke genutzt."),
      ),
    );
  }
}
