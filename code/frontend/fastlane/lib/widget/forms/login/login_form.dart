import 'package:email_validator/email_validator.dart';
import 'package:fastlane/control/mediaChecker.dart';
import 'package:fastlane/data/accountData.dart';
import 'package:fastlane/widget/dashboard/navigation.dart';
import 'package:fastlane/widget/forms/register/register_form_personal.dart';
import 'package:fastlane/widget/helper/mainScrollable.dart';
import 'package:fastlane/widget/helper/platformHelper.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:local_auth/local_auth.dart';
import 'package:universal_html/html.dart' as html;

import '../../../model/BackendException.dart';
import '../../../model/login.dart';
import '../../buttons/fastlane_elevated_cyan_icon.dart';
import '../../homePage/home_page.dart';

/// The `LoginForm` class is a stateful widget in Dart that represents a form for
/// user login.
class LoginForm extends StatefulWidget {
  const LoginForm({super.key});

  @override
  State<StatefulWidget> createState() => _LoginFormState();
}

/// The `_LoginFormState` class is responsible for handling the login form,
/// including form validation, authentication, and navigation to the dashboard.
class _LoginFormState extends State<LoginForm> {

  /// The function `authenticate()` uses biometrics authentication to authenticate
  /// the user and then navigates to the dashboard if the authentication is
  /// successful.
  ///
  /// Returns:
  ///   a Future<bool> value.
  Future authenticate() async {
    LocalAuthentication auth = LocalAuthentication();
    final bool isBiometricsAvailable = await auth.isDeviceSupported();

    if (!isBiometricsAvailable) return false;
    bool isAuthenticated = false;
    try {
      isAuthenticated = await auth.authenticate(
        localizedReason:
            "Scan your fingerprint (or face or whatever) to authenticate",
        options: const AuthenticationOptions(
          useErrorDialogs: true,
          stickyAuth: true,
        ),
      );
    } on PlatformException {
      return false;
    }

    if (isAuthenticated) {
      String email = "admin@flojc.com";
      String password = "123Password!";

      navigateToDashboard(email, password);
    }
  }

