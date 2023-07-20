import 'dart:convert';
import 'dart:io';

import 'package:auto_size_text/auto_size_text.dart';
import 'package:fastlane/constants/addresses.dart';
import 'package:fastlane/constants/colors.dart';
import 'package:fastlane/model/reservationRequest.dart';
import 'package:fastlane/widget/helper/mainCard.dart';
import 'package:fastlane/widget/helper/mainScrollable.dart';
import 'package:fastlane/widget/helper/networkImage.dart';
import 'package:fastlane/widget/vehiclePage/reservationCalendar.dart';
import 'package:fastlane/widget/vehiclePage/timeframePicker.dart';
import 'package:fastlane/widget/vehiclePage/vehicleMap.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart';
import 'package:http/http.dart' as http;
import 'package:intl/intl.dart';

import '../../constants/border.dart';
import '../../constants/numbers.dart';
import '../../constants/padding.dart';
import '../../control/mediaChecker.dart';
import '../../data/accountData.dart';
import '../../data/driverData.dart';
import '../../data/reservationData.dart';
import '../../model/reservation.dart';
import '../../model/vehicle.dart';

/// The `FastlaneDashboardVehiclePage` class is a stateful widget that represents a
/// page in a fastlane dashboard for a specific vehicle.
class FastlaneDashboardVehiclePage extends StatefulWidget {
  final Vehicle vehicle;

  const FastlaneDashboardVehiclePage({super.key, required this.vehicle});

  @override
  State<StatefulWidget> createState() => FastlaneDashboardVehiclePageState();
}

