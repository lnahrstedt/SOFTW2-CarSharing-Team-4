import 'package:geolocator/geolocator.dart';
import 'package:latlong2/latlong.dart';

/// The `GeoPosition` class is a singleton that provides a way to fetch and store the current geographic
/// position using the Geolocator package in Dart.
class GeoPosition {
  static GeoPosition? _instance;
  late Future<LatLng?> _position;

  GeoPosition._privateConstructor() {
    _position = _fetchAndSetPosition();
  }

  static GeoPosition get instance {
    _instance ??= GeoPosition._privateConstructor();
    return _instance!;
  }

  /// The function `_fetchAndSetPosition` fetches the current device's location and returns it as a
  /// `LatLng` object.
  /// 
  /// Returns:
  ///   The function `_fetchAndSetPosition` returns a `Future` object that resolves to a `LatLng` object
  /// or `null`.
  Future<LatLng?> _fetchAndSetPosition() async {
    LocationPermission permission = await Geolocator.checkPermission();
    if (permission == LocationPermission.denied) {
      permission = await Geolocator.requestPermission();
      if (permission == LocationPermission.denied) {
        return Future.value(null);
      }
    }

    if (permission == LocationPermission.deniedForever) {
      return Future.value(null);
    }

    Position geoPosition = await Geolocator.getCurrentPosition(
        desiredAccuracy: LocationAccuracy.best);
    return Future.value(LatLng(geoPosition.latitude, geoPosition.longitude));
  }

  Future<void> refresh() async {
    _position = _fetchAndSetPosition();
  }

  Future<LatLng?> getPosition() {
    return _position;
  }
}
