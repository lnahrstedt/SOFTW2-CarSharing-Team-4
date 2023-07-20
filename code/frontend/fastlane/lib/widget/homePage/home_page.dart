import 'package:fastlane/constants/colors.dart';
import 'package:fastlane/widget/homePage/car_brands.dart';
import 'package:fastlane/widget/homePage/compare_tariffs.dart';
import 'package:fastlane/widget/homePage/disclaimer.dart';
import 'package:fastlane/widget/homePage/locations.dart';
import 'package:fastlane/widget/homePage/tariffs.dart';
import 'package:flutter/material.dart';
import 'package:scrollable_positioned_list/scrollable_positioned_list.dart';

import '../forms/login/login_form.dart';
import 'main_title.dart';

/// The FastlaneHomePage class is a StatefulWidget in Dart that represents the home
/// page of a Fastlane application.
class FastlaneHomePage extends StatefulWidget {
  const FastlaneHomePage({super.key});

  @override
  State<StatefulWidget> createState() => _FastlaneHomePageState();
}

/// The `_FastlaneHomePageState` class is a stateful widget that builds a scaffold
/// with a bottom navigation bar and a scrollable list of items.
class _FastlaneHomePageState extends State<FastlaneHomePage> {
  final _items = [
    const SizedBox(height: 50),
    const MainTitle(),
    const SizedBox(height: 50),
    const LocationInfo(),
    const SizedBox(height: 50),
    const CarBrands(),
    const SizedBox(height: 50),
    const Tariffs(),
    const SizedBox(height: 50),
    const CompareTariffs(),
    const SizedBox(height: 50),
    const Disclaimer(),
    const SizedBox(height: 50),
  ];

  final _scrollController = ItemScrollController();
  int _selectedIndex = 0;

  /// The function `_onItemTapped` is used to handle the tap event on a bottom
  /// navigation bar and perform different actions based on the selected index.
  ///
  /// Args:
  ///   index (int): The `index` parameter represents the index of the item that was
  /// tapped. It is used to determine which action to perform based on the selected
  /// item.
  ///
  /// Returns:
  ///   The code snippet does not explicitly return anything. It is a void function,
  /// which means it does not have a return type.
  void _onItemTapped(int index) {
    switch (index) {
      case 0:
        _scrollController.scrollTo(
            index: 3, duration: const Duration(milliseconds: 500));
        break;
      case 1:
        _scrollController.scrollTo(
            index: 5, duration: const Duration(milliseconds: 500));
        break;
      case 2:
        _scrollController.scrollTo(
            index: 7, duration: const Duration(milliseconds: 500));
        break;
      case 3:
        _scrollController.scrollTo(
            index: 9, duration: const Duration(milliseconds: 500));
        break;
      case 4:
        showDialog(
            context: context,
            builder: (BuildContext context) {
              return const LoginForm();
            });
        break;
      default:
        break;
    }
    setState(() {
      _selectedIndex = index;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        bottomNavigationBar: BottomNavigationBar(
          type: BottomNavigationBarType.fixed,
          unselectedItemColor: Colors.black,
          selectedItemColor: LESSER_BLUE,
          unselectedLabelStyle: const TextStyle(color: Colors.black),
          showUnselectedLabels: true,
          currentIndex: _selectedIndex,
          onTap: _onItemTapped,
          backgroundColor: Colors.white,
          items: const [
            BottomNavigationBarItem(
                icon: Icon(Icons.location_on_outlined), label: 'Standorte'),
            BottomNavigationBarItem(
                icon: Icon(Icons.directions_car), label: 'Marken'),
            BottomNavigationBarItem(
                icon: Icon(Icons.price_change_outlined), label: 'Tarife'),
            BottomNavigationBarItem(
                icon: Icon(Icons.compare), label: 'Vergleich'),
            BottomNavigationBarItem(
                icon: Icon(Icons.login_rounded), label: 'Login'),
          ],
        ),
        backgroundColor: Colors.white,
        body: ScrollablePositionedList.builder(
            itemScrollController: _scrollController,
            itemCount: _items.length,
            itemBuilder: (context, index) {
              return _items[index];
            }));
  }
}
