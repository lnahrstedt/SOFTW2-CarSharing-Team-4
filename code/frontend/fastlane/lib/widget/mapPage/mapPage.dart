import 'package:fastlane/widget/helper/mainCard.dart';
import 'package:fastlane/widget/helper/mainScrollable.dart';
import 'package:fastlane/widget/mapPage/futureFastlaneMap.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../../model/vehicle.dart';

/// The `FastlaneDashboardMap` class is a widget that displays a scrollable map of
/// vehicles on a dashboard.
class FastlaneDashboardMap extends StatelessWidget {
  final Future<List<Vehicle>> vehicles;

  const FastlaneDashboardMap({super.key, required this.vehicles});

  @override
  Widget build(BuildContext context) {
    double screenHeight = MediaQuery.of(context).size.height;
    double screenWidth = MediaQuery.of(context).size.width;

    return FastlaneScrollable(
        direction: Axis.vertical,
        child: FastlaneDashboardCard(
            width: screenWidth * 0.9,
            height: screenHeight * 0.778,
            padding: const EdgeInsets.all(10),
            crossAxisAlignment: CrossAxisAlignment.center,
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              ClipRRect(
                borderRadius: BorderRadius.circular(10),
                child: SizedBox(
                  width: screenWidth * 0.89,
                  height: screenHeight * 0.75,
                  child: FutureFastlaneMap(vehicles: vehicles),
                ),
              ),
            ]));
  }
}
