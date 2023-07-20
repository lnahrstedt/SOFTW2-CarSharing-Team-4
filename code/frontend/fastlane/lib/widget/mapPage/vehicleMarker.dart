import 'dart:async';

import 'package:fastlane/widget/helper/platformHelper.dart';
import 'package:fastlane/widget/mapPage/vehicleCard.dart';
import 'package:flutter/material.dart';

import '../../model/vehicle.dart';

/// The `VehicleMarkerButton` class is a stateful widget that represents a button
/// for a specific vehicle marker and allows the user to toggle movement for that
/// vehicle.
class VehicleMarkerButton extends StatefulWidget {
  final Vehicle vehicle;
  final Function(bool) setAllowMovement;

  const VehicleMarkerButton({
    super.key,
    required this.vehicle,
    required this.setAllowMovement,
  });

  @override
  State<VehicleMarkerButton> createState() => _VehicleMarkerButtonState();
}

/// The `_VehicleMarkerButtonState` class is responsible for managing the visibility
/// and behavior of a marker button and a card widget in a Flutter application.
class _VehicleMarkerButtonState extends State<VehicleMarkerButton> {
  bool _cardVisible = false;
  Timer? _timer;

  void _enterMarker() {
    _timer = Timer(
        const Duration(milliseconds: 300),
        () => {
              if (mounted)
                {
                  setState(() {
                    _cardVisible = true;
                  }),
                  widget.setAllowMovement(false)
                }
            });
  }

  void _exitMarker() {
    _timer?.cancel();
  }

  void _exitCard() {
    setState(() {
      _cardVisible = false;
    });
    _timer?.cancel();
    widget.setAllowMovement(true);
  }

  void _setCardVisibility(bool visiblity) {
    setState(() {
      _cardVisible = visiblity;
    });
    widget.setAllowMovement(!visiblity);
  }

  Widget _createMarker() {
    return MouseRegion(
      onEnter: (_) => _enterMarker(),
      onExit: (_) => _exitMarker(),
      child: const Icon(
        Icons.directions_car,
        color: Colors.redAccent,
        size: 30.0,
      ),
    );
  }

  Widget _createCard() {
    return Column(mainAxisSize: MainAxisSize.min, children: [
      Visibility(
        maintainSize: true,
        maintainAnimation: true,
        maintainState: true,
        visible: _cardVisible,
        child: VehicleCard(vehicle: widget.vehicle),
      ),
      const SizedBox(
        height: 10,
      ),
      _createMarker(),
    ]);
  }

  Widget _createSpaceholder() {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        const SizedBox(height: 320),
        _createMarker(),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    if (PlatformHelper.isMobile()) {
      return _cardVisible
          ? TapRegion(
              onTapOutside: (_) => _setCardVisibility(false),
              child: _createCard(),
            )
          : GestureDetector(
              onTap: () => _setCardVisibility(true),
              child: _createSpaceholder());
    } else {
      return _cardVisible
          ? MouseRegion(
              onExit: (_) => _exitCard(),
              child: _createCard(),
            )
          : _createSpaceholder();
    }
  }
}
