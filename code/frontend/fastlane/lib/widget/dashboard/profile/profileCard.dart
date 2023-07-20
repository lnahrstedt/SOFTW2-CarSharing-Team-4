import 'dart:async';
import 'dart:convert';

import 'package:email_validator/email_validator.dart';
import 'package:fastlane/constants/addresses.dart';
import 'package:fastlane/constants/countryCode.dart';
import 'package:fastlane/constants/margin.dart';
import 'package:fastlane/constants/numbers.dart';
import 'package:fastlane/constants/padding.dart';
import 'package:fastlane/data/accountData.dart';
import 'package:fastlane/data/driverData.dart';
import 'package:fastlane/model/driver.dart';
import 'package:fastlane/widget/helper/mainCard.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:http/http.dart' as http;
import 'package:intl/intl.dart';

import '../../../constants/colors.dart';
import '../../../model/account.dart';
import '../../../model/country.dart';
import '../../helper/alertHelper.dart';

/// The `FastlaneDashboardProfileHeaderCard` class is a stateful widget that
/// represents a profile header card in a Fastlane dashboard, allowing users to
/// switch to an edit menu.
class FastlaneDashboardProfileHeaderCard extends StatefulWidget {
  const FastlaneDashboardProfileHeaderCard(
      {super.key, this.account, required this.switchEditMenu});

  final Account? account;
  final void Function() switchEditMenu;

  @override
  State<FastlaneDashboardProfileHeaderCard> createState() =>
      _FastlaneDashboardProfileHeaderCardState();
}

/// The `_FastlaneDashboardProfileHeaderCardState` class is a stateful widget that
/// displays a user's profile information and allows them to edit their profile.
class _FastlaneDashboardProfileHeaderCardState
    extends State<FastlaneDashboardProfileHeaderCard> {
  bool isHover = false;

  setHover(val) {
    setState(() {
      isHover = val;
    });
  }

  @override
  Widget build(BuildContext context) {
    Account account = AccountData.instance.getAccount();

    return FastlaneDashboardCard(
        title: "",
        height: DEFAULT_CARD_HEIGHT + 50,
        width: DEFAULT_FULLSCREEN_CARD_WIDTH,
        crossAxisAlignment: CrossAxisAlignment.center,
        padding: BIG_PADDING,
        children: [
          Center(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                const CircleAvatar(
                  backgroundImage: AssetImage('assets/user.png'),
                  backgroundColor: FLOJC_BLUE_ACCENT,
                  minRadius: 30,
                  maxRadius: 60,
                ),
                const SizedBox(
                  height: 15,
                ),
                Text(
                  utf8.decode(
                      "${account.user.firstName} ${account.user.lastName}"
                          .runes
                          .toList()),
                  style: const TextStyle(
                      fontSize: 24,
                      fontWeight: FontWeight.w600,
                      color: Colors.black),
                ),
                Text(
                  utf8.decode(account.email.runes.toList()),
                  style: const TextStyle(
                      fontSize: 16,
                      fontWeight: FontWeight.w400,
                      color: INBETWEEN_GRAY),
                ),
                const SizedBox(height: 30),
                InkWell(
                    onTap: widget.switchEditMenu,
                    onHover: (val) => setHover(val),
                    child: Container(
                      padding: const EdgeInsets.symmetric(
                          horizontal: 20, vertical: 10),
                      decoration: BoxDecoration(
                          color: isHover ? Colors.black : BUTTON_BEFORE_HOVER,
                          borderRadius:
                              const BorderRadius.all(Radius.circular(10))),
                      child: const Row(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          Text(
                            "Bearbeiten",
                            style: TextStyle(
                                color: Colors.white,
                                fontSize: 20,
                                fontWeight: FontWeight.w400),
                          ),
                          SizedBox(
                            width: 5,
                          ),
                          Icon(
                            Icons.create,
                            size: 20,
                            color: Colors.white,
                          )
                        ],
                      ),
                    )),
              ],
            ),
          ),
        ]);
  }
}