  /// The function `navigateToDashboard` is used to navigate to the dashboard page
  /// after logging in with the provided email and password.
  ///
  /// Args:
  ///   email (String): The email parameter is a string that represents the user's
  /// email address.
  ///   password (String): The password parameter is a string that represents the
  /// user's password.
  ///
  /// Returns:
  ///   The function `navigateToDashboard` returns a widget. The widget returned
  /// depends on the conditions inside the `builder` function of the
  /// `FutureBuilder`. If the `snapshot.connectionState` is
  /// `ConnectionState.waiting`, a `Center` widget with a
  /// `CircularProgressIndicator` is returned. If there is an error in the
  /// `snapshot`, an error dialog is shown and a `FastlaneHomePage
  void navigateToDashboard(String email, String password) {
    var login = Login(email: email, password: password);
    Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => FutureBuilder(
                future: Future.wait([AccountData.instance.login(login)]),
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

  /// The function `_errorAlert` creates an `AlertDialog` widget with an error
  /// message extracted from a given error string.
  ///
  /// Args:
  ///   error (String): The "error" parameter is a string that represents an error
  /// message or exception that occurred in the code.
  ///
  /// Returns:
  ///   an AlertDialog widget.
  AlertDialog _errorAlert(String error) {
    String errorText;
    String errorSplit;
    final split = error.split('Exception: ');
    if (split.length > 1) {
      errorSplit = split.elementAt(1);
    } else {
      errorSplit = split.elementAt(0);
    }
    try {
      final backendException = backendExceptionFromJson(errorSplit);
      errorText = backendException.description;
    } on Exception {
      errorText = errorSplit;
    }
    return AlertDialog(
        title: Row(
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      children: [
        const Icon(
          Icons.error,
          color: Colors.red,
        ),
        const SizedBox(width: 20),
        Text(errorText, style: const TextStyle(color: Colors.red)),
      ],
    ));
  }

  final _formKey = GlobalKey<FormState>();
  final _emailController = TextEditingController();

  final _passwordController = TextEditingController();

  String? _validateEmail(String? value) {
    if (value == null || value.isEmpty || !EmailValidator.validate(value)) {
      return 'Bitte valide Email eingeben';
    }
    return null;
  }

  String? _validatePassword(String? value) {
    if (value == null || value.isEmpty) {
      return 'Das Feld darf nicht leer sein';
    }
    return null;
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      shape: const RoundedRectangleBorder(
          borderRadius: BorderRadius.all(Radius.circular(32.0))),
      scrollable: true,
      title: const Text('Anmelden'),
      content: SizedBox(
        width: 600,
        height: 250,
        child: Form(
          key: _formKey,
          child: FastlaneScrollable(
            direction: Axis.vertical,
            child: Wrap(
              crossAxisAlignment: WrapCrossAlignment.end,
              direction: Axis.horizontal,
              runAlignment: WrapAlignment.start,
              spacing: 50,
              runSpacing: 10,
              clipBehavior: Clip.hardEdge,
              children: [
                Wrap(
                  direction: Axis.vertical,
                  spacing: 10,
                  runSpacing: 10,
                  children: [
                    Container(
                      padding: const EdgeInsets.all(8.0),
                      width: 250,
                      child: TextFormField(
                        validator: _validateEmail,
                        controller: _emailController,
                        decoration: const InputDecoration(labelText: 'Email'),
                      ),
                    ),
                    Container(
                      padding: const EdgeInsets.all(8.0),
                      width: 250,
                      child: TextFormField(
                        validator: _validatePassword,
                        controller: _passwordController,
                        onFieldSubmitted: (value) {
                          if (_formKey.currentState!.validate()) {
                            _formKey.currentState?.save();

                            var login = Login(
                                email: _emailController.text,
                                password: _passwordController.text);
                            Navigator.push(
                                context,
                                MaterialPageRoute(
                                    builder: (context) => FutureBuilder(
                                        future: Future.wait([
                                          AccountData.instance.login(login)
                                        ]),
                                        builder: (BuildContext context,
                                            AsyncSnapshot snapshot) {
                                          if (snapshot.connectionState ==
                                              ConnectionState.waiting) {
                                            return const Center(
                                              child:
                                                  CircularProgressIndicator(),
                                            );
                                          } else {
                                            if (snapshot.hasError) {
                                              Future.delayed(
                                                  const Duration(
                                                      milliseconds: 1),
                                                  () => showDialog(
                                                      context: context,
                                                      builder: (BuildContext
                                                          context) {
                                                        return _errorAlert(
                                                            snapshot.error
                                                                .toString());
                                                      }));
                                              html.window.history
                                                  .pushState({}, '', '/');
                                              return const FastlaneHomePage();
                                            } else {
                                              html.window.history
                                                  .pushState({}, '', '/home');
                                              return FastlaneDashboardNavigation();
                                            }
                                          }
                                        })));
                          }
                        },
                        obscureText: true,
                        decoration:
                            const InputDecoration(labelText: 'Passwort'),
                      ),
                    ),
                    Row(
                      children: [
                        FastlaneElevatedButtonCyanIcon(
                          onPressed: () {
                            if (_formKey.currentState!.validate()) {
                              _formKey.currentState?.save();
                              navigateToDashboard(_emailController.text,
                                  _passwordController.text);
                            }
                          },
                          label: 'Anmelden',
                          icon: Icons.login_rounded,
                        ),
                        Visibility(
                          visible: PlatformHelper.isMobile(),
                          child: Padding(
                            padding: const EdgeInsets.only(left: 27.5),
                            child: Center(
                              child: SizedBox(
                                height: 75,
                                width: 75,
                                child: IconButton.filled(
                                    onPressed: authenticate,
                                    icon: const Icon(Icons.fingerprint)),
                              ),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
                Wrap(
                  direction: Axis.vertical,
                  spacing: 20,
                  runSpacing: 10,
                  children: [
                    SizedBox(
                      width: 250.0,
                      child: Text(
                        'Noch keinen Account?',
                        softWrap: true,
                        maxLines: 3,
                        style: MediaController.isSmallScreen(context)
                            ? const TextStyle(
                                fontSize: 24, fontWeight: FontWeight.bold)
                            : const TextStyle(
                                fontSize: 40, fontWeight: FontWeight.bold),
                      ),
                    ),
                    const SizedBox(
                      width: 200.0,
                      child: Text(
                        'Registriere dich jetzt und werde Teil unserer Fastlane Community',
                        maxLines: 5,
                        softWrap: true,
                      ),
                    ),
                    FastlaneElevatedButtonCyanIcon(
                      onPressed: () {
                        Navigator.pop(context);
                        showDialog(
                            context: context,
                            builder: (BuildContext context) {
                              return const RegisterFormPersonal();
                            });
                      },
                      label: 'Registrieren',
                      icon: Icons.login_rounded,
                    )
                  ],
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
