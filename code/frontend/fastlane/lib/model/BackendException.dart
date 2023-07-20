// To parse this JSON data, do
//
//     final backendException = backendExceptionFromJson(jsonString);

import 'dart:convert';

BackendException backendExceptionFromJson(String str) =>
    BackendException.fromJson(json.decode(str));

String backendExceptionToJson(BackendException data) =>
    json.encode(data.toJson());

/// The `BackendException` class represents an exception that can occur in a backend system, with
/// properties for error code, error name, description, and timestamp.
class BackendException {
  int errorCode;
  String errorName;
  String description;
  String timestamp;

  BackendException({
    required this.errorCode,
    required this.errorName,
    required this.description,
    required this.timestamp,
  });

  factory BackendException.fromJson(Map<String, dynamic> json) =>
      BackendException(
        errorCode: json["errorCode"],
        errorName: json["errorName"],
        description: json["description"],
        timestamp: json["timestamp"],
      );

  Map<String, dynamic> toJson() => {
        "errorCode": errorCode,
        "errorName": errorName,
        "description": description,
        "timestamp": timestamp,
      };
}
