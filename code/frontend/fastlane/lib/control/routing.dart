import 'package:fastlane/data/accountData.dart';
import 'package:fastlane/widget/dashboard/navigation.dart';
import 'package:fastlane/widget/homePage/home_page.dart';
import 'package:flutter/material.dart';
import 'package:universal_html/html.dart' as html;

/// The `FastlaneRouterDelegate` class is a Dart class that implements the `RouterDelegate` interface
/// and handles the routing logic for a Flutter application.
class FastlaneRouterDelegate extends RouterDelegate<RouteInformation>
    with ChangeNotifier, PopNavigatorRouterDelegateMixin<RouteInformation> {
  @override
  GlobalKey<NavigatorState> navigatorKey = GlobalKey<NavigatorState>();

  String _currentPath = '/';

  @override
  RouteInformation get currentConfiguration =>
      RouteInformation(location: _currentPath);

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
        future: AccountData.instance.checkUserIsLogged(),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.done) {
            if (_currentPath == '/home' && !AccountData.instance.isLoggedIn()) {
              html.window.history.pushState({}, '', '/');
            }

            if (_currentPath != '/' && _currentPath != '/home') {
              html.window.history.pushState({}, '', '/');
            }

            return Navigator(
              key: navigatorKey,
              onPopPage: (route, result) {
                if (!route.didPop(result)) {
                  return false;
                }
                _currentPath = '/';
                notifyListeners();
                return true;
              },
              pages: [
                if (_currentPath == '/')
                  const MaterialPage(child: FastlaneHomePage())
                else if (_currentPath == '/home' &&
                    AccountData.instance.isLoggedIn())
                  MaterialPage(child: FastlaneDashboardNavigation())
                else
                  const MaterialPage(child: FastlaneHomePage())
              ],
            );
          } else {
            return Container();
          }
        });
  }

  @override
  Future<void> setNewRoutePath(RouteInformation configuration) async {
    _currentPath = configuration.location!;
    notifyListeners();
  }
}

/// The `FastlaneRouteInformationParser` class is a Dart class that extends the `RouteInformationParser`
/// class and is used to parse and restore route information.
class FastlaneRouteInformationParser
    extends RouteInformationParser<RouteInformation> {
  @override
  Future<RouteInformation> parseRouteInformation(
      RouteInformation routeInformation) async {
    return RouteInformation(location: routeInformation.location);
  }

  @override
  RouteInformation? restoreRouteInformation(RouteInformation configuration) {
    return configuration;
  }
}
