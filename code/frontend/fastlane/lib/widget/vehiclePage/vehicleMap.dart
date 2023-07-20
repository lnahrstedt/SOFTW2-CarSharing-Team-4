import 'package:flutter/material.dart';
import 'package:flutter_map/flutter_map.dart';
import 'package:latlong2/latlong.dart';

import '../../model/vehicle.dart';

/// The VehicleMap class is a StatefulWidget that represents a map of a specific
/// vehicle.
class VehicleMap extends StatefulWidget {
  final Vehicle vehicle;

  const VehicleMap({super.key, required this.vehicle});

  @override
  State<VehicleMap> createState() => _VehicleMapState();
}

/// The `_VehicleMapState` class generates a FlutterMap widget with a tile layer and
/// a marker representing a vehicle's location.
class _VehicleMapState extends State<VehicleMap> {
  FlutterMap _generateMap() {
    return FlutterMap(
      options: MapOptions(
          center: LatLng(widget.vehicle.latitude, widget.vehicle.longitude),
          zoom: 15,
          maxZoom: 18,
          minZoom: 3,
          interactiveFlags: InteractiveFlag.all & ~InteractiveFlag.rotate),
      children: [
        TileLayer(
          urlTemplate: 'https://tile.openstreetmap.org/{z}/{x}/{y}.png',
          subdomains: const ['a', 'b', 'c'],
        ),
        MarkerLayer(
          markers: [
            Marker(
                height: 30,
                width: 30,
                point:
                    LatLng(widget.vehicle.latitude, widget.vehicle.longitude),
                anchorPos: AnchorPos.align(AnchorAlign.top),
                builder: (BuildContext context) {
                  return const Icon(Icons.location_on,
                      color: Colors.red, size: 30);
                }),
          ],
        )
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    return _generateMap();
  }
}
