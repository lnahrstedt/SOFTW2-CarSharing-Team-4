import 'package:flutter/material.dart';

import '../buttons/fastlane_elevated_cyan_icon.dart';
import '../buttons/fastlane_icon_button.dart';
import '../forms/login/login_form.dart';

/// The TitleBar class is a StatefulWidget in Dart that represents a title bar
/// component.
class TitleBar extends StatefulWidget {
  const TitleBar({super.key});

  @override
  State<StatefulWidget> createState() => _TitleBarState();
}

/// The `_TitleBarState` class builds an AppBar widget with various buttons and a
/// login button that opens a login form dialog when pressed.
///
/// Args:
///   context (BuildContext): The `context` parameter is an object that provides
/// access to various information and services related to the current build context.
/// It is typically used to access the theme, media queries, and to navigate to
/// other screens or widgets.
///
/// Returns:
///   The `_TitleBarState` class is returning an `AppBar` widget.
class _TitleBarState extends State<TitleBar> {
  @override
  Widget build(BuildContext context) {
    return AppBar(
        leading:
            FastlaneIconButton(asset: 'assets/fastlane.svg', onPressed: () {}),
        title: Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            TextButton(onPressed: () {}, child: const Text('Standorte')),
            TextButton(onPressed: () {}, child: const Text('Automarken')),
            TextButton(onPressed: () {}, child: const Text('Tarife')),
            TextButton(onPressed: () {}, child: const Text('Tarifvergleich')),
            FastlaneElevatedButtonCyanIcon(
              onPressed: () {
                showDialog(
                    context: context,
                    builder: (BuildContext context) {
                      return const LoginForm();
                    });
              },
              label: 'Login',
              asset: 'assets/LogIn.svg',
            ),
          ],
        ));
  }
}
