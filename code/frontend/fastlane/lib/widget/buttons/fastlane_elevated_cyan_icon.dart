import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';

/// The `FastlaneElevatedButtonCyanIcon` class is a stateful widget that represents a button with a cyan
/// background, an optional icon, and a label.
class FastlaneElevatedButtonCyanIcon extends StatefulWidget {
  const FastlaneElevatedButtonCyanIcon(
      {super.key,
      required this.onPressed,
      required this.label,
      this.asset,
      this.iconSize = 20,
      this.icon});

  final String label;
  final String? asset;
  final IconData? icon;
  final Function onPressed;
  final double iconSize;

  @override
  State<StatefulWidget> createState() => _FastlaneElevatedButtonCyanIconState();
}

/// The `_FastlaneElevatedButtonCyanIconState` class is a stateful widget that builds an elevated button
/// with a cyan background, an icon, and a label.
class _FastlaneElevatedButtonCyanIconState
    extends State<FastlaneElevatedButtonCyanIcon> {
  @override
  Widget build(BuildContext context) {
    return ElevatedButton.icon(
      style: ElevatedButton.styleFrom(
          padding: const EdgeInsets.fromLTRB(20, 10, 20, 10),
          backgroundColor: const Color(0xFF1FFFF2),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(8.0),
            side: const BorderSide(color: Colors.black),
          )),
      label: Text(widget.label,
          style: const TextStyle(color: Colors.black), softWrap: false),
      icon: widget.asset == null
          ? Icon(
              widget.icon,
              color: Colors.black,
            )
          : SvgPicture.asset(widget.asset!,
              width: widget.iconSize, height: widget.iconSize),
      onPressed: () {
        widget.onPressed();
      },
    );
  }
}
