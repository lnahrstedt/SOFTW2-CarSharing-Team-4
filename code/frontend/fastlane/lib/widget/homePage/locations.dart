import 'package:flutter/material.dart';

/// The `LocationInfo` class is a stateful widget in Dart that provides information
/// about a location.
class LocationInfo extends StatefulWidget {
  const LocationInfo({super.key});

  @override
  State<StatefulWidget> createState() => _LocationInfoState();
}

/// The `_LocationInfoState` class in Dart creates a widget that displays
/// information about different locations, including an introduction text and
/// location cards.
class _LocationInfoState extends State<LocationInfo> {
  Wrap createIntroText() {
    return const Wrap(
      crossAxisAlignment: WrapCrossAlignment.center,
      direction: Axis.horizontal,
      spacing: 200,
      runSpacing: 20,
      children: [
        Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('Unsere Standorte',
                style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold)),
            Text(
              "Egal ob Sie durch die belebten Straßen\nvon Bremen und Hamburg cruisen,\noder die idyllischen Landschaften von Schwanewede,\nGanderkesee oder Rautendorf erkunden möchten\n- mit uns sind Sie stets flexibel und komfortabel unterwegs!",
              maxLines: 8,
              softWrap: true,
            )
          ],
        ),
        SizedBox(
            width: 300,
            height: 300,
            child:
                Image(image: AssetImage('assets/homepage/locationImage.png')))
      ],
    );
  }

  Card createCard(String text, String asset) {
    return Card(
      elevation: 5,
      clipBehavior: Clip.hardEdge,
      child: SizedBox(
        width: 200,
        height: 250,
        child: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Column(
            children: [
              SizedBox(
                  width: 150,
                  height: 150,
                  child: Image(image: AssetImage(asset))),
              const SizedBox(height: 30),
              Text(
                text,
                style:
                    const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
              ),
              const Text('Hauptstadt', style: TextStyle(fontSize: 10))
            ],
          ),
        ),
      ),
    );
  }

  Wrap createLocationCards() {
    return Wrap(
      direction: Axis.horizontal,
      crossAxisAlignment: WrapCrossAlignment.center,
      spacing: 75,
      runSpacing: 10,
      children: [
        createCard('Rautendorf', 'assets/homepage/Rautendorf.png'),
        createCard('Ganderkesee', 'assets/homepage/Ganderkesee.png'),
        createCard('Schwanewede', 'assets/homepage/Schwanewede.png'),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        createIntroText(),
        const SizedBox(height: 20),
        createLocationCards()
      ],
    );
  }
}
