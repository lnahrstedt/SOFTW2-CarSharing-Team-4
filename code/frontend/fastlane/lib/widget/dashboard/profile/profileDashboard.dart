import 'package:fastlane/widget/dashboard/profile/profileCard.dart';
import 'package:flutter/material.dart';

import '../../helper/mainContainer.dart';
import '../../helper/widget.dart';

/// The class FastlaneDashboardProfile is a StatefulWidget in Dart.
class FastlaneDashboardProfile extends StatefulWidget {
  const FastlaneDashboardProfile({super.key});

  @override
  State<FastlaneDashboardProfile> createState() =>
      FastlaneDashboardProfileState();
}

/// The `FastlaneDashboardProfileState` class manages the state and UI for
/// displaying a profile in a dashboard, including an edit menu that can be toggled
/// on and off.
class FastlaneDashboardProfileState extends State<FastlaneDashboardProfile> {
  bool _showEditMenu = false;

  void _switchEditMenu() {
    setState(() {
      _showEditMenu = !_showEditMenu;
    });
  }

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
        child: FastlaneDashboardMainContainer(
      title: "Profil",
      wrap: false,
      widgets: [
        FastlaneWidget(
            child: FastlaneDashboardProfileHeaderCard(
          switchEditMenu: _switchEditMenu,
        )),
        _showEditMenu
            ? const FastlaneWidget(child: FastlaneDashboardProfileEdit())
            : const FastlaneWidget(child: FastlaneDashboardProfileAttributes()),
      ],
    ));
  }
}
