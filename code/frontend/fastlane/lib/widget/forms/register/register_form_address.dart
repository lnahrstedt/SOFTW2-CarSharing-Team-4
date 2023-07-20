import 'package:fastlane/constants/countryCode.dart';
import 'package:fastlane/data/registration_values.dart' as account;
import 'package:fastlane/widget/forms/register/register_form_personal.dart';
import 'package:fastlane/widget/forms/register/register_form_tarif.dart';
import 'package:fastlane/widget/indicators/fastlane_circular_percent_indicator.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../../../control/mediaChecker.dart';
import '../../../model/country.dart';

/// The RegisterFormAddress class is a StatefulWidget that represents a form for
/// entering address information.
class RegisterFormAddress extends StatefulWidget {
  const RegisterFormAddress({super.key});

  @override
  State<StatefulWidget> createState() => _RegisterFormAddressState();
}

/// The `_RegisterFormAddressState` class is responsible for managing the state and
/// UI of a registration form for user addresses in a Dart application.
class _RegisterFormAddressState extends State<RegisterFormAddress> {
  late List<Country> countries;

  Future<void> loadCountries() async {
    countries =
        countryFromJson(await rootBundle.loadString('assets/countries.json'));
    for (final Country country in countries) {
      countryEntries
          .add(DropdownMenuEntry<Country>(value: country, label: country.name));
    }
    setState(() {});
  }

  final _formKey = GlobalKey<FormState>();

  List<DropdownMenuEntry<Country>> countryEntries =
      <DropdownMenuEntry<Country>>[];

  var pressedContinueOrBack = false;

  final _specialCharRegex = RegExp(r'[^;:\\|{}\]\[]');

  final _countryController = TextEditingController();
  var _selectedCountryCode = '';

  String? _validateField(String? value) {
    if (value?.trim() == null || value!.trim().isEmpty) {
      return 'Feld darf nicht leer sein';
    } else if (!_specialCharRegex.hasMatch(value)) {
      return 'Feld darf keine Sonderzeichen enthalten';
    }
    return null;
  }

  @override
  void setState(VoidCallback fn) {
    super.setState(fn);
  }

  @override
  void initState() {
    loadCountries();
    super.initState();
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
            key: _formKey,
            child: Column(
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    SizedBox(
                      width: 200,
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          TextFormField(
                              initialValue: account.postalCode,
                              inputFormatters: [
                                FilteringTextInputFormatter.allow(
                                    _specialCharRegex)
                              ],
                              validator: _validateField,
                              onSaved: (value) {
                                account.postalCode = value!;
                              },
                              decoration: const InputDecoration(
                                  labelText: 'Postleitzahl')),
                          TextFormField(
                              initialValue: account.city,
                              inputFormatters: [
                                FilteringTextInputFormatter.allow(
                                    _specialCharRegex)
                              ],
                              validator: _validateField,
                              onSaved: (value) {
                                account.city = value!;
                              },
                              decoration:
                                  const InputDecoration(labelText: 'Ort')),
                          TextFormField(
                              initialValue: account.street,
                              inputFormatters: [
                                FilteringTextInputFormatter.allow(
                                    _specialCharRegex)
                              ],
                              validator: _validateField,
                              onSaved: (value) {
                                account.street = value!;
                              },
                              keyboardType: TextInputType.streetAddress,
                              decoration: const InputDecoration(
                                  labelText: 'Straße und Hausnummer')),
                          const SizedBox(height: 20),
                          DropdownMenu(
                            width: 200,
                            controller: _countryController,
                            dropdownMenuEntries: countryEntries,
                            label: const Text('Land'),
                            trailingIcon: const Icon(Icons.arrow_drop_down),
                            onSelected: (Country? country) {
                              setState(() {
                                _countryController.text = country!.name;
                                account.countryCode = country.code;
                                countryCodeSaver = country.code;
                              });
                            },
                          ),
                          const SizedBox(height: 20),
                        ],
                      ),
                    ),
                    if (!MediaController.isSmallScreen(context))
                      const SizedBox(width: 50),
                    if (!MediaController.isSmallScreen(context))
                      const FastlaneCircularPercentIndicator(
                        percent: 0.5,
                      )
                  ],
                ),
                const SizedBox(height: 20),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    IconButton(
                      onPressed: () {
                        _formKey.currentState?.save();
                        account.countryCode = '';
                        pressedContinueOrBack = true;
                        Navigator.pop(context);
                        showDialog(
                            context: context,
                            builder: (BuildContext context) {
                              return const RegisterFormPersonal();
                            });
                      },
                      icon: const Icon(Icons.arrow_back_rounded),
                    ),
                    IconButton(
                        onPressed: () {
                          if (account.countryCode == '') {
                            _countryController.text = 'Bitte Land auswählen';
                            setState(() {});
                          }
                          if (_formKey.currentState!.validate() &&
                              account.countryCode != '') {
                            _formKey.currentState?.save();
                            pressedContinueOrBack = true;
                            Navigator.pop(context);
                            showDialog(
                                context: context,
                                builder: (BuildContext context) {
                                  return const RegisterFormTarif();
                                });
                          }
                        },
                        icon: const Icon(Icons.arrow_forward_rounded))
                  ],
                ),
              ],
            )),
      ),
    );
  }
}
