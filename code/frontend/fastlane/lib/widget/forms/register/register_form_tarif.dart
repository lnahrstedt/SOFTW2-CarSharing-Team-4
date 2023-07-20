import 'package:fastlane/data/registration_values.dart' as account;
import 'package:fastlane/widget/forms/register/register_form_address.dart';
import 'package:fastlane/widget/forms/register/register_form_email.dart';
import 'package:fastlane/widget/indicators/fastlane_circular_percent_indicator.dart';
import 'package:flutter/material.dart';

import '../../../control/mediaChecker.dart';

/// The RegisterFormTarif class is a StatefulWidget in Dart.
class RegisterFormTarif extends StatefulWidget {
  const RegisterFormTarif({super.key});

  @override
  State<StatefulWidget> createState() => _RegisterFormTarif();
}

enum SelectedCard { none, JUNIOR, COMFORT, PREMIUM }

/// The `_RegisterFormTarif` class is a stateful widget that displays a dialog for
/// selecting a tariff option and navigating to the next step in the registration
/// process.
class _RegisterFormTarif extends State<RegisterFormTarif> {
  var pressedContinueOrBack = false;
  var _colorJunior = Colors.white;
  var _colorComfort = Colors.white;
  var _colorPremium = Colors.white;
  SelectedCard _selectedCard = SelectedCard.none;

  @override
  void initState() {
    switch (account.typeName) {
      case "JUNIOR":
        _selectedCard = SelectedCard.JUNIOR;
        break;
      case "COMFORT":
        _selectedCard = SelectedCard.COMFORT;
        break;
      case "PREMIUM":
        _selectedCard = SelectedCard.PREMIUM;
        break;
      default:
        _selectedCard = SelectedCard.none;
    }
    setState(() {});
    super.initState();
  }

  @override
  void setState(VoidCallback fn) {
    super.setState(fn);
    switch (_selectedCard) {
      case SelectedCard.JUNIOR:
        _colorJunior = const Color(0x501FFFF2);
        _colorComfort = Colors.white;
        _colorPremium = Colors.white;
        break;
      case SelectedCard.COMFORT:
        _colorJunior = Colors.white;
        _colorComfort = const Color(0x501FFFF2);
        _colorPremium = Colors.white;
        break;
      case SelectedCard.PREMIUM:
        _colorJunior = Colors.white;
        _colorComfort = Colors.white;
        _colorPremium = const Color(0x501FFFF2);
        break;
      case SelectedCard.none:
        _colorJunior = Colors.white;
        _colorComfort = Colors.white;
        _colorPremium = Colors.white;
        break;
      default:
        _colorJunior = Colors.white;
        _colorComfort = Colors.white;
        _colorPremium = Colors.white;
    }
  }

  @override
  void dispose() {
    if (!pressedContinueOrBack) {
      account.setDefaults();
      pressedContinueOrBack = false;
    }
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      shape: const RoundedRectangleBorder(
          borderRadius: BorderRadius.all(Radius.circular(32.0))),
      scrollable: true,
      title: Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [
        const Text('Registrieren'),
        IconButton(
            onPressed: () {
              Navigator.of(context).pop();
            },
            icon: const Icon(Icons.close))
      ]),
      content: Padding(
        padding: const EdgeInsets.all(10.0),
        child: Form(
          child: Column(
            children: [
              Row(
                children: [
                  Column(
                    children: [
                      SizedBox(
                        width: 200,
                        height: 300,
                        child: ListView(
                          children: [
                            Card(
                              color: _colorJunior,
                              clipBehavior: Clip.hardEdge,
                              child: InkWell(
                                splashColor: Colors.blue.withAlpha(30),
                                onTap: () {
                                  _selectedCard = SelectedCard.JUNIOR;
                                  setState(() {});
                                },
                                child: const SizedBox(
                                  width: 200,
                                  height: 250,
                                  child: Padding(
                                    padding: EdgeInsets.all(8.0),
                                    child: Column(
                                      mainAxisAlignment:
                                          MainAxisAlignment.spaceEvenly,
                                      children: [
                                        Text('Junior',
                                            style: TextStyle(
                                                fontWeight: FontWeight.bold,
                                                fontSize: 16)),
                                        Image(
                                            image: AssetImage(
                                                'assets/junior.png')),
                                        Text(
                                            'Der ideale Tarif für Einsteiger und Gelegenheitsfahrer.',
                                            style: TextStyle(fontSize: 14))
                                      ],
                                    ),
                                  ),
                                ),
                              ),
                            ),
                            Card(
                              color: _colorComfort,
                              clipBehavior: Clip.hardEdge,
                              child: InkWell(
                                splashColor: Colors.blue.withAlpha(30),
                                onTap: () {
                                  _selectedCard = SelectedCard.COMFORT;
                                  setState(() {});
                                },
                                child: const SizedBox(
                                  width: 200,
                                  height: 250,
                                  child: Padding(
                                    padding: EdgeInsets.all(8.0),
                                    child: Column(
                                      mainAxisAlignment:
                                          MainAxisAlignment.spaceEvenly,
                                      children: [
                                        Text('Comfort',
                                            style: TextStyle(
                                                fontWeight: FontWeight.bold,
                                                fontSize: 16)),
                                        Image(
                                            image: AssetImage(
                                                'assets/comfort.png')),
                                        Text(
                                            'Ideal für Personen, die gelegentlich ein Auto benötigen.',
                                            style: TextStyle(fontSize: 14))
                                      ],
                                    ),
                                  ),
                                ),
                              ),
                            ),
                            Card(
                              color: _colorPremium,
                              clipBehavior: Clip.hardEdge,
                              child: InkWell(
                                splashColor: Colors.blue.withAlpha(30),
                                onTap: () {
                                  _selectedCard = SelectedCard.PREMIUM;
                                  setState(() {});
                                },
                                child: const SizedBox(
                                  width: 200,
                                  height: 250,
                                  child: Padding(
                                    padding: EdgeInsets.all(8.0),
                                    child: Column(
                                      mainAxisAlignment:
                                          MainAxisAlignment.spaceEvenly,
                                      children: [
                                        Text('Premium',
                                            style: TextStyle(
                                                fontWeight: FontWeight.bold,
                                                fontSize: 16)),
                                        Image(
                                            image: AssetImage(
                                                'assets/premium.png')),
                                        Text(
                                            'Der perfekte Tarif für Personen die häufig unterwegs sind.',
                                            style: TextStyle(fontSize: 14))
                                      ],
                                    ),
                                  ),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                  if (!MediaController.isSmallScreen(context))
                    const FastlaneCircularPercentIndicator(
                      percent: 0.75,
                    )
                ],
              ),
              const SizedBox(height: 25),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  IconButton(
                    onPressed: () {
                      account.typeName = _selectedCard.name;
                      account.countryCode = '';
                      pressedContinueOrBack = true;
                      Navigator.pop(context);
                      showDialog(
                          context: context,
                          builder: (BuildContext context) {
                            return const RegisterFormAddress();
                          });
                    },
                    icon: const Icon(Icons.arrow_back_rounded),
                  ),
                  IconButton(
                    onPressed: () {
                      if (_selectedCard == SelectedCard.none) return;

                      account.typeName = _selectedCard.name;
                      pressedContinueOrBack = true;
                      Navigator.pop(context);
                      showDialog(
                          context: context,
                          builder: (BuildContext context) {
                            return const RegisterFormEmail();
                          });
                    },
                    icon: const Icon(Icons.arrow_forward_rounded),
                  )
                ],
              )
            ],
          ),
        ),
      ),
    );
  }
}
