import 'package:fastlane/widget/forms/register/register_form_address.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  testWidgets('Tests the RegisterFormAddress', (tester) async {
    await tester.pumpWidget(const MaterialApp(
      home: RegisterFormAddress(),
    ));

    //verifies the existence of the TextFormFields
    expect(find.text('Postleitzahl'), findsOneWidget);
    expect(find.text('Ort'), findsOneWidget);
    expect(find.text('Straße und Hausnummer'), findsOneWidget);
    expect(find.text('Land'), findsOneWidget);

    //finds and enters text into postal field
    final postal = find.byType(TextFormField).at(0);
    await tester.enterText(postal, '28879');
    await tester.pump();

    //finds and enters text into city field
    final city = find.byType(TextFormField).at(1);
    await tester.enterText(city, 'Grasberg');
    await tester.pump();

    //finds and enters text into street field
    final street = find.byType(TextFormField).at(2);
    await tester.enterText(street, 'Musterstraße');
    await tester.pump();

    //opens country DropdownMenu and selects a country
    await tester.tap(find.text('Land'), warnIfMissed: false);
    await tester.pumpAndSettle();
    await tester.tap(find.text('Algerien'));
    await tester.pumpAndSettle();

    //verifies the entered values
    expect(find.text('28879'), findsOneWidget);
    expect(find.text('Grasberg'), findsOneWidget);
    expect(find.text('Musterstraße'), findsOneWidget);
    expect(find.text('Algerien'), findsOneWidget);

    //verifies the existence of the forward and backward arrows
    expect(find.byIcon(Icons.arrow_back_rounded), findsOneWidget);
    expect(find.byIcon(Icons.arrow_forward_rounded), findsOneWidget);
  });
}
