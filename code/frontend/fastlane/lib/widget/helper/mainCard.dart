import 'package:fastlane/constants/border.dart';
import 'package:fastlane/constants/text.dart';
import 'package:flutter/material.dart';

import '../../constants/margin.dart';
import '../../constants/padding.dart';
import '../../constants/shadow.dart';
import '../../control/NavigationController.dart';

/// The `FastlaneDashboardCard` class is a stateful widget that represents a card in
/// a dashboard with customizable properties such as title, size, padding,
/// alignment, and child widgets.
class FastlaneDashboardCard extends StatefulWidget {
  const FastlaneDashboardCard(
      {super.key,
      this.title,
      required this.children,
      this.navigateToWidget,
      required this.height,
      required this.width,
      required this.padding,
      required this.crossAxisAlignment,
      this.mainAxisAlignment = MainAxisAlignment.start});

  final String? title;
  final Widget? navigateToWidget;
  final double height;
  final double width;
  final EdgeInsets padding;
  final CrossAxisAlignment crossAxisAlignment;
  final MainAxisAlignment mainAxisAlignment;
  final List<Widget> children;

  @override
  State<FastlaneDashboardCard> createState() => _FastlaneDashboardCardState();
}

/// The `_FastlaneDashboardCardState` class is a stateful widget that builds a card
/// with different layouts based on the presence of a title.
class _FastlaneDashboardCardState extends State<FastlaneDashboardCard> {
  bool isHovering = false;

  void setHover(hover) {
    setState(() {
      isHovering = hover;
    });
  }

  void navigateToWidget(Widget widget) {
    Navigator.push(
        context,
        PageRouteBuilder(
          pageBuilder: (context, animation1, animation2) =>
              widget,
          transitionDuration: Duration.zero,
          reverseTransitionDuration: Duration.zero,
        ));
  }

  Container buildDefaultCard() {
    return Container(
      margin: MIDDLE_MARGIN,
      child: SizedBox(
        height: widget.height,
        width: widget.width,
        child: InkWell(
          onTap: (widget.navigateToWidget != null)
              ? () {
                  navigateToWidget(widget.navigateToWidget!);
                }
              : null,
          onHover: (hover) {
            setHover(hover);
          },
          child: Container(
            padding: widget.padding,
            decoration: BoxDecoration(
                borderRadius: CARD_BORDERRADIUS,
                color: Colors.white,
                boxShadow: [
                  isHovering && widget.navigateToWidget != null
                      ? CARD_SHADOW_HOVER
                      : CARD_SHADOW_NO_HOVER
                ]),
            child: Column(
                crossAxisAlignment: widget.crossAxisAlignment,
                children: [
                  buildCardTitle(widget.title!),
                  Column(
                    mainAxisAlignment: widget.mainAxisAlignment,
                    children: widget.children,
                  ),
                ]),
          ),
        ),
      ),
    );
  }

  Container buildGridCard() {
    return Container(
      margin: MIDDLE_MARGIN,
      child: SizedBox(
        height: widget.height,
        width: widget.width,
        child: InkWell(
          onTap: (widget.navigateToWidget != null)
              ? () {
                  navigateToWidget(widget.navigateToWidget!);
                }
              : null,
          onHover: (hover) {
            setHover(hover);
          },
          child: Container(
            padding: widget.padding,
            decoration: BoxDecoration(
                borderRadius: CARD_BORDERRADIUS,
                color: Colors.white,
                boxShadow: [
                  isHovering && widget.navigateToWidget != null
                      ? CARD_SHADOW_HOVER
                      : CARD_SHADOW_NO_HOVER
                ]),
            child: Column(
              crossAxisAlignment: widget.crossAxisAlignment,
              mainAxisAlignment: widget.mainAxisAlignment,
              children: widget.children,
            ),
          ),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return widget.title == null ? buildGridCard() : buildDefaultCard();
  }
}

/// The `FastlaneDashboardExpandedCard` class is a stateful widget that represents
/// an expanded card in a dashboard, with a title, optional navigation to another
/// widget, and a list of child widgets.
class FastlaneDashboardExpandedCard extends StatefulWidget {
  const FastlaneDashboardExpandedCard(
      {Key? key,
      required this.title,
      this.navigateToWidget,
      required this.children})
      : super(key: key);

  final String title;
  final Widget? navigateToWidget;
  final List<Widget> children;

  @override
  State<FastlaneDashboardExpandedCard> createState() =>
      _FastlaneDashboardExpandedCardState();
}

/// The `_FastlaneDashboardExpandedCardState` class is a stateful widget that
/// represents an expanded card in a fastlane dashboard, with hover functionality
/// and the ability to navigate to another widget.
class _FastlaneDashboardExpandedCardState
    extends State<FastlaneDashboardExpandedCard> {
  bool isHovering = false;

  void setHover(hover) {
    setState(() {
      isHovering = hover;
    });
  }

  void navigateToWidget(Widget widget) {
    FastlaneNavigationController.push(widget, context);
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: MIDDLE_MARGIN,
      child: InkWell(
        onTap: (widget.navigateToWidget != null)
            ? () {
                navigateToWidget(widget.navigateToWidget!);
              }
            : null,
        onHover: (hover) {
          setHover(hover);
        },
        child: Container(
          padding: BIG_PADDING,
          decoration: BoxDecoration(
              borderRadius: CARD_BORDERRADIUS,
              color: Colors.white,
              boxShadow: [
                isHovering ? CARD_SHADOW_HOVER : CARD_SHADOW_NO_HOVER
              ]),
          child:
              Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
            buildCardTitle(widget.title),
            Column(
              children: widget.children,
            ),
          ]),
        ),
      ),
    );
  }
}

/// The `FastlaneDashboardFittedCard` class is a stateful widget that represents a
/// card with a title and a list of children widgets, allowing customization of the
/// main and cross axis alignment, padding, and title.
class FastlaneDashboardFittedCard extends StatefulWidget {
  const FastlaneDashboardFittedCard(
      {super.key,
      required this.children,
      this.mainAxisAlignment,
      this.crossAxisAlignment,
      this.padding,
      this.title});

  final String? title;
  final List<Widget> children;
  final MainAxisAlignment? mainAxisAlignment;
  final CrossAxisAlignment? crossAxisAlignment;
  final EdgeInsets? padding;

  @override
  State<FastlaneDashboardFittedCard> createState() =>
      _FastlaneDashboardFittedCardState();
}

/// The `_FastlaneDashboardFittedCardState` class is a stateful widget that builds a
/// grid card with a fitted box and handles the rendering of the card's title and
/// children.
class _FastlaneDashboardFittedCardState
    extends State<FastlaneDashboardFittedCard> {
  Container buildGridCard() {
    return Container(
      margin: MIDDLE_MARGIN,
      child: FittedBox(
        child: InkWell(
          child: Container(
            padding: widget.padding,
            decoration: const BoxDecoration(
                borderRadius: CARD_BORDERRADIUS,
                color: Colors.white,
                boxShadow: [CARD_SHADOW_NO_HOVER]),
            child: Column(
              crossAxisAlignment:
                  widget.crossAxisAlignment ?? CrossAxisAlignment.start,
              mainAxisAlignment:
                  widget.mainAxisAlignment ?? MainAxisAlignment.center,
              children: [
                if (widget.title != null) buildCardTitle(widget.title!),
                Column(
                  children: widget.children,
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return buildGridCard();
  }
}
