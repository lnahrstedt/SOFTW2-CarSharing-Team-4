import 'package:fastlane/widget/forms/register/register_form_personal.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:http/http.dart' as http;
import 'package:intl/intl.dart';
import 'package:mocktail/mocktail.dart';

Future<void> setDatePicker(Finder calendar, WidgetTester tester) async {
  await tester.tap(calendar);
  await tester.pumpAndSettle();
  expect(find.byType(CalendarDatePicker), findsOneWidget);
  await tester.tap(find.text(DateTime.now()
      .subtract(const Duration(days: 365 * 18)).day.toString()));
  await tester.tap(find.text('OK'));
  await tester.pumpAndSettle();
}

class MockClient extends Mock implements http.Client {}
class FakeUri extends Fake implements Uri {}

void main() {
  testWidgets('Tests the RegisterFormPersonal', (tester) async {
    await tester.pumpWidget(
        const MaterialApp(
          home: RegisterFormPersonal(),
        )
    );

    //finds the TextFormFields
    expect(find.text('Vorname'), findsOneWidget);
    expect(find.text('Nachname'), findsOneWidget);
    expect(find.text('Geburtsdatum'), findsOneWidget);
    expect(find.text('Geburtsort'), findsOneWidget);
    expect(find.text('Telefonnummer'), findsOneWidget);
    expect(find.text('Führerschein'), findsOneWidget);

    //finds firstName TextField and types 'Max'
    final firstName = find.byType(TextFormField).at(0);
    await tester.enterText(firstName, 'Max');
    await tester.pump();

    //finds lastName TextField and types 'Mustermann'
    final lastName = find.byType(TextFormField).at(1);
    await tester.enterText(lastName, 'Mustermann');
    await tester.pump();

    //finds and tests datePicker
    await setDatePicker(find.byIcon(Icons.calendar_month), tester);
    expect(find.text(DateFormat('dd.MM.yyyy').format(DateTime.now()
        .subtract(const Duration(days: 365 * 18)))),
        findsOneWidget);

    //finds placeOfBirth TextField and types 'Musterstadt'
    final placeOfBirth = find.byType(TextFormField).at(3);
    await tester.enterText(placeOfBirth, 'Musterstadt');
    await tester.pump();

    //finds phone TextField and types '123456'
    final phone = find.byType(TextFormField).at(4);
    await tester.enterText(phone, '123456');
    await tester.pump();

    //finds first TextField and types 'A1B2C3'
    final licenseId = find.byType(TextFormField).at(5);
    await tester.enterText(licenseId, 'A1B2C3');
    await tester.pump();

    //verifies the entered text
    expect(find.text('Max'), findsOneWidget);
    expect(find.text('Mustermann'), findsOneWidget);
    expect(find.text('Musterstadt'), findsOneWidget);
    expect(find.text('123456'), findsOneWidget);
    expect(find.text('A1B2C3'), findsOneWidget);

    //verifies the existence of the forward icon
    expect(find.byIcon(Icons.arrow_forward_rounded), findsOneWidget);
  });
}