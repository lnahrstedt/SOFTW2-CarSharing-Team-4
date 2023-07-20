import 'package:fastlane/data/driverData.dart';
import 'package:flutter/material.dart';
import 'package:syncfusion_flutter_calendar/calendar.dart';

import '../../model/reservation.dart';
import '../../model/reservationDataSource.dart';

/// The `ReservationCalendar` class is a widget that displays a calendar with
/// reservations, highlighting blocked times in different colors based on the
/// driver.
class ReservationCalendar extends StatelessWidget {
  final Future<List<Reservation>> reservations;
  final Size size;

  const ReservationCalendar(
      {super.key, required this.reservations, required this.size});

  /// The function `_createCalendar` creates a calendar widget with special regions
  /// representing blocked times based on a list of reservations.
  ///
  /// Args:
  ///   reservations (List<Reservation>): The `reservations` parameter is a list of
  /// `Reservation` objects.
  ///
  /// Returns:
  ///   a widget, specifically an instance of the SfCalendar widget.
  Widget _createCalendar(List<Reservation> reservations) {
    List<TimeRegion> blockedTimes = [];
    final activeReservation =
        reservations.where((res) => !res.isCanceled()).toList();
    for (Reservation reservation in activeReservation) {
      Color blockedColor;
      if (reservation.driver == DriverData.instance?.driver.id) {
        blockedColor = Colors.green;
      } else {
        blockedColor = Colors.redAccent;
      }
      blockedTimes.add(TimeRegion(
          color: blockedColor,
          startTime: reservation.startDateTime,
          endTime: reservation.endDateTime));
    }

    return SfCalendar(
      showNavigationArrow: true,
      showTodayButton: true,
      showCurrentTimeIndicator: true,
      showDatePickerButton: true,
      specialRegions: blockedTimes,
      view: CalendarView.month,
      allowedViews: const [CalendarView.week, CalendarView.month],
      dataSource: ReservationDataSource(activeReservation),
    );
  }

  @override
  build(BuildContext context) {
    return FutureBuilder(
        future: reservations,
        builder: (context, AsyncSnapshot<dynamic> snapshot) {
          if (snapshot.connectionState == ConnectionState.done) {
            if (!snapshot.hasError) {
              List<Reservation> vehicleReservations = snapshot.data!;
              return SizedBox(
                  height: size.height,
                  width: size.width,
                  child: _createCalendar(vehicleReservations));
            } else {
              return Container(
                  height: size.height,
                  width: size.width,
                  alignment: Alignment.center,
                  child:
                      const Text("Der Kalender konnte nicht geladen werden."));
            }
          } else {
            return Container(
                height: size.height,
                width: size.width,
                alignment: Alignment.center,
                child: const CircularProgressIndicator());
          }
        });
  }
}
