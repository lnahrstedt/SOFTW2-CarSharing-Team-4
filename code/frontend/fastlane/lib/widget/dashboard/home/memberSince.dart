import 'package:fastlane/constants/numbers.dart';
import 'package:fastlane/constants/padding.dart';
import 'package:fastlane/widget/helper/mainCard.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';

/// The `FastlaneDashboardMembership` class is a stateful widget in Dart.
/// The `FastlaneDashboardMembership` class is a stateful widget in Dart.
class FastlaneDashboardMembership extends StatefulWidget {
  const FastlaneDashboardMembership({Key? key}) : super(key: key);

  @override
  State<FastlaneDashboardMembership> createState() =>
      _FastlaneDashboardMembershipState();
}

/// The `_FastlaneDashboardMembershipState` class is a stateful widget that builds a
/// card with a title and an SVG image.
class _FastlaneDashboardMembershipState
    extends State<FastlaneDashboardMembership> {
  @override
  Widget build(BuildContext context) {
    return FastlaneDashboardCard(
        padding: BIG_PADDING,
        title: "",
        height: DEFAULT_CARD_HEIGHT,
        width: DEFAULT_CARD_WIDTH,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          const SizedBox(height: 20),
          Center(
              child: SvgPicture.asset(
            "assets/fastlane.svg",
            width: DEFAULT_CARD_WIDTH * 0.6,
            height: DEFAULT_CARD_HEIGHT * 0.6,
          )),
        ]);
  }
}
