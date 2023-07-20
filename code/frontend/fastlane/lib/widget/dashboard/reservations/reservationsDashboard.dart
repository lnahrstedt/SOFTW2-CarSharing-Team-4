import 'package:fastlane/constants/colors.dart';
import 'package:fastlane/constants/numbers.dart';
import 'package:fastlane/constants/padding.dart';
import 'package:fastlane/data/driverData.dart';
import 'package:fastlane/widget/dashboard/home/actualReservation.dart';
import 'package:fastlane/widget/dashboard/reservations/reservationEntry.dart';
import 'package:fastlane/widget/helper/mainCard.dart';
import 'package:fastlane/widget/helper/mainScrollable.dart';
import 'package:flutter/material.dart';

import '../../../data/reservationData.dart';
import '../../../data/vehicleData.dart';
import '../../../model/reservation.dart';
import '../../../model/vehicle.dart';
import '../../helper/mainContainer.dart';
import '../../helper/widget.dart';
import '../cars/allCarsDashboard.dart';

/// The class FastlaneDashboardReservations is a StatefulWidget in Dart.
class FastlaneDashboardReservations extends StatefulWidget {
  const FastlaneDashboardReservations({
    Key? key,
  }) : super(key: key);

  @override
  State<FastlaneDashboardReservations> createState() =>
      FastlaneDashboardReservationsState();
}

/// The `FastlaneDashboardReservationsState` class is responsible for displaying
/// different categories of reservations in a dashboard, including active, canceled,
/// and personal reservations.
class FastlaneDashboardReservationsState
    extends State<FastlaneDashboardReservations> {
  callback() {
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: Future.wait([
        DriverData.instance?.getDriver() == null
            ? ReservationData.instance.refresh()
            : ReservationData.instance.refreshReservationsOfDriver(),
        VehicleData.instance.refresh(),
        DriverData.instance?.getDriver() == null
            ? ReservationData.instance.getReservations()
            : ReservationData.instance.getReservationsOfDriver(),
        VehicleData.instance.getVehicles()
      ]),
      builder: (BuildContext context, AsyncSnapshot<dynamic> snapshot) {
        if (snapshot.connectionState == ConnectionState.done) {
          if (!snapshot.hasError) {
            List<Reservation> reservationList = snapshot.data[2];
            List<Vehicle> vehicleList = snapshot.data[3];

            List<Reservation>? paidOrUnpaidReservation;
            List<Reservation>? actualReservations;
            List<Reservation>? canceledReservation;

            if (reservationList.isNotEmpty && vehicleList.isNotEmpty) {
              paidOrUnpaidReservation = reservationList
                  .where((element) => element.reservationState != "CANCELED")
                  .toList();
              paidOrUnpaidReservation
                  .sort((a, b) => a.startDateTime.compareTo(b.startDateTime));
              actualReservations = paidOrUnpaidReservation
                  .where((element) =>
                      DateTime.now().isAfter(element.startDateTime) &&
                      DateTime.now().isBefore(element.endDateTime))
                  .toList();
              canceledReservation = reservationList
                  .where((element) => element.reservationState == "CANCELED")
                  .toList();
              canceledReservation
                  .sort((a, b) => a.startDateTime.compareTo(b.startDateTime));
            }

            return FastlaneScrollable(
                direction: Axis.vertical,
                child: reservationList.isNotEmpty
                    ? FastlaneDashboardMainContainer(
                        title: "Buchungen",
                        wrap: false,
                        widgets: [
                            Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                FittedBox(
                                  child: Container(
                                    margin: const EdgeInsets.only(
                                        left: 10, top: 10),
                                    padding: MIDDLE_PADDING,
                                    decoration: const BoxDecoration(
                                        color: FLOJC_GREEN_ACCENT,
                                        borderRadius: BorderRadius.all(
                                            Radius.circular(20))),
                                    child: const Text(
                                      "Aktive Buchungen",
                                      style: TextStyle(
                                          fontSize: 16,
                                          fontWeight: FontWeight.w500,
                                          color: FLOJC_GREEN),
                                    ),
                                  ),
                                ),
                                actualReservations!.isNotEmpty
                                    ? SizedBox(
                                        width: DEFAULT_FULLSCREEN_CARD_WIDTH,
                                        height: DEFAULT_CARD_HEIGHT * 1.7,
                                        child: ListView(
                                          shrinkWrap: true,
                                          scrollDirection: Axis.horizontal,
                                          children: actualReservations
                                              .map((reservation) =>
                                                  FastlaneDashboardNextReservations(
                                                      reservation: reservation,
                                                      vehicle: vehicleList
                                                          .where((element) =>
                                                              element.id ==
                                                              reservation
                                                                  .vehicle)
                                                          .first))
                                              .toList(),
                                        ),
                                      )
                                    : const FastlaneDashboardReservationsNoEntry(),
                              ],
                            ),
                            FastlaneDashboardReservationsCategory(
                                title: "Deine Buchungen",
                                reservations: paidOrUnpaidReservation!,
                                callback: callback,
                                vehicles: vehicleList,
                                backgroundTextColor: FLOJC_BLUE_ACCENT,
                                textColor: LESSER_BLUE),
                            FastlaneDashboardReservationsCategory(
                                title: "Storniert",
                                reservations: canceledReservation!,
                                callback: callback,
                                vehicles: vehicleList,
                                backgroundTextColor: GRAY_BLUEISH,
                                textColor: INBETWEEN_GRAY)
                          ])
                    : const FastlaneDashboardReservationsNoEntries(
                        text: "Derzeit leider keine Buchungen",
                      ));
          } else {
            return Text(snapshot.error.toString());
          }
        } else {
          return const Center(
            child: SizedBox(
                height: 50,
                width: 50,
                child: Center(child: CircularProgressIndicator())),
          );
        }
      },
    );
  }
}