/// The `FastlaneDashboardProfileAttributes` class is a widget that displays various
/// profile attributes such as personal data, account information, and driver data
/// in a horizontal layout.
class FastlaneDashboardProfileAttributes extends StatelessWidget {
  const FastlaneDashboardProfileAttributes({super.key});

  @override
  Widget build(BuildContext context) {
    Account account = AccountData.instance.getAccount();
    User user = account.user;
    Address address = user.address;
    Driver? driver = DriverData.instance?.getDriver()!;
    return Wrap(
      direction: Axis.horizontal,
      children: [
        FastlaneDashboardFittedCard(
            title: "Persönliche Daten",
            crossAxisAlignment: CrossAxisAlignment.start,
            padding: BIG_PADDING,
            children: [
              SizedBox(
                width: DEFAULT_CARD_WIDTH * 1.255,
                child: Wrap(
                  crossAxisAlignment: WrapCrossAlignment.start,
                  direction: Axis.vertical,
                  children: [
                    FastlaneDashboardProfileTextLabel(
                        attribute: Icons.badge_outlined,
                        value: utf8.decode("${user.firstName} ${user.lastName}"
                            .runes
                            .toList())),
                    FastlaneDashboardProfileTextLabel(
                        attribute: Icons.baby_changing_station_outlined,
                        value: utf8.decode(user.placeOfBirth.runes.toList())),
                    FastlaneDashboardProfileTextLabel(
                        attribute: Icons.cake_outlined,
                        value:
                            DateFormat('dd.MM.yyyy').format(user.dateOfBirth)),
                    FastlaneDashboardProfileTextLabel(
                        attribute: Icons.add_road_outlined,
                        value: utf8.decode(address.street.runes.toList())),
                    FastlaneDashboardProfileTextLabel(
                        attribute: Icons.markunread_mailbox_outlined,
                        value: utf8.decode(address.postalCode.runes.toList())),
                    FastlaneDashboardProfileTextLabel(
                        attribute: Icons.location_city_outlined,
                        value: utf8.decode(address.city.runes.toList())),
                    FastlaneDashboardProfileTextLabel(
                        attribute: Icons.flag_outlined,
                        value: utf8.decode(address.country.runes.toList())),
                  ],
                ),
              ),
            ]),
        FastlaneDashboardFittedCard(
            title: "Account",
            crossAxisAlignment: CrossAxisAlignment.start,
            padding: BIG_PADDING,
            children: [
              SizedBox(
                width: DEFAULT_CARD_WIDTH * 1.255,
                child: Wrap(
                  crossAxisAlignment: WrapCrossAlignment.start,
                  direction: Axis.vertical,
                  children: [
                    FastlaneDashboardProfileTextLabel(
                        attribute: Icons.mail_outline_outlined,
                        value: utf8.decode(account.email.runes.toList())),
                    FastlaneDashboardProfileTextLabel(
                        attribute: Icons.phone_outlined,
                        value: utf8.decode(account.phone.runes.toList())),
                  ],
                ),
              ),
            ]),
        if (driver != null)
          FastlaneDashboardFittedCard(
              title: "Fahrerdaten",
              crossAxisAlignment: CrossAxisAlignment.start,
              padding: BIG_PADDING,
              children: [
                SizedBox(
                  width: DEFAULT_CARD_WIDTH * 1.255,
                  child: Wrap(
                    crossAxisAlignment: WrapCrossAlignment.start,
                    direction: Axis.vertical,
                    children: [
                      FastlaneDashboardProfileTextLabel(
                        attribute: Icons.remember_me_outlined,
                        value: utf8.decode(driver.licenseId.runes.toList()),
                      ),
                      FastlaneDashboardProfileTextLabel(
                        attribute: Icons.category_outlined,
                        value: utf8.decode(driver.fareType.name.runes.toList()),
                      ),
                    ],
                  ),
                ),
              ]),
      ],
    );
  }
}

