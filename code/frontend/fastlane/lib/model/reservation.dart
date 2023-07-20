// To parse this JSON data, do
//
//     final reservation = reservationFromJson(jsonString);

import 'dart:convert';

import 'package:fastlane/constants/reservationState.dart';

Reservation reservationFromJson(String str) =>
    Reservation.fromJson(json.decode(str));

String reservationToJson(Reservation data) => json.encode(data.toJson());

/// The `Reservation` class represents a reservation with properties such as id, price, start and end
/// date/time, reservation state, vehicle, and driver.
class Reservation {
  int id;
  double price;
  DateTime startDateTime;
  DateTime endDateTime;
  String reservationState;
  int vehicle;
  int driver;

  Reservation({
    required this.id,
    required this.price,
    required this.startDateTime,
    required this.endDateTime,
    required this.reservationState,
    required this.vehicle,
    required this.driver,
  });

  factory Reservation.fromJson(Map<String, dynamic> json) => Reservation(
        id: json["id"],
        price: json["price"]?.toDouble(),
        startDateTime: DateTime.parse(json["startDateTime"]),
        endDateTime: DateTime.parse(json["endDateTime"]),
        reservationState: json["reservationState"],
        vehicle: json["vehicle"],
        driver: json["driver"],
      );

  Map<String, dynamic> toJson() => {
        "id": id,
        "price": price,
        "startDateTime": startDateTime.toIso8601String(),
        "endDateTime": endDateTime.toIso8601String(),
        "reservationState": reservationState,
        "vehicle": vehicle,
        "driver": driver,
      };

  bool isCanceled() {
    return reservationState == ReservationState.CANCELED.name;
  }
}
