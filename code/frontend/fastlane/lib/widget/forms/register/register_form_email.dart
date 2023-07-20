import 'dart:async';
import 'dart:convert';

import 'package:email_validator/email_validator.dart';
import 'package:fastlane/constants/accountType.dart';
import 'package:fastlane/constants/addresses.dart';
import 'package:fastlane/data/accountData.dart';
import 'package:fastlane/data/registration_values.dart' as account;
import 'package:fastlane/model/BackendException.dart';
import 'package:fastlane/widget/dashboard/navigation.dart';
import 'package:fastlane/widget/forms/register/register_form_tarif.dart';
import 'package:fastlane/widget/homePage/home_page.dart';
import 'package:fastlane/widget/indicators/fastlane_circular_percent_indicator.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:universal_html/html.dart' as html;

import '../../../control/mediaChecker.dart';
import '../../../data/driverData.dart';
import '../../../model/account_request.dart';
import '../../../model/authentication.dart';
import '../../buttons/fastlane_elevated_cyan_icon.dart';

/// The RegisterFormEmail class is a StatefulWidget in Dart that represents a form
/// for registering an email.
class RegisterFormEmail extends StatefulWidget {
  const RegisterFormEmail({super.key});

  @override
  State<StatefulWidget> createState() => _RegisterFormEmailState();
}

/// The `_RegisterFormEmailState` class is a stateful widget that represents a
/// registration form for email input in a Dart application.
class _RegisterFormEmailState extends State<RegisterFormEmail> {
  final _formKey = GlobalKey<FormState>();
  final _passKey = GlobalKey<FormFieldState>();
  final _passRepeatKey = GlobalKey<FormFieldState>();

  var pressedBack = false;

  bool _isChecking = false;
  dynamic _validationMsg;
  final _emailController = TextEditingController();

  final _passwordRegex =
      RegExp(r'^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z\d]).{8,}$');

  /// The function `_validateEmail` checks if a given email is valid and exists, and
  /// returns a boolean value indicating the result.
  ///
  /// Args:
  ///   value (String): The value parameter is a string that represents an email
  /// address.
  ///
  /// Returns:
  ///   a `Future<dynamic>`.
  Future<dynamic> _validateEmail(String? value) async {
    _validationMsg = null;
    setState(() {});

    if (value?.trim() == null ||
        value!.trim().isEmpty ||
        !EmailValidator.validate(value)) {
      _validationMsg = 'Bitte valide Email eingeben';
      setState(() {});
      return false;
    }

    _isChecking = true;
    setState(() {});
    bool exists = false;
    try {
      exists = await doesEmailExist(value);
    } catch (e) {
      _validationMsg = e.toString();
      _isChecking = false;
      setState(() {});
      return false;
    }
    if (exists) {
      _isChecking = false;
      _validationMsg = 'Email existiert bereits';
      setState(() {});
      return false;
    }
    _isChecking = false;
    setState(() {});
    return true;
  }

