import 'dart:convert';
import 'dart:io';

import 'package:fastlane/constants/addresses.dart';
import 'package:fastlane/data/driverData.dart';
import 'package:fastlane/data/vehicleData.dart';
import 'package:http/http.dart' as http;

import '../model/reservation.dart';
import 'accountData.dart';

/// The `ReservationData` class is responsible for fetching, refreshing, and managing reservations data
/// from an API.
class ReservationData {
  static ReservationData? _instance;
  late Future<List<Reservation>> _reservations;
  http.Client? client;
  late Future<List<Reservation>> _reservationsOfDriver;

  /// The `ReservationData._privateConstructor()` is a private constructor method that is called when
  /// creating an instance of the `ReservationData` class.
  ReservationData._privateConstructor() {
    _reservations = _fetchReservations();
    if (DriverData.instance?.getDriver() != null) {
      _reservationsOfDriver =
          _fetchReservationsByDriverId(DriverData.instance!.getDriver().id);
    } else {
      _reservationsOfDriver = _fetchReservations();
    }
  }

  /// The function fetches a list of reservations from a server using an HTTP GET request.
  /// 
  /// Returns:
  ///   The method `_fetchReservations()` returns a `Future` object that resolves to a `List` of
  /// `Reservation` objects.
  Future<List<Reservation>> _fetchReservations() async {
    final response =
        await http.get(Uri.parse("$BACKEND_ADDRESS/reservation"), headers: {
      HttpHeaders.authorizationHeader:
          'Bearer ${AccountData.instance.getAuthentication().accessToken}'
    });

    if (response.statusCode == 200) {
      List<Reservation> reservationList = [];
      final jsonData = json.decode(response.body);
      for (var vehicle in jsonData) {
        reservationList.add(Reservation.fromJson(vehicle));
      }
      return reservationList;
    } else {
      throw Exception(
          'Failed to load reservations ERROR ${response.statusCode}');
    }
  }

  Future<void> refresh() async {
    _reservations = _fetchReservations();
  }

  /// The function refreshes a list of reservations by vehicle ID.
  /// 
  /// Args:
  ///   vehicleId (int): The vehicleId parameter is an integer that represents the unique identifier of
  /// a vehicle.
  Future<void> refreshByVehicleId(int vehicleId) async {
    List<Reservation> allReservations = await _reservations;
    List<Reservation> otherReservations =
        allReservations.where((res) => res.vehicle != vehicleId).toList();
    List<Reservation> vehicleReservations = [];

    final response = await http.get(
        Uri.parse('$BACKEND_ADDRESS/reservation/vehicle/$vehicleId'),
        headers: {
          HttpHeaders.contentTypeHeader: 'application/json; charset=UTF-8',
          HttpHeaders.authorizationHeader:
              'Bearer ${AccountData.instance.getAuthentication().accessToken}'
        });

    if (response.statusCode == 200) {
      final jsonData = json.decode(response.body);
      for (var vehicle in jsonData) {
        vehicleReservations.add(Reservation.fromJson(vehicle));
      }
    } else {
      throw Exception('Failed to load vehicles ${response.statusCode}');
    }

    otherReservations.addAll(vehicleReservations);
    _reservations = Future.value(otherReservations);
  }

  /// The function `getReservationsByVehicleId` returns a future that resolves to a list of reservations
  /// filtered by a specific vehicle ID.
  /// 
  /// Args:
  ///   vehicleId (int): The vehicleId parameter is an integer that represents the unique identifier of
  /// a vehicle.
  /// 
  /// Returns:
  ///   a `Future` that resolves to a `List` of `Reservation` objects.
  Future<List<Reservation>> getReservationsByVehicleId(int vehicleId) async {
    List<Reservation> allReservations = await _reservations;
    return Future.value(
        allReservations.where((res) => res.vehicle == vehicleId).toList());
  }

  /// The function deletes a reservation by its ID using an HTTP DELETE request.
  /// 
  /// Args:
  ///   id (int): The `id` parameter is the unique identifier of the reservation that you want to
  /// delete.
  Future<void> deleteReservationById(int id) async {
    final response =
        await http.delete(Uri.parse("$BACKEND_ADDRESS/reservation/$id"), headers: {
      HttpHeaders.contentTypeHeader: 'application/json; charset=UTF-8',
      HttpHeaders.authorizationHeader:
          'Bearer ${AccountData.instance.getAuthentication().accessToken}'
    });
    if (response.statusCode == 204) {
      List<Reservation> allReservations =  await ReservationData.instance.getReservations();
      Reservation specificReservation = allReservations.where((res) => res.id == id).first;
      await ReservationData.instance.refreshByVehicleId(specificReservation.vehicle);
    } else {
      throw Exception(response.body.toString());
    }
  }

  static ReservationData get instance {
    _instance ??= ReservationData._privateConstructor();
    return _instance!;
  }

  Future<List<Reservation>> getReservations() {
    return _reservations;
  }

  /// The function fetches a list of reservations by driver ID from a server.
  /// 
  /// Args:
  ///   driverId (int): The driverId parameter is an integer that represents the unique identifier of a
  /// driver. It is used to fetch reservations associated with a specific driver.
  /// 
  /// Returns:
  ///   The method is returning a Future object that resolves to a List of Reservation objects.
  Future<List<Reservation>> _fetchReservationsByDriverId(int driverId) async {
    final response = await http
        .get(Uri.parse('$BACKEND_ADDRESS/reservation/driver/$driverId'), headers: {
      HttpHeaders.contentTypeHeader: 'application/json; charset=UTF-8',
      HttpHeaders.authorizationHeader:
          'Bearer ${AccountData.instance.getAuthentication().accessToken}'
    });
    if (response.statusCode == 200) {
      List<Reservation> reservationList = [];
      final jsonData = json.decode(response.body);
      for (var vehicle in jsonData) {
        reservationList.add(Reservation.fromJson(vehicle));
      }
      return reservationList;
    } else {
      throw Exception(
          'Failed to load reservations ERROR ${response.statusCode}');
    }
  }

  Future<List<Reservation>> getReservationsOfDriver() {
    return _reservationsOfDriver;
  }

  Future<void> refreshReservationsOfDriver() async {
    _reservationsOfDriver =
        _fetchReservationsByDriverId(DriverData.instance!.getDriver().id);
  }

  /// The function sets the reservation state to "paid" for a given ID.
  /// 
  /// Args:
  ///   id (int): The `id` parameter is an integer that represents the unique identifier of a
  /// reservation.
  Future<void> setReservationStateOnPaid(int id) async {
    final Uri uri = Uri.parse("$BACKEND_ADDRESS/reservation/$id");

    final Map<String, String> headers = {
      HttpHeaders.contentTypeHeader: 'application/json; charset=UTF-8',
      HttpHeaders.authorizationHeader:
          'Bearer ${AccountData.instance.getAuthentication().accessToken}'
    };

    final Map<String, String> body = {
      "reservationState": "PAID",
    };

    final response =
        await http.patch(uri, headers: headers, body: json.encode(body));

    if (response.statusCode == 200) {
    } else {
      throw Exception(response.body.toString());
    }
  }
}
