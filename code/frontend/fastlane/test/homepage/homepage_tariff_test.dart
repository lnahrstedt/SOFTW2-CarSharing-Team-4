import 'package:fastlane/widget/forms/register/register_form_tarif.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  testWidgets('Tests the RegisterFormTarif', (tester) async {
    await tester.pumpWidget(const MaterialApp(
      home: RegisterFormTarif(),
    ));

    //finds the Tariff selection cards
    expect(find.text('Junior'), findsOneWidget);
    expect(find.text('Comfort'), findsOneWidget);
    expect(find.text('Premium'), findsOneWidget);

    //verifies the existence of the forward and backward arrows
    expect(find.byIcon(Icons.arrow_back_rounded), findsOneWidget);
    expect(find.byIcon(Icons.arrow_forward_rounded), findsOneWidget);
  });
}