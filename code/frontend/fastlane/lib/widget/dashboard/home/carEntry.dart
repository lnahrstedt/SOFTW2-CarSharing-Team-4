import 'package:fastlane/constants/border.dart';
import 'package:fastlane/constants/colors.dart';
import 'package:fastlane/constants/margin.dart';
import 'package:fastlane/constants/numbers.dart';
import 'package:fastlane/constants/shadow.dart';
import 'package:fastlane/widget/helper/mainCard.dart';
import 'package:fastlane/widget/helper/networkImage.dart';
import 'package:fastlane/widget/vehiclePage/vehiclePage.dart';
import 'package:flutter/material.dart';

import '../../../model/vehicle.dart';

/// The `FastlaneDashboardCarEntry` class is a stateful widget that represents an
/// entry for a car in a dashboard, with properties for the vehicle, grid layout,
/// and a widget to navigate to.
class FastlaneDashboardCarEntry extends StatefulWidget {
  const FastlaneDashboardCarEntry(
      {Key? key,
      required this.vehicle,
      this.navigateToWidget,
      required this.grid})
      : super(key: key);

  final Vehicle vehicle;
  final bool grid;
  final Widget? navigateToWidget;

  @override
  State<FastlaneDashboardCarEntry> createState() =>
      _FastlaneDashboardCarEntryState();
}

/// The `_FastlaneDashboardCarEntryState` class is responsible for building and
/// managing the state of a car entry widget in a Fastlane dashboard, including
/// handling hover events and displaying the car's brand and image.
class _FastlaneDashboardCarEntryState extends State<FastlaneDashboardCarEntry> {
  bool isHovering = false;
  final GlobalKey<FastlaneDashboardVehiclePageState> fastlaneVehiclePage =
      GlobalKey();

  void setHover(hover) {
    setState(() {
      isHovering = hover;
    });
  }

  /// The function `buildBrand()` returns an `Image` widget based on the brand name
  /// of a vehicle.
  ///
  /// Returns:
  ///   The function `buildBrand()` returns an `Image` widget.
  Image buildBrand() {
    String brand = widget.vehicle.vehicleModel.vehicleBrand.brandName;
    if (brand == "Mercedes-Benz") {
      return const Image(
        image: AssetImage("assets/brands/mercedes.png"),
      );
    }
    if (brand == "BMW") {
      return const Image(
        image: AssetImage("assets/brands/bmw.png"),
      );
    }
    if (brand == "Volkswagen") {
      return const Image(
        image: AssetImage("assets/brands/volkswagen.png"),
      );
    }
    if (brand == "Lamborghini") {
      return const Image(
        image: AssetImage("assets/brands/lamborghini.png"),
      );
    }
    if (brand == "Tesla") {
      return const Image(
        image: AssetImage("assets/brands/tesla.png"),
      );
    }
    if (brand == "Seat") {
      return const Image(
        image: AssetImage("assets/brands/seat.png"),
      );
    }
    if (brand == "Ford") {
      return const Image(
        image: AssetImage("assets/brands/ford.png"),
      );
    }
    if (brand == "Opel") {
      return const Image(
        image: AssetImage("assets/brands/opel.png"),
      );
    }
    if (brand == "Skoda") {
      return const Image(
        image: AssetImage("assets/brands/skoda.png"),
      );
    }
    if (brand == "Audi") {
      return const Image(
        image: AssetImage("assets/brands/audi.png"),
      );
    }
    return const Image(
      image: AssetImage("assets/brands/toyota.png"),
    );
  }

  /// The function builds a default entry container with a vehicle image, brand
  /// name, and model name.
  ///
  /// Returns:
  ///   a Container widget.
  Container buildDefaultEntry() {
    return Container(
      margin: MIDDLE_MARGIN,
      child: Material(
        color: Colors.white,
        child: InkWell(
          hoverColor: Colors.white,
          onHover: (hover) => setHover(hover),
          onTap: () => Navigator.push(
              context,
              PageRouteBuilder(
                pageBuilder: (context, animation1, animation2) =>
                    FastlaneDashboardVehiclePage(vehicle: widget.vehicle),
                transitionDuration: Duration.zero,
                reverseTransitionDuration: Duration.zero,
              )),
          child: SizedBox(
            width: 175,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Container(
                  height: 180,
                  decoration: BoxDecoration(
                      color: GRAY_BLUEISH,
                      borderRadius: CARD_BORDERRADIUS,
                      boxShadow: [
                        isHovering ? CARD_SHADOW_NO_HOVER : NO_SHADOW_CAR_ENTRY
                      ]),
                  child: Center(
                    child: Container(
                      margin: const EdgeInsets.all(4),
                      child: FastlaneNetworkImage(
                          url: widget.vehicle.vehicleModel.linkToPicture),
                    ),
                  ),
                ),
                const SizedBox(
                  height: 5,
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          widget.vehicle.vehicleModel.vehicleBrand.brandName,
                          style: const TextStyle(
                            fontSize: 14,
                            fontWeight: FontWeight.w600,
                          ),
                        ),
                        Text(
                          widget.vehicle.vehicleModel.modelName,
                          style: const TextStyle(
                            fontSize: 12,
                            fontWeight: FontWeight.w400,
                            color: INBETWEEN_GRAY,
                          ),
                        ),
                      ],
                    ),
                    SizedBox(height: 24, width: 24, child: buildBrand()),
                  ],
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  /// The function `buildGridEntry()` returns a `FastlaneDashboardCard` widget with
  /// a specific layout and content based on the `widget.vehicle` data.
  ///
  /// Returns:
  ///   a FastlaneDashboardCard widget.
  InkWell buildGridEntry() {
    return InkWell(
      onTap: () => Navigator.push(
          context,
          PageRouteBuilder(
            pageBuilder: (context, animation1, animation2) =>
                FastlaneDashboardVehiclePage(vehicle: widget.vehicle),
            transitionDuration: Duration.zero,
            reverseTransitionDuration: Duration.zero,
          )),
      child: FastlaneDashboardCard(
          padding: const EdgeInsets.symmetric(horizontal: 12.5, vertical: 15),
          height: DEFAULT_CARD_HEIGHT,
          width: DEFAULT_CARD_WIDTH * 0.925,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Center(
              child: SizedBox(
                width: 270,
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Container(
                      height: 220,
                      decoration: const BoxDecoration(
                        color: GRAY_BLUEISH,
                        borderRadius: CARD_BORDERRADIUS,
                      ),
                      child: Center(
                        child: Container(
                          margin: const EdgeInsets.all(4),
                          child: FastlaneNetworkImage(
                              url: widget.vehicle.vehicleModel.linkToPicture),
                        ),
                      ),
                    ),
                    const SizedBox(
                      height: 10,
                    ),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              widget.vehicle.vehicleModel.vehicleBrand.brandName,
                              style: const TextStyle(
                                  fontSize: 16, fontWeight: FontWeight.w600),
                            ),
                            Text(
                              widget.vehicle.vehicleModel.modelName,
                              style: const TextStyle(
                                  fontSize: 13,
                                  fontWeight: FontWeight.w500,
                                  color: INBETWEEN_GRAY),
                            ),
                          ],
                        ),
                        SizedBox(height: 24, width: 24, child: buildBrand()),
                      ],
                    )
                  ],
                ),
              ),
            ),
          ]),
    );
  }

  @override
  Widget build(BuildContext context) {
    return widget.grid ? buildGridEntry() : buildDefaultEntry();
  }
}
