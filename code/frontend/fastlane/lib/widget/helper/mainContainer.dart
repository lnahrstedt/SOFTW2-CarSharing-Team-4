import 'package:fastlane/constants/margin.dart';
import 'package:fastlane/control/mediaChecker.dart';
import 'package:flutter/material.dart';

/// The `FastlaneDashboardMainContainer` class is a stateful widget that represents
/// a container for displaying a title and a list of widgets, with an option to wrap
/// the widgets.
class FastlaneDashboardMainContainer extends StatefulWidget {
  const FastlaneDashboardMainContainer(
      {Key? key,
      required this.title,
      required this.widgets,
      required this.wrap})
      : super(key: key);
  final String title;
  final List<Widget> widgets;
  final bool wrap;

  @override
  State<FastlaneDashboardMainContainer> createState() =>
      _FastlaneDashboardMainContainerState();
}

/// The `_FastlaneDashboardMainContainerState` class is responsible for building the
/// main container widget for the Fastlane Dashboard, which includes a column of
/// widgets that are either centered or aligned to the start based on the screen
/// size.
class _FastlaneDashboardMainContainerState
    extends State<FastlaneDashboardMainContainer> {
  @override
  Widget build(BuildContext context) {
    return Container(
      margin: SYMMETRIC_HORIZONTAL_MIDDLE_MARGIN,
      child: Column(
        crossAxisAlignment: MediaController.isSmallScreen(context) &&
                widget.title != "Buchungen"
            ? CrossAxisAlignment.center
            : CrossAxisAlignment.start,
        children: widget.wrap ? [buildDefaultView(context)] : widget.widgets,
      ),
    );
  }

  Widget buildDefaultView(BuildContext context) {
    return Wrap(
        alignment: MediaController.isSmallScreen(context)
            ? WrapAlignment.center
            : WrapAlignment.start,
        direction: Axis.horizontal,
        children: widget.widgets);
  }
}
