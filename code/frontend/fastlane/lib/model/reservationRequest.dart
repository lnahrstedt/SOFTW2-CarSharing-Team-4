// To parse this JSON data, do
//
//     final reservationRequest = reservationRequestFromJson(jsonString);

import 'dart:convert';

ReservationRequest reservationRequestFromJson(String str) =>
    ReservationRequest.fromJson(json.decode(str));

String reservationRequestToJson(ReservationRequest data) =>
    json.encode(data.toJson());

/// The `ReservationRequest` class represents a reservation request for a vehicle, including details
/// such as vehicle ID, driver ID, price, currency code, reservation state, start date and time, and end
/// date and time.
class ReservationRequest {
  int vehicleId;
  int driverId;
  double price;
  String currencyCode;
  String reservationState;
  DateTime startDateTime;
  DateTime endDateTime;

  ReservationRequest({
    required this.vehicleId,
    required this.driverId,
    required this.price,
    required this.currencyCode,
    required this.reservationState,
    required this.startDateTime,
    required this.endDateTime,
  });

  factory ReservationRequest.fromJson(Map<String, dynamic> json) =>
      ReservationRequest(
        vehicleId: json["vehicleId"],
        driverId: json["driverId"],
        price: json["price"]?.toDouble(),
        currencyCode: json["currencyCode"],
        reservationState: json["reservationState"],
        startDateTime: DateTime.parse(json["startDateTime"]),
        endDateTime: DateTime.parse(json["endDateTime"]),
      );

  Map<String, dynamic> toJson() => {
        "vehicleId": vehicleId,
        "driverId": driverId,
        "price": price,
        "currencyCode": currencyCode,
        "reservationState": reservationState,
        "startDateTime": startDateTime.toIso8601String(),
        "endDateTime": endDateTime.toIso8601String(),
      };
}
