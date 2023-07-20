import 'package:fastlane/constants/numbers.dart';
import 'package:fastlane/constants/padding.dart';
import 'package:fastlane/widget/dashboard/home/carEntry.dart';
import 'package:fastlane/widget/helper/mainCard.dart';
import 'package:fastlane/widget/vehiclePage/vehiclePage.dart';
import 'package:flutter/material.dart';

import '../../../model/vehicle.dart';

/// The `FastlaneDashboardHomeOtherCars` class is a stateful widget that displays a
/// list of vehicles on a dashboard.
class FastlaneDashboardHomeOtherCars extends StatefulWidget {
  const FastlaneDashboardHomeOtherCars({super.key, required this.vehicles});

  final List<Vehicle> vehicles;

  @override
  State<FastlaneDashboardHomeOtherCars> createState() =>
      _FastlaneDashboardHomeOtherCarsState();
}

/// The `_FastlaneDashboardHomeOtherCarsState` class is a stateful widget that
/// displays a list of vehicles in a horizontal scrollable view.
class _FastlaneDashboardHomeOtherCarsState
    extends State<FastlaneDashboardHomeOtherCars> {
  @override
  Widget build(BuildContext context) {
    return FastlaneDashboardCard(
      padding: BIG_PADDING,
      crossAxisAlignment: CrossAxisAlignment.start,
      title: "Unsere Fahrzeuge",
      height: DEFAULT_CARD_HEIGHT,
      width: DEFAULT_EXPANDED_CARD_WIDTH,
      children: [
        SizedBox(
          height: DEFAULT_CARD_HEIGHT * 0.8,
          child: ListView(
            scrollDirection: Axis.horizontal,
            shrinkWrap: true,
            children: widget.vehicles
                .map((vehicle) => FastlaneDashboardCarEntry(
                      vehicle: vehicle,
                      navigateToWidget:
                          FastlaneDashboardVehiclePage(vehicle: vehicle),
                      grid: false,
                    ))
                .toList(),
          ),
        )
      ],
    );
  }
}
