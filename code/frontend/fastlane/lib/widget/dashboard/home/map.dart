import 'package:fastlane/constants/numbers.dart';
import 'package:fastlane/constants/padding.dart';
import 'package:fastlane/data/vehicleData.dart';
import 'package:fastlane/widget/helper/mainCard.dart';
import 'package:fastlane/widget/mapPage/futureFastlaneMap.dart';
import 'package:flutter/material.dart';

/// The `FastlaneDashboardHomeMap` class is a stateful widget in Dart used for
/// displaying a map on the Fastlane dashboard home page.
class FastlaneDashboardHomeMap extends StatefulWidget {
  const FastlaneDashboardHomeMap({Key? key}) : super(key: key);

  @override
  State<FastlaneDashboardHomeMap> createState() =>
      _FastlaneDashboardHomeMapState();
}

/// The `_FastlaneDashboardHomeMapState` class is a stateful widget that displays a
/// card with a map showing the locations of vehicles.
class _FastlaneDashboardHomeMapState extends State<FastlaneDashboardHomeMap> {
  @override
  Widget build(BuildContext context) {
    return FastlaneDashboardCard(
      padding: BIG_PADDING,
      title: "Finde unsere Fahrzeuge",
      width: DEFAULT_EXPANDED_CARD_WIDTH,
      height: DEFAULT_CARD_HEIGHT,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Column(
          children: [
            const SizedBox(
              height: 10,
            ),
            SizedBox(
              width: DEFAULT_EXPANDED_CARD_WIDTH,
              height: DEFAULT_CARD_HEIGHT * 0.75,
              child: ClipRRect(
                borderRadius: BorderRadius.circular(10),
                child: FutureFastlaneMap(
                  vehicles: VehicleData.instance.getVehicles(),
                ),
              ),
            ),
          ],
        )
      ],
    );
  }
}
