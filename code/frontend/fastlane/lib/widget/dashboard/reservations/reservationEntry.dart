import 'package:fastlane/constants/colors.dart';
import 'package:fastlane/constants/numbers.dart';
import 'package:fastlane/data/reservationData.dart';
import 'package:fastlane/model/reservation.dart';
import 'package:fastlane/widget/helper/mainCard.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

import '../../../constants/padding.dart';
import '../../../model/vehicle.dart';
import '../../vehiclePage/vehiclePage.dart';

/// The `FastlaneDashboardReservationEntry` class is a stateful widget that
/// represents an entry in a dashboard for a reservation, with various properties
/// and a callback function.
class FastlaneDashboardReservationEntry extends StatefulWidget {
  const FastlaneDashboardReservationEntry(
      {Key? key,
      required this.reservation,
      this.navigateToWidget,
      required this.grid,
      required this.vehicle,
      required this.callback})
      : super(key: key);

  final Reservation reservation;
  final Function callback;
  final Vehicle vehicle;
  final bool grid;
  final Widget? navigateToWidget;

  @override
  State<FastlaneDashboardReservationEntry> createState() =>
      _FastlaneDashboardReservationEntryState();
}

/// The `_FastlaneDashboardReservationEntryState` class is a stateful widget that
/// builds a card entry for a reservation in a dashboard, displaying various
/// reservation details and allowing actions such as deleting or paying for the
/// reservation.
class _FastlaneDashboardReservationEntryState
    extends State<FastlaneDashboardReservationEntry> {
  bool isPayingOnHover = false;
  bool isDeletingOnHover = false;

  void setPayingHover(val) {
    setState(() {
      isPayingOnHover = val;
    });
  }

  var priceFormat = NumberFormat.currency(locale: "de", symbol: "€");

  FastlaneDashboardCard buildGridEntry() {
    return FastlaneDashboardCard(
        padding: const EdgeInsets.symmetric(horizontal: 12.5, vertical: 15),
        height: DEFAULT_CARD_HEIGHT * 0.9,
        width: DEFAULT_CARD_WIDTH,
        navigateToWidget: FastlaneDashboardVehiclePage(vehicle: widget.vehicle),
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Center(
            child: SizedBox(
                width: 270,
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                        "${widget.vehicle.vehicleModel.vehicleBrand.brandName} ${widget.vehicle.vehicleModel.modelName}",
                        style: const TextStyle(
                            fontSize: 18, fontWeight: FontWeight.w600)),
                    const SizedBox(
                      height: 12.5,
                    ),
                    Row(
                      children: [
                        Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            FastlaneDashboardReservationData(
                                label: "Startdatum",
                                data: DateFormat('dd.MM.yyyy')
                                    .format(widget.reservation.startDateTime)),
                            const SizedBox(
                              height: 7.5,
                            ),
                            FastlaneDashboardReservationData(
                                label: "Zeit",
                                data: DateFormat('kk:mm')
                                    .format(widget.reservation.startDateTime))
                          ],
                        ),
                        const SizedBox(
                          width: 25,
                        ),
                        Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            FastlaneDashboardReservationData(
                                label: "Enddatum",
                                data: DateFormat('dd.MM.yyyy')
                                    .format(widget.reservation.endDateTime)),
                            const SizedBox(
                              height: 7.5,
                            ),
                            FastlaneDashboardReservationData(
                                label: "Zeit",
                                data: DateFormat('kk:mm')
                                    .format(widget.reservation.endDateTime))
                          ],
                        )
                      ],
                    ),
                    const SizedBox(
                      height: 20,
                    ),
                    Text(
                      "${priceFormat.format(widget.reservation.price)}",
                      style: const TextStyle(
                          color: Colors.black,
                          fontSize: 16,
                          fontWeight: FontWeight.w600),
                    ),
                    const SizedBox(
                      height: 5,
                    ),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        createReservationState(),
                        Row(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            if (widget.reservation.startDateTime
                                    .isAfter(DateTime.now()) &&
                                widget.reservation.reservationState !=
                                    "CANCELED")
                              InkWell(
                                  onTap: () async {
                                    await ReservationData.instance
                                        .deleteReservationById(
                                            widget.reservation.id);
                                    widget.callback.call();
                                  },
                                  onHover: (val) => setDeletingHover(val),
                                  child: Icon(
                                    Icons.delete_outline_rounded,
                                    color: isDeletingOnHover
                                        ? Colors.black
                                        : GRAY_BLUEISH,
                                  )),
                            if (widget.reservation.reservationState == "UNPAID")
                              InkWell(
                                  onTap: () async {
                                    await ReservationData.instance
                                        .setReservationStateOnPaid(
                                            widget.reservation.id);
                                    widget.callback.call();
                                  },
                                  onHover: (val) => setPayingHover(val),
                                  child: Icon(
                                    Icons.payment,
                                    color: isPayingOnHover
                                        ? Colors.black
                                        : GRAY_BLUEISH,
                                  ))
                          ],
                        ),
                      ],
                    )
                  ],
                )),
          ),
        ]);
  }

  void setDeletingHover(val) {
    setState(() {
      isDeletingOnHover = val;
    });
  }

  FastlaneDashboardCard buildDefaultEntry() {
    return FastlaneDashboardCard(
        padding: const EdgeInsets.symmetric(horizontal: 12.5, vertical: 15),
        height: DEFAULT_CARD_HEIGHT,
        width: DEFAULT_CARD_WIDTH,
        navigateToWidget: FastlaneDashboardVehiclePage(vehicle: widget.vehicle),
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Center(
            child: SizedBox(
              width: 270,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                      "${widget.vehicle.vehicleModel.vehicleBrand.brandName} ${widget.vehicle.vehicleModel.modelName} ${widget.vehicle.constructionYear}"),
                  const SizedBox(
                    height: 10,
                  ),
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      FastlaneDashboardReservationData(
                          label: "Startdatum",
                          data: DateFormat('dd-MM-yyyy')
                              .format(widget.reservation.startDateTime)),
                      FastlaneDashboardReservationData(
                          label: "Zeit",
                          data: DateFormat('kk:mm')
                              .format(widget.reservation.startDateTime))
                    ],
                  ),
                  const SizedBox(
                    height: 10,
                  ),
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      FastlaneDashboardReservationData(
                          label: "Enddatum",
                          data: DateFormat('dd-MM-yyyy')
                              .format(widget.reservation.endDateTime)),
                      FastlaneDashboardReservationData(
                          label: "Zeit",
                          data: DateFormat('kk:mm')
                              .format(widget.reservation.endDateTime))
                    ],
                  )
                ],
              ),
            ),
          ),
        ]);
  }

  FittedBox createReservationState() {
    if (widget.reservation.reservationState == "PAID") {
      return FittedBox(
        child: Container(
          padding: SMALL_PADDING,
          decoration: const BoxDecoration(
              color: FLOJC_GREEN_ACCENT,
              borderRadius: BorderRadius.all(Radius.circular(20))),
          child: const Text(
            "Bezahlt",
            style: TextStyle(
                fontSize: 12, fontWeight: FontWeight.w400, color: FLOJC_GREEN),
          ),
        ),
      );
    }
    if (widget.reservation.reservationState == "UNPAID") {
      return FittedBox(
        child: Container(
          padding: SMALL_PADDING,
          decoration: const BoxDecoration(
              color: FLOJC_RED_ACCENT,
              borderRadius: BorderRadius.all(Radius.circular(20))),
          child: const Text(
            "Unbezahlt",
            style: TextStyle(
                fontSize: 12, fontWeight: FontWeight.w400, color: FLOJC_RED),
          ),
        ),
      );
    }
    return const FittedBox();
  }

  @override
  Widget build(BuildContext context) {
    return widget.grid ? buildGridEntry() : buildDefaultEntry();
  }
}

class FastlaneDashboardReservationData extends StatelessWidget {
  const FastlaneDashboardReservationData(
      {Key? key, required this.label, required this.data})
      : super(key: key);

  final String label;
  final String data;

  @override
  Widget build(BuildContext context) {
    return FittedBox(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            label,
            style: const TextStyle(
                color: INBETWEEN_GRAY,
                fontSize: 13,
                fontWeight: FontWeight.w400),
          ),
          const SizedBox(
            height: 3,
          ),
          Text(
            data,
            style: const TextStyle(fontSize: 14, fontWeight: FontWeight.w500),
          )
        ],
      ),
    );
  }
}