/// The `_FastlaneDashboardVehiclePageState` class is a stateful widget that
/// displays information about a vehicle and allows users to make reservations.
class FastlaneDashboardVehiclePageState
    extends State<FastlaneDashboardVehiclePage> {
  late Future<List<Reservation>> _reservations;
  DateTime? _startDateTime;
  DateTime? _endDateTime;
  String? _reservationPriceText;
  double? _reservationPrice = DriverData.instance?.driver.fareType.price;
  final priceFormat = NumberFormat.currency(locale: "de", symbol: "€");


  void init() {
    debugPrint("initState");
    if (!AccountData.instance.isEmployee()) {
      _reservationPriceText =
          '${priceFormat.format(DriverData.instance?.driver.fareType.price)} / Stunde';
    }
    ReservationData.instance.refreshByVehicleId(widget.vehicle.id);
    _reservations =
        ReservationData.instance.getReservationsByVehicleId(widget.vehicle.id);
  }

  Widget _createAttributeIcon(IconData icon, String text) {
    return FittedBox(
        child: Container(
      padding: MIDDLE_PADDING,
      decoration: const BoxDecoration(
          borderRadius: CARD_BORDERRADIUS, color: GRAY_BLUEISH),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(icon, size: MediaController.isSmallScreen(context) ? 20 : 25),
          Text(text,
              style: TextStyle(
                  fontSize: MediaController.isSmallScreen(context) ? 12 : 15)),
        ],
      ),
    ));
  }

  Widget _createAttributeDisplay() {
    List<Widget> optional = [];
    if (widget.vehicle.configuration.ac) {
      optional.add(_createAttributeIcon(Icons.ac_unit, "Klimaanlage"));
    }
    if (widget.vehicle.configuration.navigation) {
      optional.add(_createAttributeIcon(Icons.location_on, "GPS"));
    }
    if (widget.vehicle.configuration.cruiseControl) {
      optional.add(_createAttributeIcon(Icons.speed, "Tempomat"));
    }
    if (widget.vehicle.configuration.drivingAssistent) {
      optional
          .add(_createAttributeIcon(Icons.arrow_circle_up, "Fahrassistent"));
    }

    return Wrap(
      direction: Axis.horizontal,
      crossAxisAlignment: WrapCrossAlignment.start,
      spacing: 10,
      runSpacing: 10,
      children: [
        _createAttributeIcon(
            Icons.local_gas_station, widget.vehicle.configuration.fuel),
        _createAttributeIcon(
            Icons.candlestick_chart, widget.vehicle.configuration.transmission),
        if (optional.isNotEmpty) optional.removeAt(0),
        if (optional.isNotEmpty) optional.removeAt(0),
        if (optional.isNotEmpty) optional.removeAt(0),
        if (optional.isNotEmpty) optional.removeAt(0),
      ],
    );
  }

  Widget _createTimespanPicker() {
    return Container(
      height: 200,
      width: DEFAULT_FULLSCREEN_CARD_WIDTH * 0.3,
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(16),
      ),
      child: Column(
        children: [
          TimeframePicker(
            getTimes: _fetchDateTimes,
          ),
          const Spacer(),
          ElevatedButton(
              style: const ButtonStyle(
                  backgroundColor: MaterialStatePropertyAll(Colors.black),
                  alignment: Alignment.center,
                  minimumSize: MaterialStatePropertyAll(Size(50, 50))),
              onPressed: _reservationButtonPressed,
              child: const Row(
                mainAxisSize: MainAxisSize.max,
                children: [Icon(Icons.add, size: 30), Text('Buchen')],
              )),
        ],
      ),
    );
  }

  /// The `_showAlert` function displays an alert dialog with an icon and text, and
  /// automatically dismisses it after 3 seconds.
  ///
  /// Args:
  ///   icon (Icon): The "icon" parameter is of type Icon, which represents an icon
  /// widget in Flutter. It is used to display an icon in the title of the
  /// AlertDialog widget.
  ///   text (String): The "text" parameter is a string that represents the message
  /// or content of the alert dialog.
  ///
  /// Returns:
  ///   The function `_showAlert` returns nothing (void).
  void _showAlert(Icon icon, String text) {
    showDialog(
      context: context,
      builder: (context) {
        return FutureBuilder(
          future:
              Future.delayed(const Duration(seconds: 3)).then((value) => true),
          builder: (context, snapshot) {
            if (snapshot.hasData) {
              Navigator.of(context).pop();
            }

            return AlertDialog(
                title: Row(children: [
              icon,
              const SizedBox(width: 10),
              Flexible(child: Text(text)),
            ]));
          },
        );
      },
    );
  }

  void _fetchDateTimes(DateTime startDate, DateTime endDate) {
    _startDateTime = startDate;
    _endDateTime = endDate;
    setState(() {
      calculateAndSetPrice();
    });
  }

  /// The function calculates and sets the price for a reservation based on the
  /// duration and fare type.
  void calculateAndSetPrice() {
    double price;
    final difference = _endDateTime?.difference(_startDateTime!);
    if (difference!.inMinutes <= 60) {
      price = DriverData.instance!.driver.fareType.price;
    } else {
      if (difference.inMinutes % 60 != 0) {
        price = (difference.inHours + 1) *
            DriverData.instance!.driver.fareType.price;
      } else {
        price = difference.inHours * DriverData.instance!.driver.fareType.price;
      }
    }
    _reservationPrice = price;
    _reservationPriceText = priceFormat.format(price);
  }

  /// The `_reservationButtonPressed` function checks for various conditions and
  /// makes a reservation request, displaying appropriate alerts based on the
  /// response status code.
  ///
  /// Returns:
  ///   The function does not return any value. It has a return statement in several
  /// places, but it is used to exit the function early and does not return any
  /// specific value.
  void _reservationButtonPressed() async {
    if (DriverData.instance == null) {
      _showAlert(const Icon(Icons.error, color: Colors.red),
          "Du musst ein Member sein um ein Auto zu reservieren.");
      return;
    }

    if (_startDateTime == null || _endDateTime == null) {
      _showAlert(
          const Icon(
            Icons.error,
            color: Colors.red,
          ),
          "Es wurden nicht alle Felder ausgefüllt.");
      return;
    }

    if (_startDateTime!.isBefore(DateTime.now())) {
      _showAlert(const Icon(Icons.error, color: Colors.red),
          "Die Reservierung muss in der Zukunft liegen");
      return;
    }

    if (_startDateTime!.isAfter(_endDateTime!)) {
      _showAlert(const Icon(Icons.error, color: Colors.red),
          "Der Startzeitpunkt muss vor dem Endzeitpunkt liegen.");
      return;
    }

    for (Reservation reservation
        in (await _reservations).where((res) => !res.isCanceled()).toList()) {
      if (_startDateTime!.isBefore(reservation.endDateTime) ||
          _startDateTime!.isAtSameMomentAs(reservation.endDateTime)) {
        if (reservation.startDateTime.isBefore(_endDateTime!) ||
            reservation.startDateTime.isAtSameMomentAs(_endDateTime!)) {
          _showAlert(const Icon(Icons.error, color: Colors.red),
              "Innerhalb dieser Zeitspanne existiert bereits eine Reservierung.");
          return;
        }
      }
    }

    ReservationRequest reservationRequest = ReservationRequest(
        driverId: DriverData.instance!.getDriver().id,
        vehicleId: widget.vehicle.id,
        price: _reservationPrice!,
        startDateTime: _startDateTime!,
        endDateTime: _endDateTime!,
        currencyCode: "EURO",
        reservationState: "Unpaid");

    String jsonData = jsonEncode(reservationRequest.toJson());

    Response response = await http.post(Uri.parse('$BACKEND_ADDRESS/reservation'),
        headers: {
          HttpHeaders.contentTypeHeader: 'application/json; charset=UTF-8',
          HttpHeaders.authorizationHeader:
              'Bearer ${AccountData.instance.getAuthentication().accessToken}'
        },
        body: jsonData);

    if (response.statusCode == 201) {
      _showAlert(
          const Icon(
            Icons.check,
            color: Colors.green,
          ),
          "Die Reservierung wurde erfolgreich angelegt.");
      await ReservationData.instance.refreshByVehicleId(widget.vehicle.id);
      setState(() {
        _reservations = ReservationData.instance
            .getReservationsByVehicleId(widget.vehicle.id);
      });
    } else if (response.statusCode == 409) {
      _showAlert(const Icon(Icons.error, color: Colors.red),
          "Innerhalb dieser Zeitspanne existiert bereits eine Reservierung.");
    } else if (response.statusCode == 403) {
      _showAlert(const Icon(Icons.error, color: Colors.red),
          "Der übergebene Preis stimmt nicht mit dem berechneten Wert überein.");
    } else {
      _showAlert(const Icon(Icons.error, color: Colors.red),
          "Es gab einen unerwarteten Fehler. Bitte versuche es später erneut.");
    }
  }

  Widget _createPrice() {
    String fareType = DriverData.instance!.driver.fareType.name;
    fareType = fareType.substring(0, 1).toUpperCase() +
        fareType.substring(1).toLowerCase();
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(_reservationPriceText!,
            style: TextStyle(
                fontSize: MediaController.isSmallScreen(context) ? 32 : 42,
                fontWeight: FontWeight.bold)),
        Text(fareType,
            style: TextStyle(
                fontSize: MediaController.isSmallScreen(context) ? 15 : 22,
                color: GRAY))
      ],
    );
  }

  Widget _createName() {
    return Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
      Text(
        widget.vehicle.configuration.category,
        style: TextStyle(
            color: LESSER_BLUE,
            fontSize: MediaController.isSmallScreen(context) ? 15 : 22),
      ),
      AutoSizeText(
        "${widget.vehicle.vehicleModel.vehicleBrand.brandName} - ${widget.vehicle.vehicleModel.modelName}",
        style: TextStyle(
            fontSize: MediaController.isSmallScreen(context) ? 27 : 42),
        maxLines: 2,
        maxFontSize: MediaController.isSmallScreen(context) ? 27 : 42,
      )
    ]);
  }

  Widget _createImage(Size size) {
    return SizedBox(
        width: size.width,
        height: size.height,
        child: FastlaneNetworkImage(
            url: widget.vehicle.vehicleModel.linkToPicture));
  }

  @override
  Widget build(BuildContext context) {
    init();
    debugPrint('Building VehiclePage');
    return Scaffold(
        body: FastlaneScrollable(
      direction: Axis.vertical,
      child: Align(
        alignment: Alignment.topCenter,
        child: Column(crossAxisAlignment: CrossAxisAlignment.center, children: [
          Wrap(
            direction: Axis.horizontal,
            children: [
              FastlaneDashboardCard(
                  height: DEFAULT_FULLSCREEN_CARD_HEIGHT * 0.45,
                  width: DEFAULT_FULLSCREEN_CARD_WIDTH * 0.45,
                  padding: BIG_PADDING,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [_createImage(const Size(500, 250))]),
              FastlaneDashboardCard(
                height: DEFAULT_FULLSCREEN_CARD_HEIGHT * 0.45,
                width: DEFAULT_FULLSCREEN_CARD_WIDTH * 0.45,
                padding: BIG_PADDING,
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  _createName(),
                  const SizedBox(height: 10),
                  _createAttributeDisplay(),
                  const Spacer(),
                  if (!AccountData.instance.isEmployee())
                    Align(
                        alignment: Alignment.center,
                        child: _createTimespanPicker()),
                  const Spacer(),
                  if (!AccountData.instance.isEmployee())
                    Align(
                        alignment: Alignment.bottomLeft, child: _createPrice())
                ],
              )
            ],
          ),
          Wrap(
            children: [
              FastlaneDashboardCard(
                  height: DEFAULT_FULLSCREEN_CARD_HEIGHT * 0.5,
                  width: DEFAULT_FULLSCREEN_CARD_WIDTH * 0.45,
                  padding: const EdgeInsets.all(15),
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Center(
                        child: ReservationCalendar(
                      reservations: _reservations,
                      size: const Size(DEFAULT_FULLSCREEN_CARD_HEIGHT * 0.45,
                          DEFAULT_FULLSCREEN_CARD_WIDTH * 0.4),
                    ))
                  ]),
              FastlaneDashboardCard(
                height: DEFAULT_FULLSCREEN_CARD_HEIGHT * 0.5,
                width: DEFAULT_FULLSCREEN_CARD_WIDTH * 0.45,
                padding: BIG_PADDING,
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Flexible(
                      child: ClipRRect(
                    borderRadius: BorderRadius.circular(15),
                    child: VehicleMap(
                      vehicle: widget.vehicle,
                    ),
                  )),
                ],
              ),
            ],
          )
        ]),
      ),
    ));
  }
}
