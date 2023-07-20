import 'package:fastlane/data/geoPosition.dart';
import 'package:fastlane/widget/mapPage/fastlaneMap.dart';
import 'package:flutter/material.dart';

import '../../model/vehicle.dart';

/// The `FutureFastlaneMap` class is a stateful widget that displays a map and takes
/// a future list of vehicles as input.
class FutureFastlaneMap extends StatefulWidget {
  final Future<List<Vehicle>> vehicles;

  const FutureFastlaneMap({super.key, required this.vehicles});

  @override
  State<FutureFastlaneMap> createState() => _FutureFastlaneMapState();
}

/// The `_FutureFastlaneMapState` class is a stateful widget that uses a
/// `FutureBuilder` to conditionally load the position and display a `FastlaneMap`
/// widget based on the loaded data.
class _FutureFastlaneMapState extends State<FutureFastlaneMap> {
  bool loadedPosition = false;

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
        future: loadedPosition
            ? Future.wait([widget.vehicles])
            : Future.wait(
                [widget.vehicles, GeoPosition.instance.getPosition()]),
        builder: (context, AsyncSnapshot<List<dynamic>> snapshot) {
          if (snapshot.connectionState == ConnectionState.done) {
            if (!snapshot.hasError) {
              if (!loadedPosition) {
                if (snapshot.data![1] == null) {
                  return FastlaneMap(vehicles: snapshot.data![0]);
                } else {
                  return FastlaneMap(
                    vehicles: snapshot.data![0],
                    position: snapshot.data![1],
                  );
                }
              } else {
                return Text(snapshot.error.toString());
              }
            } else {
              return Text(snapshot.error.toString());
            }
          } else {
            return const SizedBox(
                height: 50,
                width: 50,
                child: Center(child: CircularProgressIndicator()));
          }
        });
  }
}