/// The class FastlaneDashboardProfileEdit is a StatefulWidget used for editing
/// profiles in a Fastlane dashboard.
class FastlaneDashboardProfileEdit extends StatefulWidget {
  const FastlaneDashboardProfileEdit({super.key});

  @override
  State<FastlaneDashboardProfileEdit> createState() =>
      _FastlaneDashboardProfileEditState();
}

/// The `_FastlaneDashboardProfileEditState` class builds a widget that displays
/// different profile editing options based on the user's account type.
class _FastlaneDashboardProfileEditState
    extends State<FastlaneDashboardProfileEdit> {
  @override
  Widget build(BuildContext context) {
    return Wrap(
      direction: Axis.horizontal,
      children: [
        const FastlaneDashboardProfileEditUser(),
        const FastlaneDashboardProfileEditAccount(),
        if (!AccountData.instance.isEmployee())
          const FastlaneDashboardProfileEditDriver(),
      ],
    );
  }
}

/// The class FastlaneDashboardProfileEditDriver is a StatefulWidget used for
/// editing driver profiles in a Fastlane dashboard.
class FastlaneDashboardProfileEditDriver extends StatefulWidget {
  const FastlaneDashboardProfileEditDriver({super.key});

  @override
  State<StatefulWidget> createState() =>
      _FastlaneDashboardProfileEditDriverState();
}

/// The `_FastlaneDashboardProfileEditDriverState` class is a stateful widget that
/// allows users to edit driver profile information, including their license ID and
/// fare type.
class _FastlaneDashboardProfileEditDriverState
    extends State<FastlaneDashboardProfileEditDriver> {
  Driver? driver = DriverData.instance?.driver;

  List<DropdownMenuItem<String>> tariffEntries = [
    const DropdownMenuItem(
      value: "JUNIOR",
      child: Text("JUNIOR"),
    ),
    const DropdownMenuItem(value: "COMFORT", child: Text("COMFORT")),
    const DropdownMenuItem(value: "PREMIUM", child: Text("PREMIUM"))
  ];
  String? selectedTariff;

  final _licenseRegex = RegExp(r'\w+');
  bool _isChecking = false;
  dynamic _validationMsg;
  final _formKey = GlobalKey<FormState>();

  final licenseIdController = TextEditingController();
  final fareTypeController = TextEditingController();

  /// The function `_validateLicense` checks if a given license value is valid and
  /// does not already exist.
  ///
  /// Args:
  ///   value (String): The value parameter is a string that represents a license
  /// ID.
  ///
  /// Returns:
  ///   a `Future<dynamic>`.
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

  /// The function `doesLicenseExist` checks if a license exists by making an HTTP
  /// GET request and returns a boolean value indicating the result.
  ///
  /// Args:
  ///   licenseId (String): The `licenseId` parameter is a string that represents
  /// the ID of a license. It is used to check if a license exists in the system.
  ///
  /// Returns:
  ///   The function `doesLicenseExist` returns a `Future<bool>`.
  /// The function `doesLicenseExist` checks if a license exists by making an HTTP
  /// GET request and returns a boolean value indicating the result.
  ///
  /// Args:
  ///   licenseId (String): The `licenseId` parameter is a string that represents
  /// the ID of a license. It is used to check if a license exists in the system.
  ///
  /// Returns:
  ///   The function `doesLicenseExist` returns a `Future<bool>`.
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
      if (licenseIdController.text == driver?.licenseId) {
        return false;
      }
      return true;
    } else if (response.statusCode == 401) {
      throw Exception('[${response.statusCode}] Unauthorized');
    } else {
      throw Exception('[${response.statusCode}] Backend nicht erreichbar');
    }
  }

  @override
  void initState() {
    selectedTariff = driver?.fareType.name;
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return FastlaneDashboardFittedCard(
      title: "Fahrerdaten",
      crossAxisAlignment: CrossAxisAlignment.start,
      padding: BIG_PADDING,
      children: [
        SizedBox(
          width: DEFAULT_CARD_WIDTH * 1.255,
          child: Form(
            key: _formKey,
            child: Wrap(
              crossAxisAlignment: WrapCrossAlignment.start,
              children: [
                Focus(
                  child: FastlaneDashboardProfileTextField(
                    controller: licenseIdController,
                    attribute: "Führerschein Id",
                    hideInput: false,
                    value: utf8.decode(driver!.licenseId.runes.toList()),
                    validator: (val) => _validationMsg,
                    decoration: InputDecoration(
                        labelText: 'Führerschein Id',
                        border: const OutlineInputBorder(),
                        suffixIcon: _isChecking
                            ? Transform.scale(
                                scale: 0.5,
                                child: const CircularProgressIndicator())
                            : null),
                  ),
                  onFocusChange: (hasFocus) {
                    if (!hasFocus) _validateLicense(licenseIdController.text);
                  },
                ),
                Container(
                  padding:
                      const EdgeInsets.symmetric(horizontal: 10, vertical: 5),
                  decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.circular(10)),
                  child: DropdownButtonFormField(
                      decoration:
                          const InputDecoration(border: OutlineInputBorder()),
                      items: tariffEntries,
                      value: selectedTariff,
                      onChanged: (String? value) {
                        setState(() {
                          selectedTariff = value!;
                        });
                      }),
                ),
                FastlaneDashboardProfileSaveButton(save: () async {
                  if (_formKey.currentState!.validate() &&
                      await _validateLicense(licenseIdController.text)) {
                    Driver patchedDriver = Driver(
                        id: driver!.id,
                        licenseId: licenseIdController.text,
                        user: driver!.user,
                        fareType: DriverData.instance!
                            .fareTypeFromName(selectedTariff!));
                    try {
                      DriverData.instance!
                          .patchDriver(driver!.id, patchedDriver);
                      AlertHelper.show(
                          context,
                          const Icon(Icons.check, color: Colors.green),
                          "Die Änderungen wurden erfolgreich gespeichert",
                          const Duration(seconds: 3));
                    } catch (e) {
                      AlertHelper.show(
                          context,
                          const Icon(Icons.error, color: Colors.red),
                          "Die Änderungen konnten nicht gespeichert werden. Bitte versuche es später erneut.",
                          const Duration(seconds: 3));
                    }
                  }
                })
              ],
            ),
          ),
        )
      ],
    );
  }
}

