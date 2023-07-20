import 'package:auto_size_text/auto_size_text.dart';
import 'package:fastlane/constants/colors.dart';
import 'package:fastlane/data/accountData.dart';
import 'package:fastlane/model/vehicle.dart';
import 'package:fastlane/widget/helper/networkImage.dart';
import 'package:fastlane/widget/mapPage/vehicleButton.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

import '../../data/driverData.dart';
import '../helper/platformHelper.dart';

/// The `VehicleCard` class is a widget that displays information about a vehicle,
/// including its category, brand and model, image, price, and a button for further
/// actions.
class VehicleCard extends StatelessWidget {
  final Vehicle vehicle;
  final priceFormat = NumberFormat.currency(locale: "de", symbol: "€");

  VehicleCard({
    super.key,
    required this.vehicle,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 310,
      width: 275,
      decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(16),
          color: CupertinoColors.white,
          boxShadow: const [
            BoxShadow(
              color: Colors.grey,
              spreadRadius: 5,
              blurRadius: 7,
              offset: Offset(0, 3),
            )
          ]),
      padding: const EdgeInsets.all(20),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(vehicle.configuration.category,
              style: const TextStyle(
                fontSize: 12,
                color: LESSER_BLUE,
              )),
          AutoSizeText(
            '${vehicle.vehicleModel.vehicleBrand.brandName} - ${vehicle.vehicleModel.modelName}',
            style: TextStyle(
                fontSize: PlatformHelper.isMobile() ? 14 : 18,
                fontWeight: FontWeight.bold,
                color: GRAY),
            maxFontSize: PlatformHelper.isMobile() ? 14 : 18,
            maxLines: 2,
          ),
          FastlaneNetworkImage(url: vehicle.vehicleModel.linkToPicture),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  if (!AccountData.instance.isEmployee())
                    Text(
                      '${priceFormat.format(DriverData.instance?.driver.fareType.price)} / Stunde',
                      style: TextStyle(
                          fontSize: PlatformHelper.isMobile() ? 18 : 26,
                          fontWeight: FontWeight.w700,
                          color: GRAY),
                    ),
                  if (!AccountData.instance.isEmployee())
                    Text(
                      DriverData.instance!.driver.fareType.name,
                      style: TextStyle(
                          fontSize: PlatformHelper.isMobile() ? 12 : 14,
                          fontWeight: FontWeight.w500,
                          color: INBETWEEN_GRAY),
                    )
                ],
              ),
              VehicleButton(vehicle: vehicle),
            ],
          )
        ],
      ),
    );
  }
}
