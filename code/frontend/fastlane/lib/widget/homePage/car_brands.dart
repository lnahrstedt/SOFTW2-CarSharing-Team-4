import 'package:flutter/material.dart';

/// The CarBrands class is a StatefulWidget in Dart that represents a list of car
/// brands.
class CarBrands extends StatefulWidget {
  const CarBrands({super.key});

  @override
  State<StatefulWidget> createState() => _CarBrandsState();
}

/// The `_CarBrandsState` class in Dart creates a column layout with an introductory
/// text and brand cards for BMW, Opel, and Volkswagen.
class _CarBrandsState extends State<CarBrands> {
  Wrap createIntroText() {
    return const Wrap(
      crossAxisAlignment: WrapCrossAlignment.center,
      direction: Axis.horizontal,
      spacing: 200,
      runSpacing: 20,
      children: [
        SizedBox(
            width: 300,
            height: 300,
            child:
                Image(image: AssetImage('assets/homepage/locationImage.png'))),
        Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('Unsere Automarken',
                style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold)),
            Text(
              "Unsere Flotte beinhaltet eine Vielfalt an renommierten\nAutomarken, damit wir jedem individuellen Geschmack\nund Bedürfnis gerecht werden und Ihnen stets ein\nerstklassiges Fahrerlebnis bieten können.",
              maxLines: 8,
              softWrap: true,
            )
          ],
        ),
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
            ],
          ),
        ),
      ),
    );
  }

  Wrap createBrandCards() {
    return Wrap(
      direction: Axis.horizontal,
      crossAxisAlignment: WrapCrossAlignment.center,
      spacing: 75,
      runSpacing: 10,
      children: [
        createCard('BMW', 'assets/homepage/BMW.png'),
        createCard('Opel', 'assets/homepage/Opel.png'),
        createCard('Volkswagen', 'assets/homepage/VW.png'),
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
        createBrandCards()
      ],
    );
  }
}