/// The class FastlaneDashboardProfileEditUser is a StatefulWidget in Dart that
/// represents a profile editing screen in a Fastlane dashboard.
class FastlaneDashboardProfileEditUser extends StatefulWidget {
  const FastlaneDashboardProfileEditUser({super.key});

  @override
  State<FastlaneDashboardProfileEditUser> createState() =>
      _FastlaneDashboardProfileEditUserState();
}

/// The `_FastlaneDashboardProfileEditUserState` class is a stateful widget that
/// allows users to edit their personal information in a fastlane dashboard.
class _FastlaneDashboardProfileEditUserState
    extends State<FastlaneDashboardProfileEditUser> {
  final _formKey = GlobalKey<FormState>();

  late List<Country> countries;

  Country? _pickedCountry;
  User user = AccountData.instance.getAccount().user;
  late DateTime _pickedDate;

  TextEditingController firstNameController = TextEditingController();
  TextEditingController lastNameController = TextEditingController();
  TextEditingController placeOfBirthController = TextEditingController();
  TextEditingController dateOfBirthController = TextEditingController();
  TextEditingController streetController = TextEditingController();
  TextEditingController postalCodeController = TextEditingController();
  TextEditingController cityController = TextEditingController();
  TextEditingController countryController = TextEditingController();

  final _specialCharRegex = RegExp(r'[^;:\\|{}\]\[]');

  String? _validateField(String? value) {
    if (value?.trim() == null || value!.trim().isEmpty) {
      return 'Feld darf nicht leer sein';
    } else if (!_specialCharRegex.hasMatch(value)) {
      return 'Feld darf keine Sonderzeichen enthalten';
    } else {
      return null;
    }
  }

  String? _validateBirthDate(String? value) {
    if (value?.trim() == null || value!.trim().isEmpty) {
      return 'Feld darf nicht leer sein';
    }
    return null;
  }

  List<DropdownMenuItem<Country>> _countryEntries =
      <DropdownMenuItem<Country>>[];

  /// The function loads a list of countries from a JSON file, checks if the user's
  /// country matches any of the countries in the list, and adds the countries to a
  /// dropdown menu.
  Future<void> loadCountries() async {
    countries =
        countryFromJson(await rootBundle.loadString('assets/countries.json'));
    for (final Country country in countries) {
      if (utf8.decode(AccountData.instance
              .getAccount()
              .user
              .address
              .country
              .runes
              .toList()) ==
          country.name) {
        _pickedCountry = country;
      }
      _countryEntries.add(
          DropdownMenuItem<Country>(value: country, child: Text(country.name)));
    }
    setState(() {});
  }

  @override
  void initState() {
    _pickedDate = user.dateOfBirth;
    loadCountries();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    String dateOfBirth = DateFormat("dd.MM.yyyy").format(user.dateOfBirth);

    return FastlaneDashboardFittedCard(
        title: "Persönliche Daten",
        crossAxisAlignment: CrossAxisAlignment.start,
        padding: BIG_PADDING,
        children: [
          SizedBox(
            width: DEFAULT_CARD_WIDTH * 1.255,
            child: Form(
              key: _formKey,
              child: Wrap(
                crossAxisAlignment: WrapCrossAlignment.start,
                children: [
                  FastlaneDashboardProfileTextField(
                    controller: firstNameController,
                    attribute: "Vorname",
                    hideInput: false,
                    value: utf8.decode(user.firstName.runes.toList()),
                    validator: _validateField,
                  ),
                  FastlaneDashboardProfileTextField(
                    controller: lastNameController,
                    attribute: "Nachname",
                    hideInput: false,
                    value: utf8.decode(user.lastName.runes.toList()),
                    validator: _validateField,
                  ),
                  FastlaneDashboardProfileTextField(
                    controller: placeOfBirthController,
                    attribute: "Geburtsort",
                    hideInput: false,
                    value: utf8.decode(user.placeOfBirth.runes.toList()),
                    validator: _validateField,
                  ),
                  FastlaneDashboardProfileTextField(
                    controller: dateOfBirthController,
                    attribute: "Geburtsdatum",
                    hideInput: false,
                    value: dateOfBirth,
                    validator: _validateBirthDate,
                    decoration: const InputDecoration(
                        border: OutlineInputBorder(),
                        labelText: 'Geburtsdatum',
                        suffixIcon: Icon(Icons.calendar_month)),
                    readOnly: true,
                    onTap: () async {
                      DateTime? pickedDate = await showDatePicker(
                          cancelText: "Abbrechen",
                          confirmText: "OK",
                          context: context,
                          initialDate: DateTime.now()
                              .subtract(const Duration(days: 365 * 18)),
                          firstDate: DateTime.now()
                              .subtract(const Duration(days: 365 * 100)),
                          lastDate: DateTime.now()
                              .subtract(const Duration(days: 365 * 18)),
                          builder: (BuildContext context, Widget? child) {
                            return Theme(
                              data: ThemeData.light().copyWith(
                                  colorScheme: const ColorScheme.light(
                                      primary: FLOJC_BLUE_DARKER),
                                  buttonTheme: const ButtonThemeData(
                                      textTheme: ButtonTextTheme.accent)),
                              child: child!,
                            );
                          });

                      if (pickedDate != null) {
                        _pickedDate = pickedDate;
                        String formattedDate =
                            DateFormat('dd.MM.yyyy').format(pickedDate);
                        setState(() {
                          dateOfBirthController.text =
                              DateFormat('dd.MM.yyyy').format(pickedDate);
                        });
                      }
                    },
                  ),
                  FastlaneDashboardProfileTextField(
                    controller: streetController,
                    attribute: "Straße",
                    hideInput: false,
                    value: utf8.decode(user.address.street.runes.toList()),
                    validator: _validateField,
                  ),
                  FastlaneDashboardProfileTextField(
                    controller: postalCodeController,
                    attribute: "Postleitzahl",
                    hideInput: false,
                    value: utf8.decode(user.address.postalCode.runes.toList()),
                    validator: _validateField,
                  ),
                  FastlaneDashboardProfileTextField(
                    controller: cityController,
                    attribute: "Stadt",
                    hideInput: false,
                    value: utf8.decode(user.address.city.runes.toList()),
                    validator: _validateField,
                  ),
                  Container(
                    padding:
                        const EdgeInsets.symmetric(horizontal: 10, vertical: 5),
                    decoration: BoxDecoration(
                        color: Colors.white,
                        borderRadius: BorderRadius.circular(10)),
                    child: DropdownButtonFormField(
                      decoration:
                          const InputDecoration(border: OutlineInputBorder()),
                      value: _pickedCountry,
                      items: _countryEntries,
                      onChanged: (Country? country) {
                        setState(() {
                          countryController.text = country!.name;
                          countryCodeSaver = country.code;
                          _pickedCountry = country;
                        });
                      },
                    ),
                  ),
                  FastlaneDashboardProfileSaveButton(save: () {
                    if (_formKey.currentState!.validate()) {
                      User patchedUser = User(
                          id: user.id,
                          firstName: firstNameController.text,
                          lastName: lastNameController.text,
                          dateOfBirth: _pickedDate,
                          placeOfBirth: placeOfBirthController.text,
                          address: Address(
                              street: streetController.text,
                              postalCode: postalCodeController.text,
                              city: cityController.text,
                              country: _pickedCountry!.code));
                      try {
                        AccountData.instance.patchUser(user.id, patchedUser);
                        AlertHelper.show(
                            context,
                            const Icon(Icons.check, color: Colors.green),
                            "Die Änderungen wurden erfolgreich gespeichert",
                            const Duration(seconds: 3));
                      } catch (e) {
                        AlertHelper.show(
                            context,
                            const Icon(Icons.error, color: Colors.red),
                            "Die Änderungen konnten nicht gespeichert werden. Bitte versuche es später erneut.",
                            const Duration(seconds: 3));
                      }
                    }
                  })
                ],
              ),
            ),
          ),
        ]);
  }
}

