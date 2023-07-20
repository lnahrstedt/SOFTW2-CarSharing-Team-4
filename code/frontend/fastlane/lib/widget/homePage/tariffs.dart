import 'package:flutter/material.dart';

/// The Tariffs class is a StatefulWidget in Dart that represents a widget for
/// displaying tariffs.
class Tariffs extends StatefulWidget {
  const Tariffs({super.key});

  @override
  State<StatefulWidget> createState() => _TariffsState();
}

/// The `_TariffsState` class in Dart creates a widget that displays information
/// about different tariffs and their corresponding images.
class _TariffsState extends State<Tariffs> {
  Wrap createIntro() {
    return const Wrap(
      direction: Axis.horizontal,
      crossAxisAlignment: WrapCrossAlignment.center,
      spacing: 200,
      runSpacing: 20,
      children: [
        Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('Unsere Tarife',
                style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold)),
            Text(
              "Unsere Tarife sind perfekt auf Ihre Fahrbedürfnisse abgestimmt:\nMit dem Junior-Tarif richten wir uns an Gelegenheitsfahrer,\nder Comfort-Tarif ist ideal für regelmäßige Nutzer unserer Dienste\nund mit dem Premium-Tarif bedienen wir Vielfahrer, die ständig auf Achse sind.",
              maxLines: 8,
              softWrap: true,
            )
          ],
        ),
        SizedBox(
            width: 300,
            height: 300,
            child: Image(image: AssetImage('assets/homepage/TariffsIntro.png')))
      ],
    );
  }

  Card createCard(String title, String text, String asset) {
    return Card(
      elevation: 5,
      clipBehavior: Clip.hardEdge,
      child: SizedBox(
        width: 200,
        height: 300,
        child: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Align(
                  alignment: Alignment.topLeft,
                  child: Text(title,
                      style: const TextStyle(
                          fontSize: 20, fontWeight: FontWeight.bold))),
              SizedBox(
                  width: 150,
                  height: 150,
                  child: Image(image: AssetImage(asset))),
              const SizedBox(height: 30),
              Text(text, style: const TextStyle(fontSize: 14), softWrap: true),
            ],
          ),
        ),
      ),
    );
  }

  Wrap createTariffCards() {
    return Wrap(
      direction: Axis.horizontal,
      crossAxisAlignment: WrapCrossAlignment.center,
      spacing: 75,
      runSpacing: 10,
      children: [
        createCard(
            'Junior',
            'Der ideale Tarif für Einsteiger und Gelegenheitsfahrer',
            'assets/junior.png'),
        createCard(
            'Comfort',
            'Ideal für Personen, die gelegentlich ein Auto benötigen',
            'assets/comfort.png'),
        createCard(
            'Premium',
            'Der perfekte Tarif für Personen, die häufig unterwegs sind',
            'assets/premium.png'),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        createIntro(),
        const SizedBox(height: 20),
        createTariffCards()
      ],
    );
  }
}
