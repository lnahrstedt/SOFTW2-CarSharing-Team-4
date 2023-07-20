// To parse this JSON data, do
//
//     final vehicle = vehicleFromJson(jsonString);

import 'dart:convert';

Vehicle vehicleFromJson(String str) => Vehicle.fromJson(json.decode(str));

String vehicleToJson(Vehicle data) => json.encode(data.toJson());

/// The `Vehicle` class represents a vehicle with various properties such as id, number plate, mileage,
/// latitude, longitude, construction year, vehicle model, and configuration.
class Vehicle {
  int id;
  String numberPlate;
  double mileage;
  double latitude;
  double longitude;
  int constructionYear;
  VehicleModel vehicleModel;
  Configuration configuration;

  Vehicle({
    required this.id,
    required this.numberPlate,
    required this.mileage,
    required this.latitude,
    required this.longitude,
    required this.constructionYear,
    required this.vehicleModel,
    required this.configuration,
  });

  factory Vehicle.fromJson(Map<String, dynamic> json) => Vehicle(
        id: json["id"],
        numberPlate: json["numberPlate"],
        mileage: json["mileage"]?.toDouble(),
        latitude: json["latitude"]?.toDouble(),
        longitude: json["longitude"]?.toDouble(),
        constructionYear: json["constructionYear"],
        vehicleModel: VehicleModel.fromJson(json["vehicleModel"]),
        configuration: Configuration.fromJson(json["configuration"]),
      );

  Map<String, dynamic> toJson() => {
        "id": id,
        "numberPlate": numberPlate,
        "mileage": mileage,
        "latitude": latitude,
        "longitude": longitude,
        "constructionYear": constructionYear,
        "vehicleModel": vehicleModel.toJson(),
        "configuration": configuration.toJson(),
      };
}

/// The Configuration class represents a set of configuration options for a vehicle.
class Configuration {
  String category;
  String type;
  String transmission;
  String fuel;
  bool ac;
  bool navigation;
  bool cruiseControl;
  bool drivingAssistent;

  Configuration({
    required this.category,
    required this.type,
    required this.transmission,
    required this.fuel,
    required this.ac,
    required this.navigation,
    required this.cruiseControl,
    required this.drivingAssistent,
  });

  factory Configuration.fromJson(Map<String, dynamic> json) => Configuration(
        category: json["category"],
        type: json["type"],
        transmission: json["transmission"],
        fuel: json["fuel"],
        ac: json["ac"],
        navigation: json["navigation"],
        cruiseControl: json["cruiseControl"],
        drivingAssistent: json["drivingAssistent"],
      );

  Map<String, dynamic> toJson() => {
        "category": category,
        "type": type,
        "transmission": transmission,
        "fuel": fuel,
        "ac": ac,
        "navigation": navigation,
        "cruiseControl": cruiseControl,
        "drivingAssistent": drivingAssistent,
      };
}

/// The `VehicleModel` class represents a vehicle model with properties such as id, modelName,
/// linkToPicture, and vehicleBrand.
class VehicleModel {
  int id;
  String modelName;
  String linkToPicture;
  VehicleBrand vehicleBrand;

  VehicleModel({
    required this.id,
    required this.modelName,
    required this.linkToPicture,
    required this.vehicleBrand,
  });

  factory VehicleModel.fromJson(Map<String, dynamic> json) => VehicleModel(
        id: json["id"],
        modelName: json["modelName"],
        linkToPicture: json["linkToPicture"],
        vehicleBrand: VehicleBrand.fromJson(json["vehicleBrand"]),
      );

  Map<String, dynamic> toJson() => {
        "id": id,
        "modelName": modelName,
        "linkToPicture": linkToPicture,
        "vehicleBrand": vehicleBrand.toJson(),
      };
}

/// The VehicleBrand class represents a vehicle brand with an id and brand name.
class VehicleBrand {
  int id;
  String brandName;

  VehicleBrand({
    required this.id,
    required this.brandName,
  });

  factory VehicleBrand.fromJson(Map<String, dynamic> json) => VehicleBrand(
        id: json["id"],
        brandName: json["brandName"],
      );

  Map<String, dynamic> toJson() => {
        "id": id,
        "brandName": brandName,
      };
}