class FastlaneDashboardProfileEditAccount extends StatefulWidget {
  const FastlaneDashboardProfileEditAccount({super.key});

  @override
  State<FastlaneDashboardProfileEditAccount> createState() =>
      _FastlaneDashboardProfileEditAccountState();
}

class _FastlaneDashboardProfileEditAccountState
    extends State<FastlaneDashboardProfileEditAccount> {
  Account account = AccountData.instance.getAccount();

  TextEditingController emailController = TextEditingController();
  TextEditingController phoneController = TextEditingController();
  TextEditingController passwordController = TextEditingController();
  TextEditingController passwordApplyController = TextEditingController();

  final _formKey = GlobalKey<FormState>();

  final _passwordRegex =
      RegExp(r'^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z\d]).{8,}$');
  bool _isChecking = false;
  dynamic _validationMsg;
  final _phoneRegex = RegExp(r'^[+]?\d+$');

  String? _validatePhone(String? value) {
    if (value?.trim() == null || value!.trim().isEmpty) {
      return 'Feld darf nicht leer sein';
    } else if (!_phoneRegex.hasMatch(value)) {
      return 'Bitte valide Telefonnummer eingeben';
    }
    return null;
  }

  String? _validatePassword(String? value) {
    if (value!.isNotEmpty && !_passwordRegex.hasMatch(value)) {
      return 'Bitte Passwortanforderungen beachten';
    }
    return null;
  }

  String? _validateRepeatPassword(String? value) {
    if (value!.isNotEmpty &&
        passwordController.text != passwordApplyController.text) {
      return 'Muss dem Passwort entsprechen';
    }
    return null;
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
      if (emailController.text == account.email) {
        return false;
      }
      return true;
    } else if (response.statusCode == 401) {
      throw Exception('[${response.statusCode}] Unauthorized');
    } else {
      throw Exception('[${response.statusCode}] Backend nicht erreichbar');
    }
  }

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

  @override
  Widget build(BuildContext context) {
    return FastlaneDashboardFittedCard(
        title: "Account",
        crossAxisAlignment: CrossAxisAlignment.start,
        padding: BIG_PADDING,
        children: [
          SizedBox(
            width: DEFAULT_CARD_WIDTH * 1.255,
            child: Form(
              key: _formKey,
              child: Wrap(
                crossAxisAlignment: WrapCrossAlignment.start,
                children: [
                  Focus(
                    child: FastlaneDashboardProfileTextField(
                      controller: emailController,
                      autovalidateMode: AutovalidateMode.onUserInteraction,
                      attribute: "E-Mail",
                      hideInput: false,
                      value: utf8.decode(account.email.runes.toList()),
                      validator: (val) => _validationMsg,
                      decoration: InputDecoration(
                          border: const OutlineInputBorder(),
                          labelText: 'Email',
                          suffixIcon: _isChecking
                              ? Transform.scale(
                                  scale: 0.5,
                                  child: const CircularProgressIndicator())
                              : null),
                    ),
                    onFocusChange: (hasFocus) {
                      if (!hasFocus) _validateEmail(emailController.text);
                    },
                  ),
                  FastlaneDashboardProfileTextField(
                    controller: phoneController,
                    attribute: "Telefon",
                    hideInput: false,
                    value: utf8.decode(account.phone.runes.toList()),
                    validator: _validatePhone,
                  ),
                  FastlaneDashboardProfileTextField(
                    controller: passwordController,
                    attribute: "Passwort",
                    hideInput: true,
                    value: "",
                    validator: _validatePassword,
                  ),
                  FastlaneDashboardProfileTextField(
                    controller: passwordApplyController,
                    attribute: "Passwort bestätigen",
                    hideInput: true,
                    value: "",
                    validator: _validateRepeatPassword,
                  ),
                  const SizedBox(
                    height: 30,
                  ),
                  FastlaneDashboardProfileSaveButton(save: () async {
                    if (_formKey.currentState!.validate() &&
                        await _validateEmail(emailController.text)) {
                      Account patchedAccount = Account(
                          id: account.id,
                          email: emailController.text,
                          phone: phoneController.text,
                          password: passwordController.text == ""
                              ? account.password
                              : passwordController.text,
                          user: account.user,
                          accountType: account.accountType);
                      try {
                        await AccountData.instance
                            .patchAccount(account.id, patchedAccount);
                        AlertHelper.show(
                            context,
                            const Icon(Icons.check, color: Colors.green),
                            "Die Änderungen wurden erfolgreich gespeichert",
                            const Duration(seconds: 3));
                      } catch (e) {
                        AlertHelper.show(
                            context,
                            const Icon(Icons.error, color: Colors.red),
                            "Die Änderungen konnten nicht gespeichert werden. Bitte versuche es später erneut.",
                            const Duration(seconds: 3));
                      }
                    }
                  })
                ],
              ),
            ),
          ),
        ]);
  }
}

