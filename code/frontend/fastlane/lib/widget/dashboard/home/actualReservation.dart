import 'package:fastlane/constants/border.dart';
import 'package:fastlane/constants/margin.dart';
import 'package:fastlane/constants/numbers.dart';
import 'package:fastlane/constants/padding.dart';
import 'package:fastlane/constants/text.dart';
import 'package:fastlane/control/NavigationController.dart';
import 'package:flutter/material.dart';

import '../../../constants/colors.dart';
import '../../../model/vehicle.dart';
import '../../helper/mainCard.dart';

/// The `FastlaneDashboardHomeActualReservation` class is a stateful widget that
/// represents an actual reservation with a vehicle, date, and time.
class FastlaneDashboardHomeActualReservation extends StatefulWidget {
  const FastlaneDashboardHomeActualReservation(
      {Key? key, required this.vehicle, required this.date, required this.time})
      : super(key: key);

  final Vehicle vehicle;
  final String date;
  final String time;

  @override
  State<FastlaneDashboardHomeActualReservation> createState() =>
      _FastlaneDashboardHomeActualReservationState();
}

/// The `_FastlaneDashboardHomeActualReservationState` class is a stateful widget
/// that displays information about an upcoming reservation on a dashboard card.
class _FastlaneDashboardHomeActualReservationState
    extends State<FastlaneDashboardHomeActualReservation> {
  void navigateToWidget(Widget widget) {
    FastlaneNavigationController.push(widget, context);
  }

  @override
  Widget build(BuildContext context) {
    return FastlaneDashboardCard(
        title: "Anstehende Buchungen",
        padding: BIG_PADDING,
        height: DEFAULT_CARD_HEIGHT,
        width: DEFAULT_CARD_WIDTH,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Padding(
            padding: EdgeInsets.only(left: 20),
            child: Image.network(
              widget.vehicle.vehicleModel.linkToPicture,
              width: 150,
              height: 150,
            ),
          ),
          SizedBox(
            width: 200,
            child: Wrap(
              direction: Axis.horizontal,
              children: [
                FastlaneDashboardActualReservationData(
                    data:
                        "${widget.vehicle.vehicleModel.vehicleBrand.brandName} ${widget.vehicle.vehicleModel.modelName}",
                    icon: Icons.directions_car_filled_rounded),
                FastlaneDashboardActualReservationData(
                    data: widget.time, icon: Icons.schedule_rounded),
                FastlaneDashboardActualReservationData(
                    data: widget.date, icon: Icons.date_range_rounded),
              ],
            ),
          ),
        ]);
  }
}

/// The `FastlaneDashboardActualReservationData` class is a widget that displays
/// actual reservation data with an icon.
class FastlaneDashboardActualReservationData extends StatelessWidget {
  const FastlaneDashboardActualReservationData(
      {super.key, required this.data, required this.icon});

  final String data;
  final IconData icon;

  @override
  Widget build(BuildContext context) {
    return FittedBox(
      child: Container(
        margin: SMALL_MARGIN,
        padding: SMALL_PADDING,
        decoration: BoxDecoration(
            color: GRAY_BLUEISH, borderRadius: RESERVATIONDATA_BORDERRADIUS),
        child: Row(
          children: [
            Icon(
              icon,
              color: INBETWEEN_GRAY,
            ),
            const SizedBox(
              width: 2,
            ),
            Text(
              data,
              style: RESERVATION_TEXT_STYLE,
            )
          ],
        ),
      ),
    );
  }
}
