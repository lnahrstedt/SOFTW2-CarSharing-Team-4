import 'package:fastlane/data/driverData.dart';
import 'package:fastlane/data/vehicleData.dart';
import 'package:fastlane/widget/dashboard/cars/allCarsDashboard.dart';
import 'package:fastlane/widget/dashboard/home/actualReservation.dart';
import 'package:fastlane/widget/dashboard/home/map.dart';
import 'package:fastlane/widget/dashboard/home/memberSince.dart';
import 'package:fastlane/widget/dashboard/home/otherCars.dart';
import 'package:fastlane/widget/helper/mainCard.dart';
import 'package:fastlane/widget/helper/mainScrollable.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

import '../../../constants/numbers.dart';
import '../../../constants/padding.dart';
import '../../../data/reservationData.dart';
import '../../../model/reservation.dart';
import '../../../model/vehicle.dart';
import '../../helper/mainContainer.dart';
import '../../helper/widget.dart';

/// The `FastlaneDashboardHome` class is a stateful widget in Dart.
/// The `FastlaneDashboardHome` class is a stateful widget in Dart.
class FastlaneDashboardHome extends StatefulWidget {
  const FastlaneDashboardHome({
    Key? key,
  }) : super(key: key);

  @override
  State<FastlaneDashboardHome> createState() => FastlaneDashboardHomeState();
}

/// The `FastlaneDashboardHomeState` class is responsible for building the home
/// screen of a fastlane dashboard app, which includes displaying the user's nearest
/// reservation, other available cars, a map, and membership information.
class FastlaneDashboardHomeState extends State<FastlaneDashboardHome> {
  @override
  Widget build(BuildContext context) {
    if (DriverData.instance?.getDriver() == null) {
    } else {}

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
            List<Vehicle> vehicleList = snapshot.data[3];
            List<Reservation> reservationList = snapshot.data[2];

            List<Reservation> reservationsNotCanceled = reservationList
                .where((element) => element.reservationState != "CANCELED")
                .toList();

            Reservation? findNearestReservation(
                List<Reservation> reservations) {
              if (reservationsNotCanceled.isEmpty) return null;

              DateTime now = DateTime.now();

              Reservation nearestReservation = reservationsNotCanceled[0];
              for (int i = 1; i < reservationsNotCanceled.length; i++) {
                Duration differenceA =
                    nearestReservation.startDateTime.difference(now).abs();
                Duration differenceB = reservationsNotCanceled[i]
                    .startDateTime
                    .difference(now)
                    .abs();

                if (differenceB < differenceA) {
                  nearestReservation = reservationsNotCanceled[i];
                }
              }

              return nearestReservation;
            }

            Reservation? reservation;
            String? date;
            String? time;
            String? brand;
            List<Vehicle>? vehicleFoundList;
            Vehicle? vehicle;

            if (reservationsNotCanceled.isNotEmpty && vehicleList.isNotEmpty) {
              reservation = findNearestReservation(reservationList);
              date =
                  DateFormat("dd.MM.yyyy").format(reservation!.startDateTime);
              time = DateFormat.Hm().format(reservation.startDateTime);
              vehicleFoundList = vehicleList
                  .where((res) => res.id == reservation?.vehicle)
                  .toList();
              vehicle = vehicleFoundList.first;
              brand =
                  "${vehicle.vehicleModel.vehicleBrand.brandName} ${vehicle.vehicleModel.modelName}";
            }
            return FastlaneScrollable(
              direction: Axis.vertical,
              child: FastlaneDashboardMainContainer(
                title: "",
                wrap: true,
                widgets: [
                  FastlaneWidget(
                      child: reservationsNotCanceled.isNotEmpty
                          ? FastlaneDashboardHomeActualReservation(
                              vehicle: vehicle!, date: date!, time: time!)
                          : FastlaneDashboardCard(
                              padding: BIG_PADDING,
                              height: DEFAULT_CARD_HEIGHT,
                              width: DEFAULT_CARD_WIDTH,
                              navigateToWidget: FastlaneDashboardAllCars(
                                  vehicles: VehicleData.instance.getVehicles()),
                              crossAxisAlignment: CrossAxisAlignment.center,
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: const [
                                  Text("Füge eine Buchung hinzu"),
                                  SizedBox(
                                    height: 5,
                                  ),
                                  Icon(Icons.bookmark_add_outlined,
                                      size: 40, color: Colors.black)
                                ])),
                  FastlaneWidget(
                      child: vehicleList.isNotEmpty
                          ? FastlaneDashboardHomeOtherCars(
                              vehicles: vehicleList,
                            )
                          : const FastlaneDashboardCard(
                              padding: BIG_PADDING,
                              height: DEFAULT_CARD_HEIGHT,
                              width: DEFAULT_EXPANDED_CARD_WIDTH,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                  Text(
                                      "Leider haben wir zum jetzigen Zeitpunkt keine Fahrzeuge!")
                                ])),
                  const FastlaneWidget(child: FastlaneDashboardHomeMap()),
                  const FastlaneWidget(child: FastlaneDashboardMembership()),
                ],
              ),
            );
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