/// The `FastlaneDashboardProfileTextLabel` class is a widget that displays a label
/// with an icon and a text value.
class FastlaneDashboardProfileTextLabel extends StatelessWidget {
  final IconData attribute;
  final String value;

  const FastlaneDashboardProfileTextLabel(
      {super.key, required this.attribute, required this.value});

  @override
  Widget build(BuildContext context) {
    return Container(
        margin: const EdgeInsets.only(top: 2.5, bottom: 2.5, left: 2.5),
        width: DEFAULT_CARD_WIDTH * 1.2,
        padding: const EdgeInsets.only(right: 30, top: 10, bottom: 10),
        // decoration: BoxDecoration(
        //   borderRadius: const BorderRadius.all(Radius.circular(7.5)),
        //       border: Border.all(color: INBETWEEN_GRAY, width: 0.5),
        // ),
        child: Row(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Icon(attribute, color: Colors.black),
              const SizedBox(width: 15),
              Text(value,
                  style: const TextStyle(color: INBETWEEN_GRAY, fontSize: 18)),
            ]));
  }
}

/// The `FastlaneDashboardProfileSaveButton` class is a stateful widget that
/// represents a save button in a dashboard profile, and it takes a `save` function
/// as a required parameter.
class FastlaneDashboardProfileSaveButton extends StatefulWidget {
  const FastlaneDashboardProfileSaveButton({super.key, required this.save});

