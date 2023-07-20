import 'package:fastlane/model/vehicle.dart';
import 'package:fastlane/widget/mapPage/vehicleButton.dart';
import 'package:fastlane/widget/vehiclePage/vehiclePage.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:http/http.dart' as http;
import 'package:mockito/mockito.dart';

class MockClient extends Mock implements http.Client {}

void main() {
  Vehicle testVehicle = Vehicle.fromJson({
    "id": 1,
    "numberPlate": "EB-B4-91-C7-2C-51",
    "brand": "Volkswagen",
    "model": "Arteon",
    "latitude": 38.2643887,
    "constructionYear": 1964,
    "configuration": {
      "category": "Kombi",
      "type": "PKW",
      "transmission": "Manuell",
      "fuel": "Benzin",
      "ac": true,
      "navigation": true,
      "cruiseControl": true,
      "drivingAssistent": true,
    },
    "longitude": 140.2604198
  });

  testWidgets('Build', (tester) async {
    await tester.pumpWidget(
      MaterialApp(
        home: Material(child: VehicleButton(vehicle: testVehicle)),
      ),
    );

    expect(find.byType(VehicleButton), findsOneWidget);
    expect(find.byIcon(Icons.arrow_forward), findsOneWidget);
  });

  testWidgets('Buttonpress', (tester) async {
    final responseAllReservations = http.Response('[]', 200);
    final responseSpecificReservations = http.Response('[]', 200);
    final mockClient = MockClient();

    when(mockClient.get(Uri.parse('http://localhost:8080/reservation')))
        .thenAnswer((_) async => responseAllReservations);

    when(mockClient.get(Uri.parse('http://localhost:8080/reservation/vehicle/1')))
        .thenAnswer((_) async => responseSpecificReservations);

    await tester.pumpWidget(
      MaterialApp(
        home: Material(child: VehicleButton(vehicle: testVehicle)),
      ),
    );

    await tester.tap(find.byIcon(Icons.arrow_forward));
    await tester.pumpAndSettle();
    expect(find.byType(FastlaneDashboardVehiclePage), findsOneWidget);
  });
}