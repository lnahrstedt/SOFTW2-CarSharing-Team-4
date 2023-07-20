import 'package:fastlane/control/mediaChecker.dart';
import 'package:fastlane/widget/buttons/fastlane_elevated_cyan_icon.dart';
import 'package:flutter/material.dart';

import '../forms/register/register_form_personal.dart';

/// The MainTitle class is a StatefulWidget in Dart that represents a main title
/// widget.
class MainTitle extends StatefulWidget {
  const MainTitle({super.key});

  @override
  State<StatefulWidget> createState() => _MainTitleState();
}

/// The `_MainTitleState` class is responsible for rendering the main title section
/// of a car-sharing app's homepage, including text, icons, and a signup button.
class _MainTitleState extends State<MainTitle> {
  Text infoText = const Text(
    "Willkommen bei fastlane, Deinem innovativen Carsharing-Partner, der Mobilität neu definiert.",
    softWrap: true,
    maxLines: 5,
  );

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Wrap(
        crossAxisAlignment: WrapCrossAlignment.center,
        direction: Axis.horizontal,
        alignment: WrapAlignment.center,
        spacing: 50,
        runSpacing: 20,
        runAlignment: WrapAlignment.center,
        children: [
          if (!MediaController.isSmallScreen(context)) createInfoIcons(),
          if (!MediaController.isSmallScreen(context))
            const SizedBox(width: 20),
          createTitleInfo(),
          const SizedBox(
              width: 400,
              height: 400,
              child:
                  Image(image: AssetImage('assets/homepage/example_Car.png'))),
        ],
      ),
    );
  }

  Column createInfoIcons() {
    return Column(
      children: [
        IconButton(
            onPressed: () {}, icon: const Icon(Icons.info_outline_rounded)),
        IconButton(onPressed: () {}, icon: const Icon(Icons.message_outlined)),
        IconButton(
            onPressed: () {}, icon: const Icon(Icons.location_on_outlined)),
      ],
    );
  }

  Column createTitleInfo() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text(
          "Fastlane",
          style: TextStyle(fontSize: 60.0, fontWeight: FontWeight.bold),
        ),
        SizedBox(width: 250.0, child: infoText),
        const SizedBox(height: 20),
        FastlaneElevatedButtonCyanIcon(
          onPressed: () {
            showDialog(
                context: context,
                builder: (BuildContext context) {
                  return const RegisterFormPersonal();
                });
          },
          label: "Signup",
          icon: Icons.login_rounded,
        )
      ],
    );
  }
}
