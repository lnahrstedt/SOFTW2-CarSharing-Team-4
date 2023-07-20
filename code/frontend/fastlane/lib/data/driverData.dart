import 'dart:convert';
import 'dart:io';

import 'package:fastlane/constants/addresses.dart';
import 'package:http/http.dart' as http;

import '../model/driver.dart';
import 'accountData.dart';

/// The `DriverData` class is a singleton class that manages driver data and provides methods for
/// fetching, updating, and resetting driver information.
class DriverData {
  static DriverData? _instance;
  Driver driver;

  DriverData._privateConstructor({required this.driver});

  static DriverData? get instance {
    return _instance;
  }

  factory DriverData({required Driver driver}) {
    _instance ??= DriverData._privateConstructor(driver: driver);
    return _instance!;
  }

  /// The function fetches and sets the driver data from an API endpoint using the user and account IDs.
  static Future<void> fetchAndSetDriver() async {
    final response = await http.get(
        Uri.parse(
            '$BACKEND_ADDRESS/driver/user/${AccountData.instance.getAccount().user.id}/${AccountData.instance.getAccount().id}'),
        headers: {
          HttpHeaders.authorizationHeader:
              'Bearer ${AccountData.instance.getAuthentication().accessToken}'
        });

    if (response.statusCode == 200) {
      if (DriverData.instance == null) {
        DriverData(driver: Driver.fromJson(json.decode(response.body)));
      } else {
        DriverData.instance!.driver =
            Driver.fromJson(json.decode(response.body));
      }
    } else {
      throw Exception('Fehler beim Setzen des Drivers');
    }
  }

  /// The function `patchDriver` sends a PATCH request to update a driver's information with the
  /// provided `id` and `driver` object.
  /// 
  /// Args:
  ///   id (int): The `id` parameter is the identifier of the driver that you want to patch/update.
  ///   driver (Driver): The "driver" parameter is an object of type "Driver" that contains information
  /// about the driver, such as their license ID and fare type name.
  Future<void> patchDriver(int id, Driver driver) async {
    final Uri uri = Uri.parse(
        "$BACKEND_ADDRESS/driver/$id/${AccountData.instance.getAccount().id}");

    final Map<String, String> headers = {
      HttpHeaders.contentTypeHeader: 'application/json; charset=UTF-8',
      HttpHeaders.authorizationHeader:
          'Bearer ${AccountData.instance.getAuthentication().accessToken}'
    };

    final Map<String, String> body = {
      "licenseId": driver.licenseId,
      "fareTypeName": driver.fareType.name,
    };

    final response =
        await http.patch(uri, headers: headers, body: json.encode(body));

    if (response.statusCode == 200) {
      await fetchAndSetDriver();
    } else {
      throw Exception(response.body.toString());
    }
  }

  /// The function `fareTypeFromName` takes a name as input and returns a `FareType` object with a
  /// corresponding price based on the name.
  /// 
  /// Args:
  ///   name (String): The name parameter is a string that represents the name of a fare type.
  /// 
  /// Returns:
  ///   The method `fareTypeFromName` returns an instance of the `FareType` class.
  FareType fareTypeFromName(String name) {
    switch (name) {
      case "JUNIOR":
        return FareType(name: name, price: 2.0);
      case "COMFORT":
        return FareType(name: name, price: 1.5);
      case "PREMIUM":
        return FareType(name: name, price: 1.0);
      default:
        return FareType(name: name, price: 2.0);
    }
  }

  void resetDriver() {
    _instance = null;
  }

  Driver getDriver() {
    return driver;
  }
}
