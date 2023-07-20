import 'dart:async';

import 'package:fastlane/constants/addresses.dart';
import 'package:fastlane/constants/colors.dart';
import 'package:fastlane/control/mediaChecker.dart';
import 'package:fastlane/data/registration_values.dart' as account;
import 'package:fastlane/widget/forms/register/register_form_address.dart';
import 'package:fastlane/widget/indicators/fastlane_circular_percent_indicator.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:http/http.dart' as http;
import 'package:intl/intl.dart';

/// The class RegisterFormPersonal is a StatefulWidget in Dart that represents a
/// personal information form for registration.
class RegisterFormPersonal extends StatefulWidget {
  const RegisterFormPersonal({super.key});

  @override
  State<StatefulWidget> createState() => _RegisterFormPersonalState();
}

/// The `_RegisterFormPersonalState` class is a stateful widget that represents a
/// form for registering personal information, including fields for first name, last
/// name, birth date, place of birth, phone number, and driver's license ID.
class _RegisterFormPersonalState extends State<RegisterFormPersonal> {
  final _formKey = GlobalKey<FormState>();

  var pressedContinue = false;
  final _dateInput = TextEditingController();

  final _specialCharRegex = RegExp(r'[^;:\\|{}\]\[]');
  final _licenseRegex = RegExp(r'\w+');
  final _phoneRegex = RegExp(r'^[+]?\d+$');

  bool _isChecking = false;
  dynamic _validationMsg;
  final _licenseCtrl = TextEditingController();

  String? _validateField(String? value) {
    if (value?.trim() == null || value!.trim().isEmpty) {
      return 'Feld darf nicht leer sein';
    } else if (!_specialCharRegex.hasMatch(value)) {
      return 'Feld darf keine Sonderzeichen enthalten';
    } else {
      return null;
    }
  }

  Future<dynamic> _validateLicense(String? value) async {
    _validationMsg = null;
    setState(() {});

    if (value?.trim() == null || value!.trim().isEmpty) {
      _validationMsg = 'Feld darf nicht leer sein';
      setState(() {});
      return false;
    } else if (!_licenseRegex.hasMatch(value)) {
      _validationMsg = 'Bitte valide Führerschein ID eingeben';
      setState(() {});
      return false;
    }

    _isChecking = true;
    setState(() {});
    bool exists = false;
    try {
      exists = await doesLicenseExist(value);
    } catch (e) {
      _validationMsg = e.toString();
      _isChecking = false;
      setState(() {});
      return false;
    }
    if (exists) {
      _isChecking = false;
      _validationMsg = 'Führerschein Id existiert bereits';
      setState(() {});
      return false;
    }
    _isChecking = false;
    setState(() {});
    return true;
  }

  Future<bool> doesLicenseExist(String? licenseId) async {
    final response = await http
        .get(
          Uri.parse("$BACKEND_ADDRESS/driver/exist/$licenseId"),
        )
        .timeout(const Duration(seconds: 5),
            onTimeout: () => throw TimeoutException('Timeout'));

    if (response.statusCode == 200) {
      return false;
    } else if (response.statusCode == 409) {
      return true;
    } else if (response.statusCode == 401) {
      throw Exception('[${response.statusCode}] Unauthorized');
    } else {
      throw Exception('[${response.statusCode}] Backend nicht erreichbar');
    }
  }

  String? _validatePhone(String? value) {
    if (value?.trim() == null || value!.trim().isEmpty) {
      return 'Feld darf nicht leer sein';
    } else if (!_phoneRegex.hasMatch(value)) {
      return 'Bitte valide Telefonnummer eingeben';
    }
    return null;
  }

  String? _validateBirthDate(String? value) {
    if (value?.trim() == null || value!.trim().isEmpty) {
      return 'Feld darf nicht leer sein';
    }
    return null;
  }

  @override
  void initState() {
    _licenseCtrl.text = account.id;
    try {
      _dateInput.text =
          DateFormat('dd.MM.yyyy').format(DateTime.parse(account.dateOfBirth));
    } catch (e) {
      _dateInput.text = '';
    }
    super.initState();
  }

  @override
  void dispose() {
    if (!pressedContinue) {
      account.setDefaults();
      pressedContinue = false;
    }
    super.dispose();
  }

