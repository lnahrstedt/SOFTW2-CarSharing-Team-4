import 'package:fastlane/widget/dashboard/home/carEntry.dart';
import 'package:fastlane/widget/helper/mainScrollable.dart';
import 'package:flutter/material.dart';

import '../../../model/vehicle.dart';
import '../../helper/mainContainer.dart';
import '../../helper/widget.dart';

/// The `FastlaneDashboardAllCars` class is a stateful widget that displays a dashboard of all cars, with a list of vehicles
/// as its input.
class FastlaneDashboardAllCars extends StatefulWidget {
  const FastlaneDashboardAllCars({
    Key? key,
    required this.vehicles,
  }) : super(key: key);

  final Future<List<Vehicle>> vehicles;

  @override
  State<FastlaneDashboardAllCars> createState() =>
      FastlaneDashboardAllCarsState();
}

/// The `FastlaneDashboardAllCarsState` class is responsible for building the widget that displays a list of vehicles in a
/// scrollable container on a dashboard.
class FastlaneDashboardAllCarsState extends State<FastlaneDashboardAllCars> {
  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: Future.wait([widget.vehicles]),
      builder: (BuildContext context, AsyncSnapshot<dynamic> snapshot) {
        if (snapshot.connectionState == ConnectionState.done) {
          if (!snapshot.hasError) {
            List<Vehicle> vehicleList = snapshot.data[0];
            return FastlaneScrollable(
              direction: Axis.vertical,
              child: FastlaneDashboardMainContainer(
                  title: "Unsere Autos",
                  wrap: true,
                  widgets: vehicleList
                      .map((vehicle) => FastlaneWidget(
                              child: FastlaneDashboardCarEntry(
                            vehicle: vehicle,
                            grid: true,
                          )))
                      .toList()),
            );
          } else {
            return Text(snapshot.error.toString());
          }
        } else {
          return const CircularProgressIndicator();
        }
      },
    );
  }
}