  Future<bool> doesEmailExist(String? email) async {
    final response = await http
        .get(
          Uri.parse("$BACKEND_ADDRESS/account/exist/$email"),
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

  String? _validatePassword(String? value) {
    if (value == null || value.isEmpty || !_passwordRegex.hasMatch(value)) {
      return 'Bitte Passwortanforderungen beachten';
    }
    return null;
  }

  String? _validateRepeatPassword(String? value) {
    if (value == null ||
        value.isEmpty ||
        _passKey.currentState!.value != _passRepeatKey.currentState!.value) {
      return 'Muss dem Passwort entsprechen';
    }
    return null;
  }

  @override
  void dispose() {
    if (!pressedBack) {
      account.setDefaults();
      pressedBack = false;
    }
    super.dispose();
  }

  /// The `_createAccount` function creates a new account by sending a POST request
  /// to a server and returns the created account if successful.
  ///
  /// Returns:
  ///   The function `_createAccount()` returns a `Future<AccountRequest>`.
  Future<AccountRequest> _createAccount() async {
    var createdAccount = AccountRequest(
        id: account.id,
        typeName: account.typeName,
        email: account.email,
        password: account.password,
        phone: account.phone,
        firstName: account.firstName,
        lastName: account.lastName,
        dateOfBirth: DateTime.parse(account.dateOfBirth),
        placeOfBirth: account.placeOfBirth,
        street: account.street,
        city: account.city,
        postalCode: account.postalCode,
        countryCode: account.countryCode);

    account.setDefaults();

    final response = await http
        .post(Uri.parse('$BACKEND_ADDRESS/register/member'),
            headers: {
              'Content-Type': 'application/json; charset=UTF-8',
            },
            body: jsonEncode(createdAccount.toJson()))
        .timeout(const Duration(seconds: 5),
            onTimeout: () => throw TimeoutException('Timeout'));

    if (response.statusCode == 201) {
      AccountData.instance.setAuthentication(
          Authentication.fromJson(json.decode(response.body)));
      await AccountData.instance.fetchAccount();
      await AccountData.instance.saveUserPrefs();
      if (AccountData.instance.getAccount().accountType ==
              AccountType.MEMBER.name ||
          AccountData.instance.getAccount().accountType ==
              AccountType.MEMBER_EMPLOYEE.name) {
        await DriverData.fetchAndSetDriver();
      } else {
        DriverData.instance?.resetDriver();
      }

      return createdAccount;
    } else {
      throw Exception(response.body.toString());
    }
  }

  AlertDialog _errorAlert(String error) {
    String errorText;
    String errorSplit;
    BackendException? backendException;
    final split = error.split('Exception: ');
    if (split.length > 1) {
      errorSplit = split.elementAt(1);
    } else {
      errorSplit = split.elementAt(0);
    }

    try {
      backendException = backendExceptionFromJson(errorSplit);
    } on Exception {
      errorText = 'Ein unerwarteter Fehler ist aufgetreten.';
    }

    switch (backendException?.errorCode) {
      case 7002:
        //password inadequate
        errorText = 'Das Passwort entspricht nicht den Mindestanforderungen.';
        break;
      case 9009:
        //all fields must be set
        errorText = 'Alle Felder müssen gesetzt sein.';
        break;
      case 9001:
        //driver license already exists
        errorText = 'Die gegebene Führerschein Id existiert bereits.';
      case 9002:
        //email already in use
        errorText = 'Die gegebene Email existiert bereits.';
        break;
      case 9003:
        //account type already exists for user
        errorText = 'Der gegebene Nutzer existiert bereits';
        break;
      default:
        errorText = 'Ein unerwarteter Fehler ist aufgetreten.';
        break;
    }

    return AlertDialog(
        title: Row(
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      children: [
        const Icon(
          Icons.error,
          color: Colors.red,
        ),
        const SizedBox(width: 10),
        Flexible(child: Text(errorText)),
      ],
    ));
  }

  void _registrationButtonPressed() async {
    if (_formKey.currentState!.validate() &&
        await _validateEmail(_emailController.text)) {
      _formKey.currentState?.save();
      _navigatePage();
    }
  }

  /// The `_navigatePage` function navigates to a new page and displays a loading
  /// indicator while waiting for a future to complete, then either shows an error
  /// dialog or navigates to the home page or dashboard based on the result.
  ///
  /// Returns:
  ///   The function `_navigatePage` returns a widget. The widget returned depends
  /// on the state of the `AsyncSnapshot` object received in the `builder` function
  /// of the `FutureBuilder`. If the connection state is `waiting`, a `Center`
  /// widget with a `CircularProgressIndicator` is returned. If there is an error in
  /// the snapshot, an error dialog is shown and a `FastlaneHomePage
  void _navigatePage() {
    Navigator.pop(context);
    Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => FutureBuilder(
                future: Future.wait([_createAccount()]),
                builder: (BuildContext context, AsyncSnapshot snapshot) {
                  if (snapshot.connectionState == ConnectionState.waiting) {
                    return const Center(
                      child: CircularProgressIndicator(),
                    );
                  } else {
                    if (snapshot.hasError) {
                      Future.delayed(
                          const Duration(milliseconds: 1),
                          () => showDialog(
                              context: context,
                              builder: (BuildContext context) {
                                return _errorAlert(snapshot.error.toString());
                              }));
                      html.window.history.pushState({}, '', '/');
                      return const FastlaneHomePage();
                    } else {
                      html.window.history.pushState({}, '', '/home');
                      return FastlaneDashboardNavigation();
                    }
                  }
                })));
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
                          Focus(
                            child: TextFormField(
                                controller: _emailController,
                                autovalidateMode:
                                    AutovalidateMode.onUserInteraction,
                                validator: (val) => _validationMsg,
                                onSaved: (value) {
                                  account.email = value!;
                                },
                                decoration: InputDecoration(
                                    labelText: 'Email',
                                    suffixIcon: _isChecking
                                        ? Transform.scale(
                                            scale: 0.5,
                                            child:
                                                const CircularProgressIndicator())
                                        : null)),
                            onFocusChange: (hasFocus) {
                              if (!hasFocus)
                                _validateEmail(_emailController.text);
                            },
                          ),
                          TextFormField(
                              key: _passKey,
                              obscureText: true,
                              validator: _validatePassword,
                              onSaved: (value) {
                                account.password = value!;
                              },
                              decoration:
                                  const InputDecoration(labelText: 'Passwort')),
                          const Text(
                            'Passwortanforderungen: \n '
                            '- Mindestens 8 Zeichen \n'
                            '- Ein Großbuchstabe \n'
                            '- Ein Kleinbuchstabe \n'
                            '- Eine Zahl \n'
                            '- Ein Sonderzeichen',
                            style: TextStyle(fontSize: 10),
                          ),
                          TextFormField(
                              key: _passRepeatKey,
                              obscureText: true,
                              onFieldSubmitted: (value) {
                                _registrationButtonPressed();
                              },
                              validator: _validateRepeatPassword,
                              decoration: const InputDecoration(
                                  labelText: 'Passwort bestätigen')),
                        ],
                      ),
                    ),
                    if (!MediaController.isSmallScreen(context))
                      const SizedBox(width: 50),
                    if (!MediaController.isSmallScreen(context))
                      const FastlaneCircularPercentIndicator(
                        percent: 1.0,
                      )
                  ],
                ),
                const SizedBox(height: 30),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    IconButton(
                      onPressed: () {
                        _formKey.currentState?.save();
                        pressedBack = true;
                        Navigator.pop(context);
                        showDialog(
                            context: context,
                            builder: (BuildContext context) {
                              return const RegisterFormTarif();
                            });
                      },
                      icon: const Icon(Icons.arrow_back_rounded),
                    ),
                    FastlaneElevatedButtonCyanIcon(
                        onPressed: _registrationButtonPressed,
                        label: 'Registrieren',
                        asset: 'assets/LogIn.svg'),
                  ],
                )
              ],
            )),
      ),
    );
  }
}