  void _navigatePage() {
    Navigator.pop(context);
    showDialog(
        context: context,
        builder: (BuildContext context) {
          return const RegisterFormAddress();
        });
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
                          initialValue: account.firstName,
                          inputFormatters: [
                            FilteringTextInputFormatter.allow(_specialCharRegex)
                          ],
                          validator: _validateField,
                          onSaved: (value) {
                            account.firstName = value!;
                          },
                          decoration:
                              const InputDecoration(labelText: 'Vorname'),
                        ),
                        TextFormField(
                          initialValue: account.lastName,
                          inputFormatters: [
                            FilteringTextInputFormatter.allow(_specialCharRegex)
                          ],
                          validator: _validateField,
                          onSaved: (value) {
                            account.lastName = value!;
                          },
                          decoration:
                              const InputDecoration(labelText: 'Nachname'),
                        ),
                        TextFormField(
                          validator: _validateBirthDate,
                          controller: _dateInput,
                          decoration: const InputDecoration(
                              labelText: 'Geburtsdatum',
                              suffixIcon: Icon(Icons.calendar_month)),
                          readOnly: true,
                          onTap: () async {
                            DateTime? pickedDate = await showDatePicker(
                                context: context,
                                initialDate: DateTime.now().subtract(
                                    const Duration(days: (365 * 18) + 4)),
                                firstDate: DateTime.now()
                                    .subtract(const Duration(days: 365 * 100)),
                                lastDate: DateTime.now().subtract(
                                    const Duration(days: (365 * 18) + 4)),
                                builder: (BuildContext context, Widget? child) {
                                  return Theme(
                                    data: ThemeData.light().copyWith(
                                        primaryColor: LESSER_BLUE,
                                        hintColor: LESSER_BLUE,
                                        colorScheme: const ColorScheme.light(
                                            primary: LESSER_BLUE),
                                        buttonTheme: const ButtonThemeData(
                                            textTheme: ButtonTextTheme.accent)),
                                    child: child!,
                                  );
                                });

                            if (pickedDate != null) {
                              String formattedDate =
                                  DateFormat('yyyy-MM-dd').format(pickedDate);
                              account.dateOfBirth = formattedDate;
                              setState(() {
                                _dateInput.text =
                                    DateFormat('dd.MM.yyyy').format(pickedDate);
                              });
                            }
                          },
                        ),
                        TextFormField(
                          initialValue: account.placeOfBirth,
                          inputFormatters: [
                            FilteringTextInputFormatter.allow(_specialCharRegex)
                          ],
                          validator: _validateField,
                          onSaved: (value) {
                            account.placeOfBirth = value!;
                          },
                          decoration:
                              const InputDecoration(labelText: 'Geburtsort'),
                        ),
                        TextFormField(
                          initialValue: account.phone,
                          validator: _validatePhone,
                          onSaved: (value) {
                            account.phone = value!;
                          },
                          decoration:
                              const InputDecoration(labelText: 'Telefonnummer'),
                        ),
                        Focus(
                          child: TextFormField(
                            controller: _licenseCtrl,
                            autovalidateMode:
                                AutovalidateMode.onUserInteraction,
                            inputFormatters: [
                              FilteringTextInputFormatter.allow(_licenseRegex)
                            ],
                            validator: (val) => _validationMsg,
                            onSaved: (value) {
                              account.id = value!;
                            },
                            onFieldSubmitted: (value) async {
                              if (_formKey.currentState!.validate() &&
                                  await _validateLicense(_licenseCtrl.text)) {
                                _formKey.currentState?.save();
                                pressedContinue = true;
                                _navigatePage();
                              }
                            },
                            decoration: InputDecoration(
                                labelText: 'Führerschein Id',
                                suffixIcon: _isChecking
                                    ? Transform.scale(
                                        scale: 0.5,
                                        child:
                                            const CircularProgressIndicator())
                                    : null),
                          ),
                          onFocusChange: (hasFocus) {
                            if (!hasFocus) _validateLicense(_licenseCtrl.text);
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
                      percent: 0.25,
                    )
                ],
              ),
              Align(
                alignment: Alignment.bottomRight,
                child: IconButton(
                  onPressed: () async {
                    // Validate returns true if the form is valid, or false otherwise.
                    if (_formKey.currentState!.validate() &&
                        await _validateLicense(_licenseCtrl.text)) {
                      _formKey.currentState?.save();
                      pressedContinue = true;
                      _navigatePage();
                    }
                  },
                  icon: const Icon(Icons.arrow_forward_rounded),
                ),
              )
            ],
          ),
        ),
      ),
    );
  }
}
