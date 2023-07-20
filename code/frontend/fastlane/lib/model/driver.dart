import 'dart:convert';

Driver driverFromJson(String str) => Driver.fromJson(json.decode(str));

String driverToJson(Driver data) => json.encode(data.toJson());

/// The `Driver` class represents a driver with an ID, license ID, user ID, and fare type.
class Driver {
  int id;
  String licenseId;
  int user;
  FareType fareType;

  Driver({
    required this.id,
    required this.licenseId,
    required this.user,
    required this.fareType,
  });

  factory Driver.fromJson(Map<String, dynamic> json) => Driver(
        id: json["id"],
        licenseId: json["licenseId"],
        user: json["user"],
        fareType: FareType.fromJson(json["fareType"]),
      );

  Map<String, dynamic> toJson() => {
        "id": id,
        "licenseId": licenseId,
        "user": user,
        "fareType": fareType.toJson(),
      };
}

FareType fareTypeFromJson(String str) => FareType.fromJson(json.decode(str));

String fareTypeToJson(FareType data) => json.encode(data.toJson());

/// The FareType class represents a type of fare with a name and price, and provides methods for
/// converting to and from JSON.
class FareType {
  String name;
  double price;

  FareType({
    required this.name,
    required this.price,
  });

  factory FareType.fromJson(Map<String, dynamic> json) => FareType(
        name: json["name"],
        price: json["price"],
      );

  Map<String, dynamic> toJson() => {
        "name": name,
        "price": price,
      };
}
