import 'package:fastlane/constants/addresses.dart';
import 'package:fastlane/control/routing.dart';
import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:url_strategy/url_strategy.dart';

/// The main function sets the path URL strategy and runs the MyApp widget.
void main() async{
  setPathUrlStrategy();
  await dotenv.load(fileName: ".env");
  BACKEND_ADDRESS = dotenv.env['BACKEND_ADDRESS'] ?? "http://localhost:8080";
  runApp(Fastlane());
}

/// The Fastlane class is a StatelessWidget that sets up the routerDelegate and routeInformationParser
/// for the MaterialApp.router widget and determines the local host address based on the current
/// platform.
class Fastlane extends StatelessWidget {
  final routerDelegate = FastlaneRouterDelegate();
  final routeInformation = FastlaneRouteInformationParser();

  Fastlane({super.key});

  @override
  Widget build(BuildContext context) {

    return MaterialApp.router(
        debugShowCheckedModeBanner: false,
        title: 'Fastlane',
        routerDelegate: routerDelegate,
        routeInformationParser: routeInformation);
  }
}
