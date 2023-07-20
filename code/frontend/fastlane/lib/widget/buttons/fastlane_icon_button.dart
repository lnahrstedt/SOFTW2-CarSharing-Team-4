import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';

/// The `FastlaneIconButton` class is a stateful widget that represents an icon button with a specified
/// asset, width, height, and an `onPressed` callback function.
class FastlaneIconButton extends StatefulWidget {
  const FastlaneIconButton(
      {super.key,
      required this.asset,
      required this.onPressed,
      this.width = 30,
      this.height = 30});

  final String asset;
  final Function onPressed;
  final double width;
  final double height;

  @override
  State<StatefulWidget> createState() => _FastlaneIconButtonState();
}

/// The `_FastlaneIconButtonState` class is a stateful widget that builds an `IconButton` with an
/// `SvgPicture` asset as its icon.
class _FastlaneIconButtonState extends State<FastlaneIconButton> {
  @override
  Widget build(BuildContext context) {
    return IconButton(
        onPressed: () {
          widget.onPressed();
        },
        icon: SvgPicture.asset(
          widget.asset,
          width: widget.width,
          height: widget.height,
        ));
  }
}
