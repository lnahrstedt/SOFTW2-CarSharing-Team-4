import 'package:fastlane/widget/vehiclePage/timeframePicker.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:intl/intl.dart';

void main() {
  testWidgets('Build', (tester) async {
    DateTime? fetchedStartDate;
    DateTime? fetchedEndDate;

    void fetchDateTimes(DateTime startDate, DateTime endDate) {
      fetchedStartDate = startDate;
      fetchedEndDate = endDate;
    }

    await tester.pumpWidget(
      MaterialApp(
        home: TimeframePicker(getTimes: fetchDateTimes),
      ),
    );

    expect(find.byType(TimeframePicker), findsOneWidget);
    expect(find.text('Datum'), findsNWidgets(2));
    expect(find.text('Uhrzeit'), findsNWidgets(2));
    expect(fetchedStartDate, null);
    expect(fetchedEndDate, null);
  });

  Future<void> setDatePicker(Finder calendar, WidgetTester tester) async {
    await tester.tap(calendar);
    await tester.pumpAndSettle();
    expect(find.byType(CalendarDatePicker), findsOneWidget);
    await tester.tap(find.text(DateTime.now().day.toString()));
    await tester.tap(find.text('Ok'));
    await tester.pumpAndSettle();
  }

  testWidgets('First DatePicker', (tester) async {
    DateTime? fetchedStartDate;
    DateTime? fetchedEndDate;

    void fetchDateTimes(DateTime startDate, DateTime endDate) {
      fetchedStartDate = startDate;
      fetchedEndDate = endDate;
    }

    await tester.pumpWidget(
      MaterialApp(
        home: TimeframePicker(getTimes: fetchDateTimes),
      ),
    );

    await setDatePicker(find.byIcon(Icons.calendar_month).first, tester);
    expect(find.text(DateFormat('dd.MM.yyyy').format(DateTime.now())),
        findsOneWidget);

    expect(fetchedStartDate, null);
    expect(fetchedEndDate, null);
  });

  testWidgets('Second datePicker', (tester) async {
    DateTime? fetchedStartDate;
    DateTime? fetchedEndDate;

    void fetchDateTimes(DateTime startDate, DateTime endDate) {
      fetchedStartDate = startDate;
      fetchedEndDate = endDate;
    }

    await tester.pumpWidget(
      MaterialApp(
        home: TimeframePicker(getTimes: fetchDateTimes),
      ),
    );

    await setDatePicker(find.byIcon(Icons.calendar_month).last, tester);
    expect(find.text(DateFormat('dd.MM.yyyy').format(DateTime.now())),
        findsOneWidget);

    expect(fetchedStartDate, null);
    expect(fetchedEndDate, null);
  });

  testWidgets('Both datePickers simultaneously', (tester) async {
    DateTime? fetchedStartDate;
    DateTime? fetchedEndDate;

    void fetchDateTimes(DateTime startDate, DateTime endDate) {
      fetchedStartDate = startDate;
      fetchedEndDate = endDate;
    }

    await tester.pumpWidget(
      MaterialApp(
        home: TimeframePicker(getTimes: fetchDateTimes),
      ),
    );

    await setDatePicker(find.byIcon(Icons.calendar_month).first, tester);
    await setDatePicker(find.byIcon(Icons.calendar_month).last, tester);

    expect(find.text(DateFormat('dd.MM.yyyy').format(DateTime.now())),
        findsNWidgets(2));

    expect(fetchedStartDate, null);
    expect(fetchedEndDate, null);
  });

  Future<void> setTimePicker(Finder timePicker, WidgetTester tester) async {
    await tester.tap(timePicker);
    await tester.pumpAndSettle();
    expect(find.byType(TimePickerDialog), findsOneWidget);
    await tester.enterText(find.text(TimeOfDay.now().hour.toString()), '12');
    await tester.enterText(find.text(TimeOfDay.now().minute.toString()), '30');
    await tester.tap(find.text('Ok'));
    await tester.pumpAndSettle();
  }

  testWidgets('First timePicker', (tester) async {
    DateTime? fetchedStartDate;
    DateTime? fetchedEndDate;

    void fetchDateTimes(DateTime startDate, DateTime endDate) {
      fetchedStartDate = startDate;
      fetchedEndDate = endDate;
    }

    await tester.pumpWidget(
      MaterialApp(
        home: TimeframePicker(getTimes: fetchDateTimes),
      ),
    );

    await setTimePicker(find.byIcon(Icons.alarm).first, tester);
    expect(find.text('12:30'), findsOneWidget);

    expect(fetchedStartDate, null);
    expect(fetchedEndDate, null);
  });

  testWidgets('Second timePicker', (tester) async {
    DateTime? fetchedStartDate;
    DateTime? fetchedEndDate;

    void fetchDateTimes(DateTime startDate, DateTime endDate) {
      fetchedStartDate = startDate;
      fetchedEndDate = endDate;
    }

    await tester.pumpWidget(
      MaterialApp(
        home: TimeframePicker(getTimes: fetchDateTimes),
      ),
    );

    await setTimePicker(find.byIcon(Icons.alarm).last, tester);
    expect(find.text('12:30'), findsOneWidget);

    expect(fetchedStartDate, null);
    expect(fetchedEndDate, null);
  });

  testWidgets('Both timePicker simultaneously', (tester) async {
    DateTime? fetchedStartDate;
    DateTime? fetchedEndDate;

    void fetchDateTimes(DateTime startDate, DateTime endDate) {
      fetchedStartDate = startDate;
      fetchedEndDate = endDate;
    }

    await tester.pumpWidget(
      MaterialApp(
        home: TimeframePicker(getTimes: fetchDateTimes),
      ),
    );

    await setTimePicker(find.byIcon(Icons.alarm).first, tester);
    await setTimePicker(find.byIcon(Icons.alarm).last, tester);
    expect(find.text('12:30'), findsNWidgets(2));

    expect(fetchedStartDate, null);
    expect(fetchedEndDate, null);
  });

  testWidgets('All values set', (tester) async {
    DateTime? fetchedStartDate;
    DateTime? fetchedEndDate;

    void fetchDateTimes(DateTime startDate, DateTime endDate) {
      fetchedStartDate = startDate;
      fetchedEndDate = endDate;
    }

    await tester.pumpWidget(
      MaterialApp(
        home: TimeframePicker(getTimes: fetchDateTimes),
      ),
    );

    await setDatePicker(find.byIcon(Icons.calendar_month).first, tester);
    await setDatePicker(find.byIcon(Icons.calendar_month).last, tester);
    await setTimePicker(find.byIcon(Icons.alarm).first, tester);
    await setTimePicker(find.byIcon(Icons.alarm).last, tester);

    DateTime todayTest = DateTime.now();
    todayTest = DateTime(todayTest.year, todayTest.month, todayTest.day);
    todayTest = todayTest.add(const Duration(hours: 12, minutes: 30));
    expect(fetchedStartDate, todayTest);
    expect(fetchedEndDate, todayTest);
  });
}