  final Function save;

  @override
  State<FastlaneDashboardProfileSaveButton> createState() =>
      _FastlaneDashboardProfileSaveButtonState();
}

/// The `_FastlaneDashboardProfileSaveButtonState` class is a stateful widget that
/// represents a save button with hover functionality in a Fastlane dashboard
/// profile.
class _FastlaneDashboardProfileSaveButtonState
    extends State<FastlaneDashboardProfileSaveButton> {
  bool isHovering = false;

  setHover(val) {
    setState(() {
      isHovering = val;
    });
  }

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: () {
        widget.save.call();
      },
      onHover: (val) => setHover(val),
      child: Container(
        margin: const EdgeInsets.symmetric(vertical: 5, horizontal: 10),
        padding: MIDDLE_PADDING,
        width: DEFAULT_CARD_WIDTH * 1.25,
        decoration: BoxDecoration(
          color: isHovering ? Colors.black : BUTTON_BEFORE_HOVER,
          borderRadius: const BorderRadius.all(Radius.circular(10)),
        ),
        child: const Wrap(
          direction: Axis.horizontal,
          crossAxisAlignment: WrapCrossAlignment.center,
          spacing: 10,
          alignment: WrapAlignment.center,
          children: [
            Icon(
              Icons.save_alt_outlined,
              color: Colors.white,
              size: 20,
            ),
            Text(
              "Speichern",
              style: TextStyle(
                  color: Colors.white,
                  fontSize: 20,
                  fontWeight: FontWeight.w400),
            )
          ],
        ),
      ),
    );
  }
}

