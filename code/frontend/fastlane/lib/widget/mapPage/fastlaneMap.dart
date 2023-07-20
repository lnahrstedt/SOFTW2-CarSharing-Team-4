import 'package:fastlane/widget/helper/platformHelper.dart';
import 'package:fastlane/widget/mapPage/vehicleMarker.dart';
import 'package:flutter/material.dart';
import 'package:flutter_map/flutter_map.dart';
import 'package:geolocator/geolocator.dart';
import 'package:latlong2/latlong.dart';

import '../../constants/coordinates.dart';
import '../../constants/padding.dart';
import '../../data/accountData.dart';
import '../../model/vehicle.dart';

/// The `FastlaneMap` class is a stateful widget that represents a map with vehicles
/// and a position.
class FastlaneMap extends StatefulWidget {
  final List<Vehicle> vehicles;
  final LatLng? position;

  const FastlaneMap({super.key, required this.vehicles, this.position});

  @override
  State<StatefulWidget> createState() => _FastlaneMapState();
}

/// The `_FastlaneMapState` class is responsible for managing the state and
/// rendering of a map widget in a Flutter application, including markers for
/// vehicles and user interaction.
class _FastlaneMapState extends State<FastlaneMap> {
  late LatLng _geoPosition;
  late LatLng _mapPosition = _geoPosition;
  bool _allowMovement = true;
  bool _hasClicked = false;
  late double _mapZoom;
  int _maxDistanceKilometers = 5;

  final MapController _mapController = MapController();

  @override
  void initState() {
    if (widget.position != null) {
      _geoPosition = widget.position!;
      _mapZoom = 15;
    } else {
      _geoPosition = GERMANY;
      _mapZoom = 6;
    }
    super.initState();
  }

  void _setAllowMovement(bool drag) {
    setState(() {
      _allowMovement = drag;
    });
  }

  /// The function `_generateMap` generates a FlutterMap widget with markers and
  /// circles based on a list of vehicles.
  ///
  /// Args:
  ///   vehicles (List<Vehicle>): A list of Vehicle objects.
  ///
  /// Returns:
  ///   a FlutterMap widget.
  FlutterMap _generateMap(List<Vehicle> vehicles) {
    final markerList = <Marker>[];
    final circleList = <CircleMarker>[];
    if (!AccountData.instance.isEmployee()) {
      markerList.add(Marker(
          point: _geoPosition,
          builder: (c) => const Icon(
                Icons.person,
                color: Colors.green,
                size: 30,
              )));

      circleList.add(CircleMarker(
          point: _geoPosition,
          radius: _maxDistanceKilometers * 1000,
          useRadiusInMeter: true,
          color: Colors.blueAccent.withOpacity(0.1),
          borderColor: Colors.blueAccent,
          borderStrokeWidth: 2));
    }

    for (Vehicle vehicle in vehicles) {
      if (AccountData.instance.isEmployee() ||
          _maxDistanceKilometers * 1000 >=
              Geolocator.distanceBetween(vehicle.latitude, vehicle.longitude,
                  _geoPosition.latitude, _geoPosition.longitude)) {
        markerList.add(Marker(
          height: 360,
          width: 275,
          anchorPos: AnchorPos.exactly(Anchor(137.5, 15)),
          point: LatLng(vehicle.latitude, vehicle.longitude),
          builder: (context) => VehicleMarkerButton(
              vehicle: vehicle, setAllowMovement: _setAllowMovement),
        ));
      }
    }

    markerList.sort((a, b) => b.point.latitude.compareTo(a.point.latitude));
    int interactiveFlag = InteractiveFlag.all & ~InteractiveFlag.rotate;
    if (!_allowMovement) {
      interactiveFlag = InteractiveFlag.none;
    }
    return FlutterMap(
      mapController: _mapController,
      options: MapOptions(
          onTap: (!AccountData.instance.isEmployee() &&
                  !(PlatformHelper.isMobile()))
              ? _onMapTap
              : null,
          onLongPress: (!AccountData.instance.isEmployee() &&
                  (PlatformHelper.isMobile()))
              ? _onMapTap
              : null,
          center: _mapPosition,
          zoom: _mapZoom,
          maxZoom: 18,
          minZoom: 3,
          interactiveFlags: interactiveFlag),
      children: [
        TileLayer(
          urlTemplate: 'https://tile.openstreetmap.org/{z}/{x}/{y}.png',
          subdomains: const ['a', 'b', 'c'],
        ),
        CircleLayer(circles: circleList),
        MarkerLayer(markers: markerList),
      ],
    );
  }