/// The `FastlaneDashboardReservationsCategory` class is a stateful widget that
/// represents a category of reservations in a dashboard, with customizable title,
/// reservations, vehicles, background text color, text color, and a callback
/// function.
class FastlaneDashboardReservationsCategory extends StatefulWidget {
  const FastlaneDashboardReservationsCategory(
      {Key? key,
      required this.title,
      required this.reservations,
      required this.callback,
      required this.vehicles,
      required this.backgroundTextColor,
      required this.textColor})
      : super(key: key);

  final String title;
  final List<Reservation> reservations;
  final List<Vehicle> vehicles;
  final Color backgroundTextColor;
  final Color textColor;
  final Function callback;

  @override
  State<FastlaneDashboardReservationsCategory> createState() =>
      _FastlaneDashboardReservationsCategoryState();
}

/// The `_FastlaneDashboardReservationsCategoryState` class is responsible for
/// building the UI for a reservations category in a Fastlane dashboard, including a
/// title and a list of reservation entries.
class _FastlaneDashboardReservationsCategoryState
    extends State<FastlaneDashboardReservationsCategory> {
  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        FittedBox(
          child: Container(
            margin: const EdgeInsets.only(left: 10, top: 10),
            padding: MIDDLE_PADDING,
            decoration: BoxDecoration(
                color: widget.backgroundTextColor,
                borderRadius: const BorderRadius.all(Radius.circular(20))),
            child: Text(
              widget.title,
              style: TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.w500,
                  color: widget.textColor),
            ),
          ),
        ),
        SizedBox(
          height: 235,
          child: widget.reservations.isNotEmpty
              ? ListView(
                  shrinkWrap: true,
                  scrollDirection: Axis.horizontal,
                  children: widget.reservations
                      .map((reservation) => FastlaneWidget(
                          child: FastlaneDashboardReservationEntry(
                              callback: widget.callback,
                              reservation: reservation,
                              vehicle: widget.vehicles
                                  .where((vehicle) =>
                                      vehicle.id == reservation.vehicle)
                                  .single,
                              grid: true)))
                      .toList())
              : const FastlaneDashboardReservationsNoEntry(),
        ),
      ],
    );
  }
}

/// The `FastlaneDashboardReservationsNoEntries` class is a widget that displays a
/// message and an icon when there are no reservations entries.
class FastlaneDashboardReservationsNoEntries extends StatelessWidget {
  const FastlaneDashboardReservationsNoEntries({Key? key, required this.text})
      : super(key: key);

  final String text;

  @override
  Widget build(BuildContext context) {
    return FastlaneDashboardMainContainer(
        title: "",
        widgets: [
          FastlaneDashboardCard(
              padding: BIG_PADDING,
              height: DEFAULT_CARD_HEIGHT,
              width: DEFAULT_FULLSCREEN_CARD_WIDTH,
              navigateToWidget: FastlaneDashboardAllCars(
                  vehicles: VehicleData.instance.getVehicles()),
              crossAxisAlignment: CrossAxisAlignment.center,
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Text(text),
                const SizedBox(
                  height: 5,
                ),
                const Icon(Icons.bookmark_add_outlined,
                    size: 40, color: Colors.black)
              ]),
        ],
        wrap: true);
  }
}