/// The `FastlaneDashboardProfileTextField` class is a stateful widget in Dart that
/// represents a text field with various customizable properties.
class FastlaneDashboardProfileTextField extends StatefulWidget {
  const FastlaneDashboardProfileTextField(
      {super.key,
      required this.controller,
      required this.attribute,
      required this.hideInput,
      required this.value,
      this.validator,
      this.decoration,
      this.onTap,
      this.autovalidateMode,
      this.readOnly});

  final TextEditingController controller;
  final String attribute;
  final String value;
  final bool hideInput;
  final String? Function(String?)? validator;
  final InputDecoration? decoration;
  final Function? onTap;
  final AutovalidateMode? autovalidateMode;
  final bool? readOnly;

  @override
  State<FastlaneDashboardProfileTextField> createState() =>
      _FastlaneDashboardProfileTextFieldState();
}

/// The `_FastlaneDashboardProfileTextFieldState` class is a stateful widget that
/// initializes the text field with a given value and builds a container with a text
/// form field.
class _FastlaneDashboardProfileTextFieldState
    extends State<FastlaneDashboardProfileTextField> {
  @override
  void initState() {
    widget.controller.text = widget.value;
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: MIDDLE_MARGIN,
      child: TextFormField(
          onTap: () => widget.onTap?.call(),
          autovalidateMode: widget.autovalidateMode,
          readOnly: widget.readOnly == null ? false : widget.readOnly!,
          controller: widget.controller,
          obscureText: widget.hideInput,
          validator: widget.validator,
          decoration: widget.decoration ??
              InputDecoration(
                border: const OutlineInputBorder(),
                labelText: widget.attribute,
              )),
    );
  }
}
