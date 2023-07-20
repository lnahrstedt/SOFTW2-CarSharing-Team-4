import 'package:fastlane/data/vehicleData.dart';
import 'package:fastlane/widget/dashboard/cars/allCarsDashboard.dart';
import 'package:fastlane/widget/dashboard/dashboardScaffold.dart';
import 'package:fastlane/widget/dashboard/home/homeDashboard.dart';
import 'package:fastlane/widget/dashboard/profile/profileDashboard.dart';
import 'package:fastlane/widget/dashboard/reservations/reservationsDashboard.dart';
import 'package:flutter/material.dart';

import '../mapPage/mapPage.dart';

/// The `FastlaneDashboardNavigation` class is a widget that builds a navigation bar
/// with tabs for different sections of a dashboard.
class FastlaneDashboardNavigation extends StatelessWidget {
  final GlobalKey<FastlaneDashboardHomeState> _fastlaneHome = GlobalKey();
  final GlobalKey<FastlaneDashboardReservationsState> _fastlaneReservations =
      GlobalKey();
  final GlobalKey<FastlaneDashboardAllCarsState> _fastlaneAllCars = GlobalKey();
  final GlobalKey<FastlaneDashboardAllCarsState> _fastlaneMap = GlobalKey();
  final GlobalKey<FastlaneDashboardProfileState> _fastlaneProfile = GlobalKey();

  final _tab1navigatorKey = GlobalKey<NavigatorState>();
  final _tab2navigatorKey = GlobalKey<NavigatorState>();
  final _tab3navigatorKey = GlobalKey<NavigatorState>();
  final _tab4navigatorKey = GlobalKey<NavigatorState>();
  final _tab5navigatorKey = GlobalKey<NavigatorState>();

  @override
  Widget build(BuildContext context) {
    return FastlaneDashboardScaffold(items: [
      FastlaneNavigationTabItem(_fastlaneHome,
          tab: FastlaneDashboardHome(
            key: _fastlaneHome,
          ),
          title: "Home",
          icon: Icons.home_filled,
          navigatorkey: _tab1navigatorKey),
      FastlaneNavigationTabItem(_fastlaneReservations,
          tab: FastlaneDashboardReservations(key: _fastlaneReservations),
          title: "Buchungen",
          icon: Icons.collections_outlined,
          navigatorkey: _tab2navigatorKey),
      FastlaneNavigationTabItem(_fastlaneAllCars,
          tab: FastlaneDashboardAllCars(
            key: _fastlaneAllCars,
            vehicles: VehicleData.instance.getVehicles(),
          ),
          title: "Autos",
          icon: Icons.drive_eta_rounded,
          navigatorkey: _tab3navigatorKey),
      FastlaneNavigationTabItem(_fastlaneMap,
          tab: FastlaneDashboardMap(
            key: _fastlaneMap,
            vehicles: VehicleData.instance.getVehicles(),
          ),
          title: "Karte",
          icon: Icons.map,
          navigatorkey: _tab4navigatorKey),
      FastlaneNavigationTabItem(_fastlaneProfile,
          tab: FastlaneDashboardProfile(
            key: _fastlaneProfile,
          ),
          title: "Profil",
          icon: Icons.person,
          navigatorkey: _tab5navigatorKey)
    ]);
  }
}
