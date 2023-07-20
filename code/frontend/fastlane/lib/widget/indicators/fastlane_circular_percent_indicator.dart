import 'package:flutter/cupertino.dart';
import 'package:percent_indicator/circular_percent_indicator.dart';

/// The `FastlaneCircularPercentIndicator` class is a stateful widget in Dart that
/// represents a circular progress indicator with a given percentage.
class FastlaneCircularPercentIndicator extends StatefulWidget {
  const FastlaneCircularPercentIndicator({super.key, required this.percent});

  final double percent;

  @override
  State<StatefulWidget> createState() =>
      _FastlaneCircularPercentIndicatorState();
}

/// The `_FastlaneCircularPercentIndicatorState` class is a stateful widget that
/// builds a circular progress indicator with a given radius, line width, and
/// percentage.
class _FastlaneCircularPercentIndicatorState
    extends State<FastlaneCircularPercentIndicator> {
  @override
  Widget build(BuildContext context) {
    return CircularPercentIndicator(
      radius: 60.0,
      lineWidth: 10.0,
      percent: widget.percent,
      center: Text("${widget.percent * 100}%",
          style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
      progressColor: const Color(0xFF1FFFF2),
    );
  }
}