/// The `FastlaneDashboardReservationsNoEntry` class is a stateless widget that
/// displays a card with the text "Bisher keine Buchung" (No bookings yet) in the
/// center.
class FastlaneDashboardReservationsNoEntry extends StatelessWidget {
  const FastlaneDashboardReservationsNoEntry({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return const FastlaneDashboardCard(
        padding: BIG_PADDING,
        height: DEFAULT_CARD_HEIGHT,
        width: DEFAULT_CARD_WIDTH,
        crossAxisAlignment: CrossAxisAlignment.center,
        mainAxisAlignment: MainAxisAlignment.center,
        children: [Text("Bisher keine Buchung")]);
  }
}

/// The `FastlaneDashboardNextReservations` class is a stateful widget that displays
/// the next reservation and vehicle information on a dashboard.
class FastlaneDashboardNextReservations extends StatefulWidget {
  const FastlaneDashboardNextReservations(
      {super.key, required this.reservation, required this.vehicle});

  final Reservation reservation;
  final Vehicle vehicle;

  @override
  State<FastlaneDashboardNextReservations> createState() =>
      _FastlaneDashboardNextReservationsState();
}

/// The `_FastlaneDashboardNextReservationsState` class builds a widget that
/// displays information about a vehicle reservation and includes a progress
/// indicator.
class _FastlaneDashboardNextReservationsState
    extends State<FastlaneDashboardNextReservations> {
  @override
  Widget build(BuildContext context) {
    double calculateProgressPercentage(
        DateTime startDateTime, DateTime endDateTime) {
      DateTime now = DateTime.now();

      // Vergangene Zeit in Sekunden
      double elapsedSeconds =
          now.difference(startDateTime).inSeconds.toDouble();

      // Gesamte Zeitdauer in Sekunden
      double totalSeconds =
          endDateTime.difference(startDateTime).inSeconds.toDouble();

      // Berechne den Prozentsatz der vergangenen Zeit
      double progressPercentage = (elapsedSeconds / totalSeconds);

      return progressPercentage;
    }

    double percentValue = calculateProgressPercentage(
        widget.reservation.startDateTime, widget.reservation.endDateTime);

    return FastlaneDashboardCard(
      padding: BIG_PADDING,
      height: DEFAULT_CARD_HEIGHT * 1.5,
      width: DEFAULT_CARD_WIDTH,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          widget.vehicle.configuration.category,
          style: const TextStyle(color: LESSER_BLUE, fontSize: 16),
        ),
        Text(
            "${widget.vehicle.vehicleModel.vehicleBrand.brandName} - ${widget.vehicle.vehicleModel.modelName} ",
            style: const TextStyle(fontSize: 32)),
        Container(
          margin: const EdgeInsets.only(left: 50),
          child: Image.network(
            widget.vehicle.vehicleModel.linkToPicture,
            width: 200,
            height: 200,
          ),
        ),
        Wrap(
          direction: Axis.horizontal,
          children: [
            FastlaneDashboardActualReservationData(
              data: widget.vehicle.configuration.fuel,
              icon: Icons.local_gas_station_rounded,
            ),
            FastlaneDashboardActualReservationData(
              data: widget.vehicle.configuration.transmission,
              icon: Icons.candlestick_chart_outlined,
            ),
            if (widget.vehicle.configuration.ac)
              const FastlaneDashboardActualReservationData(
                data: "Klimaanlage",
                icon: Icons.ac_unit_outlined,
              ),
            if (widget.vehicle.configuration.drivingAssistent)
              const FastlaneDashboardActualReservationData(
                data: "Fahrassistent",
                icon: Icons.arrow_circle_up,
              ),
            if (widget.vehicle.configuration.navigation)
              const FastlaneDashboardActualReservationData(
                data: "Navigation",
                icon: Icons.location_on_outlined,
              ),
            if (widget.vehicle.configuration.cruiseControl)
              const FastlaneDashboardActualReservationData(
                data: "Tempomat",
                icon: Icons.speed_outlined,
              ),
          ],
        ),
        const Spacer(),
        Padding(
          padding: const EdgeInsets.all(5.0),
          child: Row(
            children: [
              const Icon(Icons.hourglass_top_outlined,
                  size: 20, color: LESSER_BLUE),
              const SizedBox(
                width: 5,
              ),
              SizedBox(
                  width: DEFAULT_CARD_WIDTH * 0.7,
                  child: LinearProgressIndicator(
                    color: GRAY_BLUEISH,
                    valueColor:
                        const AlwaysStoppedAnimation<Color>(LESSER_BLUE),
                    backgroundColor: GRAY_BLUEISH,
                    value: percentValue,
                  )),
              const SizedBox(
                width: 5,
              ),
              const Icon(Icons.hourglass_bottom_outlined,
                  size: 20, color: GRAY_BLUEISH),
            ],
          ),
        )
      ],
    );
  }
}
