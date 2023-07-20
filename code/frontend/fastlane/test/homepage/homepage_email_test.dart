import 'package:fastlane/widget/buttons/fastlane_elevated_cyan_icon.dart';
import 'package:fastlane/widget/forms/register/register_form_email.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  testWidgets('Tests the RegisterFormEmail', (tester) async {
    await tester.pumpWidget(const MaterialApp(
      home: RegisterFormEmail(),
    ));

    //finds the Tariff selection cards
    expect(find.text('Email'), findsOneWidget);
    expect(find.text('Passwort'), findsOneWidget);
    expect(find.text('Passwort bestätigen'), findsOneWidget);

    //finds and enters text into email field
    final email = find.byType(TextFormField).at(0);
    await tester.enterText(email, 'max.mustermann@mail.com');
    await tester.pump();

    //finds and enters text into password field
    final password = find.byType(TextFormField).at(1);
    await tester.enterText(password, 'Hallo1010!');
    await tester.pump();

    //finds and enters text into repeat password field
    final repeatPassword = find.byType(TextFormField).at(2);
    await tester.enterText(repeatPassword, 'Hallo1010!');
    await tester.pump();

    //verifies the entered values
    expect(find.text('max.mustermann@mail.com'), findsOneWidget);
    expect(find.text('Hallo1010!'), findsWidgets);

    //verifies the existence of the forward and backward arrows
    expect(find.byIcon(Icons.arrow_back_rounded), findsOneWidget);
    expect(find.byType(FastlaneElevatedButtonCyanIcon), findsOneWidget);
  });
}