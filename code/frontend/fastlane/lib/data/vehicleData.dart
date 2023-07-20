import 'dart:convert';
import 'dart:io';

import 'package:fastlane/constants/addresses.dart';
import 'package:fastlane/model/vehicle.dart';
import 'package:http/http.dart' as http;

import 'accountData.dart';

/// The `VehicleData` class is responsible for fetching and storing a list of vehicles from an API.
class VehicleData {
  static VehicleData? _instance;
  late Future<List<Vehicle>> _vehicles;

  VehicleData._privateConstructor() {
    _vehicles = _fetchVehicles();
  }

  /// The function fetches a list of vehicles from a server using an HTTP GET request and returns a
  /// Future that resolves to a List of Vehicle objects.
  /// 
  /// Returns:
  ///   The method `_fetchVehicles` returns a `Future` that resolves to a `List` of `Vehicle` objects.
  Future<List<Vehicle>> _fetchVehicles() async {
    final response =
        await http.get(Uri.parse('$BACKEND_ADDRESS/vehicle'), headers: {
      HttpHeaders.authorizationHeader:
          'Bearer ${AccountData.instance.getAuthentication().accessToken}'
    });

    if (response.statusCode == 200) {
      List<Vehicle> vehicleList = [];
      final jsonData = json.decode(response.body);
      for (var vehicle in jsonData) {
        vehicleList.add(Vehicle.fromJson(vehicle));
      }
      return vehicleList;
    } else {
      throw Exception('Failed to load vehicles ERROR ${response.statusCode}');
    }
  }

  Future<void> refresh() async {
    _vehicles = _fetchVehicles();
  }

  static VehicleData get instance {
    _instance ??= VehicleData._privateConstructor();
    return _instance!;
  }

  Future<List<Vehicle>> getVehicles() {
    return _vehicles;
  }
}