  /// The function updates the state variables with the map position, zoom level,
  /// and geographic position when the map is tapped.
  ///
  /// Args:
  ///   tapPosition (TapPosition): The tapPosition parameter represents the position
  /// of the tap on the map. It provides information about the x and y coordinates
  /// of the tap relative to the map view.
  ///   latLng (LatLng): The `latLng` parameter in the `_onMapTap` function
  /// represents the geographical position (latitude and longitude) where the user
  /// tapped on the map.
  void _onMapTap(TapPosition tapPosition, LatLng latLng) {
    setState(() {
      _hasClicked = true;
      _mapPosition = _mapController.center;
      _mapZoom = _mapController.zoom;
      _geoPosition = latLng;
    });
  }

  /// The `_createDropdown` function returns a container with a dropdown button for
  /// selecting a maximum distance in kilometers.
  ///
  /// Returns:
  ///   The function `_createDropdown` returns a `Widget`, specifically a
  /// `Container` widget with a `Row` as its child. The `Row` contains a `Text`
  /// widget and a `DropdownButton` widget.
  Widget _createDropdown() {
    return Container(
        decoration: BoxDecoration(
            color: Colors.white, borderRadius: BorderRadius.circular(16)),
        height: 100,
        width: 300,
        padding: SMALL_PADDING,
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            const Text("Suchen im Umkreis von: "),
            DropdownButton<int>(
                value: _maxDistanceKilometers,
                icon: const Icon(Icons.arrow_downward),
                elevation: 0,
                style: const TextStyle(color: Colors.deepPurple),
                items: <int>[1, 2, 3, 5, 10, 15, 20].map((int value) {
                  return DropdownMenuItem<int>(
                    value: value,
                    alignment: Alignment.center,
                    child: Text("$value km"),
                  );
                }).toList(),
                onChanged: (int? value) {
                  setState(() {
                    _maxDistanceKilometers = value!;
                    _mapPosition = _mapController.center;
                    _mapZoom = _mapController.zoom;
                  });
                })
          ],
        ));
  }

  /// The function returns an animated container with text and an icon, which is
  /// only visible if a certain condition is met.
  ///
  /// Returns:
  ///   an AnimatedOpacity widget.
  Widget _createNavigationInfo() {
    return AnimatedOpacity(
        opacity: _hasClicked ? 0.0 : 1.0,
        duration: const Duration(seconds: 1),
        child: Container(
            decoration: BoxDecoration(
                color: Colors.white, borderRadius: BorderRadius.circular(16)),
            height: 50,
            width: 300,
            padding: SMALL_PADDING,
            child: const Row(
              children: [
                Icon(Icons.info_outline),
                SizedBox(width: 10),
                Text('Klicke auf die Karte,\num deine Position zu ändern.')
              ],
            )));
  }

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(
        builder: (BuildContext context, BoxConstraints constrains) {
      bool dropdownVisible =
          constrains.maxWidth > 500 && constrains.maxHeight > 500;
      return Stack(
        children: [
          _generateMap(widget.vehicles),
          if (dropdownVisible && !AccountData.instance.isEmployee())
            Positioned(top: 10, right: 10, child: _createDropdown()),
          if (dropdownVisible && !AccountData.instance.isEmployee())
            Positioned(top: 120, right: 10, child: _createNavigationInfo())
        ],
      );
    });
  }
}
