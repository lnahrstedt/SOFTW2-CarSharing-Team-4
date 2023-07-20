import 'package:fastlane/widget/helper/platformHelper.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

import '../../constants/border.dart';
import '../../constants/colors.dart';
import '../../constants/padding.dart';

/// The TimeframePicker class is a StatefulWidget in Dart that allows users to
/// select a timeframe and provides the selected start and end times through a
/// callback function.
class TimeframePicker extends StatefulWidget {
  final void Function(DateTime, DateTime) getTimes;

  const TimeframePicker({super.key, required this.getTimes});

  @override
  State<StatefulWidget> createState() => _TimeframePickerState();
}

/// The `_TimeframePickerState` class is a stateful widget in Dart that allows users
/// to select a start and end date and time.
class _TimeframePickerState extends State<TimeframePicker> {
  DateTime? _startDate;
  DateTime? _endDate;
  TimeOfDay? _startTime;
  TimeOfDay? _endTime;
  String _stringStartDate = "Datum";
  String _stringEndDate = "Datum";
  String _stringStartTime = "Uhrzeit";
  String _stringEndTime = "Uhrzeit";

  @override
  initState() {
    super.initState();
  }

  void _sendTimes() {
    if (!(_startDate == null ||
        _startTime == null ||
        _endDate == null ||
        _endTime == null)) {
      DateTime startDate = _startDate!;
      startDate = startDate
          .add(Duration(hours: _startTime!.hour, minutes: _startTime!.minute));
      DateTime endDate = _endDate!;
      endDate = endDate
          .add(Duration(hours: _endTime!.hour, minutes: _endTime!.minute));
      widget.getTimes(startDate, endDate);
    }
  }

  Future<TimeOfDay?> _createTimePicker(
      BuildContext context, TimeOfDay? initialTime) async {
    return await showTimePicker(
      context: context,
      initialTime: initialTime ?? TimeOfDay.now(),
      helpText: "Gebe die Uhrzeit ein",
      hourLabelText: "Stunden",
      minuteLabelText: "Minuten",
      cancelText: "Abbrechen",
      confirmText: "Ok",
      errorInvalidText: "Gebe eine gültige Uhrzeit ein",
      initialEntryMode: PlatformHelper.isMobile()
          ? TimePickerEntryMode.dial
          : TimePickerEntryMode.input,
      builder: (context, child) {
        return MediaQuery(
          data: MediaQuery.of(context).copyWith(alwaysUse24HourFormat: true),
          child: child ?? Container(),
        );
      },
    );
  }

  Future<DateTime?> _createDatePicker(
      BuildContext context, DateTime? initialDate) async {
    return showDatePicker(
      context: context,
      initialDate: initialDate ?? DateTime.now(),
      firstDate: DateTime.now(),
      lastDate: DateTime.now().add(const Duration(days: 365 * 10)),
      cancelText: "Abbrechen",
      confirmText: "Ok",
      helpText: "Gebe das Datum ein",
      errorInvalidText: "Gebe ein gültiges Datum ein",
    );
  }

  void _pickStartDate() async {
    DateTime? pickedDate = await _createDatePicker(context, _startDate);
    if (pickedDate != null) {
      _startDate = pickedDate;
      setState(() {
        _sendTimes();
        _stringStartDate = DateFormat('dd.MM.yyyy').format(_startDate!);
      });
    }
  }

  void _pickEndDate() async {
    DateTime? pickedDate = await _createDatePicker(context, _endDate);
    if (pickedDate != null) {
      _endDate = pickedDate;
      setState(() {
        _sendTimes();
        _stringEndDate = DateFormat('dd.MM.yyyy').format(_endDate!);
      });
    }
  }

  void _pickStartTime() async {
    TimeOfDay? pickedTime = await _createTimePicker(context, _startTime);
    if (pickedTime != null) {
      _startTime = pickedTime;
      setState(() {
        _sendTimes();
        _stringStartTime =
            '${_startTime!.hour}:${_startTime!.minute.toString().padLeft(2, '0')}';
      });
    }
  }

  void _pickEndTime() async {
    TimeOfDay? pickedTime = await _createTimePicker(context, _endTime);
    if (pickedTime != null) {
      _endTime = pickedTime;
      setState(() {
        _sendTimes();
        _stringEndTime =
            '${_endTime!.hour}:${_endTime!.minute.toString().padLeft(2, '0')}';
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      mainAxisSize: MainAxisSize.min,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Flexible(
          fit: FlexFit.loose,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const Text('Von',
                  style: TextStyle(color: INBETWEEN_GRAY, fontSize: 16)),
              Container(
                padding: SMALL_PADDING,
                height: 50,
                decoration: const BoxDecoration(
                  color: GRAY_BLUEISH,
                  borderRadius: CARD_BORDERRADIUS,
                ),
                child: Row(
                  mainAxisSize: MainAxisSize.max,
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        TextButton(
                          onPressed: () {
                            _pickStartDate();
                          },
                          child: Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: [
                              const Icon(Icons.calendar_month,
                                  size: 25, color: GRAY),
                              Text(_stringStartDate,
                                  style: const TextStyle(
                                      color: GRAY,
                                      fontWeight: FontWeight.w500)),
                            ],
                          ),
                        ),
                        TextButton(
                          onPressed: () {
                            _pickStartTime();
                          },
                          child: Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: [
                              const Icon(Icons.alarm, size: 25, color: GRAY),
                              Text(_stringStartTime,
                                  style: const TextStyle(
                                      color: GRAY,
                                      fontWeight: FontWeight.w500)),
                            ],
                          ),
                        ),
                      ],
                    ),
                    Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        Container(
                            width: 3,
                            height: 25,
                            decoration: const BoxDecoration(
                                borderRadius: CARD_BORDERRADIUS, color: GRAY)),
                        const Icon(Icons.arrow_forward, size: 30, color: GRAY),
                      ],
                    )
                  ],
                ),
              ),
            ],
          ),
        ),
        const SizedBox(height: 10),
        Flexible(
          fit: FlexFit.loose,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const Text('Bis',
                  style: TextStyle(color: INBETWEEN_GRAY, fontSize: 16)),
              Container(
                padding: SMALL_PADDING,
                height: 50,
                decoration: const BoxDecoration(
                  color: GRAY_BLUEISH,
                  borderRadius: CARD_BORDERRADIUS,
                ),
                child: Row(
                  mainAxisSize: MainAxisSize.max,
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Row(
                      mainAxisSize: MainAxisSize.max,
                      children: [
                        TextButton(
                          onPressed: () {
                            _pickEndDate();
                          },
                          child: Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: [
                              const Icon(Icons.calendar_month,
                                  size: 25, color: GRAY),
                              Text(_stringEndDate,
                                  style: const TextStyle(
                                      color: GRAY,
                                      fontWeight: FontWeight.w500)),
                            ],
                          ),
                        ),
                        TextButton(
                          onPressed: () {
                            _pickEndTime();
                          },
                          child: Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: [
                              const Icon(Icons.alarm, size: 25, color: GRAY),
                              Text(
                                _stringEndTime,
                                style: const TextStyle(
                                    color: GRAY, fontWeight: FontWeight.w500),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                    Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        const Icon(Icons.arrow_forward, size: 30, color: GRAY),
                        Container(
                            width: 3,
                            height: 30,
                            decoration: const BoxDecoration(
                                borderRadius: CARD_BORDERRADIUS, color: GRAY)),
                      ],
                    )
                  ],
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }
}
