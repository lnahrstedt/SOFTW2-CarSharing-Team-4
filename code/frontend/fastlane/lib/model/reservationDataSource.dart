import 'package:fastlane/model/reservation.dart';
import 'package:flutter/material.dart';
import 'package:syncfusion_flutter_calendar/calendar.dart';

import '../data/driverData.dart';

/// The `ReservationDataSource` class is a subclass of `CalendarDataSource` that provides data for
/// displaying reservations in a calendar, including start and end times, subjects, colors, and whether
/// they are all-day events.
class ReservationDataSource extends CalendarDataSource {
  ReservationDataSource(List<Reservation> source) {
    appointments = source;
  }

  @override
  DateTime getStartTime(int index) {
    return appointments![index].startDateTime;
  }

  @override
  DateTime getEndTime(int index) {
    return appointments![index].endDateTime;
  }

  @override
  String getSubject(int index) {
    if (appointments![index].driver == (DriverData.instance?.getDriver().id)) {
      return 'Von dir gebucht';
    }
    return 'Gebucht';
  }

  @override
  Color getColor(int index) {
    if (appointments![index].driver == (DriverData.instance?.getDriver().id)) {
      return Colors.green;
    }
    return Colors.redAccent;
  }

  @override
  bool isAllDay(int index) {
    return false;
  }
}
