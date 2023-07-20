import 'package:fastlane/constants/colors.dart';
import 'package:fastlane/model/vehicle.dart';
import 'package:fastlane/widget/vehiclePage/vehiclePage.dart';
import 'package:flutter/material.dart';

/// The `VehicleButton` class is a Flutter widget that represents a button with an
/// arrow icon, used for navigating to a specific vehicle page in a Fastlane
/// dashboard.
class VehicleButton extends StatelessWidget {
  Vehicle vehicle;

  VehicleButton({
    super.key,
    required this.vehicle,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(10),
          color: FLOJC_BLUE,
          boxShadow: [
            BoxShadow(
              color: Colors.grey.shade300,
              spreadRadius: 1,
              blurRadius: 2,
            )
          ],
        ),
        height: 40,
        width: 40,
        alignment: Alignment.center,
        child: IconButton(
          iconSize: 24,
          icon: const Icon(Icons.arrow_forward),
          onPressed: () {
            Navigator.push(
                context,
                MaterialPageRoute(
                    builder: (context) => FastlaneDashboardVehiclePage(
                          vehicle: vehicle,
                        )));
          },
          style: const ButtonStyle(
            backgroundColor: MaterialStatePropertyAll(FLOJC_BLUE),
          ),
        ));
  }
}
