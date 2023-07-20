import 'package:fastlane/constants/border.dart';
import 'package:fastlane/constants/shadow.dart';
import 'package:fastlane/control/mediaChecker.dart';
import 'package:fastlane/data/accountData.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:google_nav_bar/google_nav_bar.dart';

import '../../constants/colors.dart';

/// The `FastlaneDashboardScaffold` class is a stateful widget that represents a
/// scaffold for a dashboard with navigation tabs.
class FastlaneDashboardScaffold extends StatefulWidget {
  const FastlaneDashboardScaffold({Key? key, required this.items})
      : super(key: key);

  final List<FastlaneNavigationTabItem> items;

  @override
  State<FastlaneDashboardScaffold> createState() =>
      _FastlaneDashboardScaffoldState();
}

/// The `_FastlaneDashboardScaffoldState` class is responsible for managing the
/// state and building the UI for the Fastlane Dashboard Scaffold, which includes a
/// side menu, bottom navigation bar, and app bar.
class _FastlaneDashboardScaffoldState extends State<FastlaneDashboardScaffold> {
  int _selectedDestination = 0;

  void selectDestination(int index) {
    setState(() {
      _selectedDestination = index;
    });
    widget.items[index].widgetKey.currentState?.setState(() {});
  }

  Container buildTabItem(String name, IconData icon, int index) {
    return Container(
      margin: const EdgeInsets.all(10),
      decoration: BoxDecoration(
          gradient: _selectedDestination == index
              ? LinearGradient(
                  colors: [
                    Colors.white,
                    FLOJC_BLUE_ACCENT,
                    FLOJC_BLUE.withOpacity(0.5)
                  ],
                  stops: const [0.1, 0.5, 1],
                  begin: Alignment.centerLeft,
                  end: Alignment.centerRight,
                )
              : null,
          borderRadius: MediaController.isMediumScreen(context)
              ? const BorderRadius.all(Radius.circular(10))
              : const BorderRadius.only(
                  topRight: Radius.circular(15),
                  bottomRight: Radius.circular(15))),
      child: ListTile(
          leading: Icon(
            icon,
            color: INBETWEEN_GRAY,
          ),
          title: MediaController.isMediumScreen(context)
              ? null
              : Text(
                  name,
                  style: const TextStyle(color: INBETWEEN_GRAY),
                ),
          selected: _selectedDestination == index,
          onTap: () {
            if (index == _selectedDestination) {
              /// if you want to pop the current tab to its root then use
              widget.items[index].navigatorkey?.currentState
                  ?.popUntil((route) => route.isFirst);
              widget.items[index].widgetKey.currentState?.setState(() {});

              /// if you want to pop the current tab to its last page
              /// then use
              // widget.items[index].navigatorkey?.currentState?.pop();
            } else {
              setState(() {
                _selectedDestination = index;
              });
              widget.items[index].widgetKey.currentState?.setState(() {});
            }
          },
          shape: RoundedRectangleBorder(
              borderRadius: MediaController.isMediumScreen(context)
                  ? const BorderRadius.all(Radius.circular(12))
                  : const BorderRadius.only(
                      topRight: Radius.circular(12),
                      bottomRight: Radius.circular(12)))),
    );
  }

  Container buildProfile(String firstName, String lastName) {
    return buildTabItem(
        "$firstName $lastName", Icons.person, widget.items.length - 1);
  }

  Column buildItems() {
    return Column(
      children: widget.items
          .map((item) =>
              buildTabItem(item.title, item.icon, widget.items.indexOf(item)))
          .toList(),
    );
  }

  Drawer buildSideMenu() {
    return Drawer(
      width: MediaController.isMediumScreen(context) ? 80 : 200,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(15)),
      backgroundColor: Colors.white,
      child: Container(
          margin: const EdgeInsets.only(top: 10, bottom: 10),
          child: buildItems()),
    );
  }

  @override
  Widget build(BuildContext context) {
    var stack = IndexedStack(
      index: _selectedDestination,
      children: widget.items
          .map((page) => Navigator(
                /// Each tab is wrapped in a Navigator so that navigation in
                /// one tab can be independent of the other tabs
                key: page.navigatorkey,
                onGenerateInitialRoutes: (navigator, initialRoute) {
                  return [MaterialPageRoute(builder: (context) => page.tab)];
                },
              ))
          .toList(),
    );

    return WillPopScope(
      onWillPop: () async {
        /// Check if current tab can be popped
        if (widget.items[_selectedDestination].navigatorkey?.currentState
                ?.canPop() ??
            false) {
          widget.items[_selectedDestination].navigatorkey?.currentState?.pop();
          return false;
        } else {
          // if current tab can't be popped then use the root navigator
          return true;
        }
      },
      child: Scaffold(
          body: !MediaController.isSmallScreen(context)
              ? Center(
                  child: SafeArea(
                    child: Row(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        buildSideMenu(),
                        Expanded(
                          flex: 5,
                          child: stack,
                        )
                      ],
                    ),
                  ),
                )
              : stack,
          bottomNavigationBar: MediaController.isSmallScreen(context)
              ? Container(
                  padding:
                      const EdgeInsets.symmetric(horizontal: 10, vertical: 7.5),
                  decoration: const BoxDecoration(
                      color: Colors.white,
                      boxShadow: [
                        BoxShadow(
                          offset: Offset(0, 0),
                          spreadRadius: 0,
                          blurRadius: 10,
                          color: DROPSHADOW_GRAY,
                        ),
                      ],
                      borderRadius: BorderRadius.all(Radius.circular(12))),
                  child: GNav(
                    onTabChange: (index) {
                      if (index == _selectedDestination) {
                        /// if you want to pop the current tab to its root then use
                        widget.items[index].navigatorkey?.currentState
                            ?.popUntil((route) => route.isFirst);
                        widget.items[index].widgetKey.currentState
                            ?.setState(() {});

                        /// if you want to pop the current tab to its last page
                        /// then use
                        // widget.items[index].navigatorkey?.currentState?.pop();
                      } else {
                        setState(() {
                          _selectedDestination = index;
                        });
                        widget.items[index].widgetKey.currentState
                            ?.setState(() {});
                      }
                    },
                    tabBackgroundGradient: const LinearGradient(
                      colors: [FLOJC_BLUE_ACCENT, FLOJC_BLUE],
                      stops: [0, 1],
                      begin: Alignment.centerLeft,
                      end: Alignment.centerRight,
                    ),
                    padding: const EdgeInsets.all(15),
                    activeColor: Colors.black,
                    color: INBETWEEN_GRAY,
                    selectedIndex: _selectedDestination,
                    gap: 2,
                    tabs: widget.items
                        .map((item) => GButton(icon: item.icon, text: ""))
                        .toList(),
                  ),
                )
              : null,
          appBar: PreferredSize(
              preferredSize: const Size.fromHeight(75),
              child: Container(
                margin: const EdgeInsets.only(bottom: 15),
                child: Align(
                  alignment: Alignment.center,
                  child: Container(
                      alignment: Alignment.center,
                      decoration: const BoxDecoration(
                          color: Colors.white,
                          borderRadius: CARD_BORDERRADIUS,
                          boxShadow: [CARD_SHADOW_NO_HOVER]),
                      child: Container(
                        padding: const EdgeInsets.symmetric(horizontal: 20),
                        child: Center(
                          child: Row(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              InkWell(
                                onTap: () {
                                  setState(() {
                                    _selectedDestination = 0;
                                  });
                                  widget.items[0].widgetKey.currentState
                                      ?.setState(() {});
                                },
                                child: Padding(
                                  padding: const EdgeInsets.only(top: 8.0),
                                  child: SvgPicture.asset(
                                    "assets/fastlane.svg",
                                    width: 30,
                                    height: 30,
                                  ),
                                ),
                              ),
                              const Spacer(),
                              InkWell(
                                child: const CircleAvatar(
                                  backgroundImage:
                                      AssetImage('assets/user.png'),
                                  backgroundColor: FLOJC_BLUE_ACCENT,
                                ),
                                onTap: () {
                                  setState(() {
                                    _selectedDestination =
                                        widget.items.length - 1;
                                  });
                                },
                              ),
                              IconButton(
                                  onPressed: () {
                                    AccountData.instance.logoutAccount(context);
                                  },
                                  icon: const Icon(Icons.logout_rounded))
                            ],
                          ),
                        ),
                      )),
                ),
              ))),
    );
  }
}

/// The `FastlaneNavigationTabItem` class represents a navigation tab item with a
/// widget key, tab widget, navigator key, title, and icon.
class FastlaneNavigationTabItem {
  final GlobalKey widgetKey;
  final Widget tab;
  final GlobalKey<NavigatorState>? navigatorkey;
  final String title;
  final IconData icon;

  FastlaneNavigationTabItem(this.widgetKey,
      {required this.tab,
      this.navigatorkey,
      required this.title,
      required this.icon});
}
