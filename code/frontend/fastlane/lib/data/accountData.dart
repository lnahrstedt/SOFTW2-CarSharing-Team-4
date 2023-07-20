import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:fastlane/constants/accountType.dart';
import 'package:fastlane/constants/addresses.dart';
import 'package:fastlane/control/NavigationController.dart';
import 'package:fastlane/widget/homePage/home_page.dart';
import 'package:http/http.dart' as http;
import 'package:intl/intl.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:universal_html/html.dart' as html;

import '../model/account.dart';
import '../model/authentication.dart';
import '../model/login.dart';
import 'driverData.dart';


/// The `AccountData` class is responsible for managing user account data, including authentication,
/// login/logout functionality, fetching and saving user preferences, and performing account and user
/// updates.
class AccountData {
  static AccountData? _instance;
  late Authentication _authentication;
  Account? _account;
  var timeout = false;

  AccountData._privateConstructor();

  /// The function fetches an account using an HTTP GET request and returns it as a Future.
  /// 
  /// Returns:
  ///   The function `fetchAccount()` returns a `Future<Account>`.
  Future<Account> fetchAccount() async {
    final response = await http.get(
        Uri.parse("$BACKEND_ADDRESS/account/id/${_authentication.accountId}"),
        headers: {
          'Authorization': 'Bearer ${_authentication.accessToken}',
        }).timeout(const Duration(seconds: 5),
        onTimeout: () => throw TimeoutException('Timeout'));

    if (response.statusCode == 200) {
      _account = Account.fromJson(json.decode(response.body));
      return Account.fromJson(json.decode(response.body));
    } else {
      throw Exception(response.body.toString());
    }
  }

  void setAuthentication(Authentication authentication) {
    _authentication = authentication;
  }

  /// The function performs a login request, handles the response, and returns the authentication data
  /// if successful.
  /// 
  /// Args:
  ///   login (Login): The `login` parameter is an object of type `Login` which contains the necessary
  /// information for authentication. It is used to create a JSON payload for the login request.
  /// 
  /// Returns:
  ///   a `Future<Authentication>`.
  Future<Authentication> login(Login login) async {
    final response = await http
        .post(Uri.parse('$BACKEND_ADDRESS/auth/login'),
            headers: <String, String>{
              'Content-Type': 'application/json; charset=UTF-8',
            },
            body: jsonEncode(login.toJson()))
        .timeout(const Duration(seconds: 5),
            onTimeout: () => throw TimeoutException('Timeout'));

    if (response.statusCode == 200) {
      Authentication authentication =
          Authentication.fromJson(jsonDecode(response.body));
      setAuthentication(authentication);
      await fetchAccount();

      if (!AccountData.instance.isEmployee()) {
        await DriverData.fetchAndSetDriver();
      } else {
        DriverData.instance?.resetDriver();
      }

      await saveUserPrefs();

      return authentication;
    } else {
      throw Exception('Fehler beim Login');
    }
  }

  /// The function `logoutAccount` sends a POST request to the server to log out the user, and if
  /// successful, it updates the browser history, navigates to the home page, and deletes user
  /// preferences.
  /// 
  /// Args:
  ///   context: The `context` parameter is typically used in Flutter applications to provide access to
  /// the current application's state and resources. It is commonly used to navigate between screens,
  /// access theme data, and retrieve localized strings.
  void logoutAccount(context) async {
    final response = await http.post(Uri.parse("$BACKEND_ADDRESS/auth/logout"));

    if (response.statusCode == 200) {
      html.window.history.pushState({}, '', '/');
      FastlaneNavigationController.push(const FastlaneHomePage(), context);
      await _deleteUserPrefs();
    } else {
      throw Exception(response.body.toString());
    }
  }

  /// The function saves user preferences, including authentication and account information, using the
  /// SharedPreferences package in Dart.
  Future<void> saveUserPrefs() async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    await prefs.setBool('SHARED_LOGGED', true);
    await prefs.setString(
        'AUTHENTICATION', jsonEncode(_authentication.toJson()));
    await prefs.setString('ACCOUNT', jsonEncode(_account!.toJson()));
  }

  /// The function `_deleteUserPrefs` deletes specific keys from the SharedPreferences.
  Future<void> _deleteUserPrefs() async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    await prefs.remove('SHARED_LOGGED');
    await prefs.remove('AUTHENTICATION');
    await prefs.remove('ACCOUNT');
  }

  /// The function checks if a user is logged in by retrieving data from shared preferences and performs
  /// additional actions based on the user's role.
  Future<void> checkUserIsLogged() async {
    final prefs = await SharedPreferences.getInstance();
    if (prefs.getBool('SHARED_LOGGED') != null &&
        prefs.getBool('SHARED_LOGGED')!) {
      _authentication = Authentication.fromJson(
          jsonDecode(prefs.getString('AUTHENTICATION')!));
      _account = Account.fromJson(jsonDecode(prefs.getString('ACCOUNT')!));
      if (!isEmployee()) {
        await DriverData.fetchAndSetDriver();
      } else {
        DriverData.instance?.resetDriver();
      }
    }
  }

  /// The function `patchAccount` sends a PATCH request to update an account with the provided ID and
  /// account object.
  /// 
  /// Args:
  ///   id (int): The `id` parameter is an integer that represents the unique identifier of the account
  /// that needs to be patched or updated.
  ///   account (Account): The "account" parameter is an instance of the "Account" class, which contains
  /// the following properties:
  /// 
  /// Returns:
  ///   a `Future<int>`.
  Future<int> patchAccount(int id, Account account) async {
    final Uri uri = Uri.parse("$BACKEND_ADDRESS/account/${_account?.email}/$id");

    final Map<String, String> headers = {
      HttpHeaders.contentTypeHeader: 'application/json; charset=UTF-8',
      HttpHeaders.authorizationHeader: 'Bearer ${_authentication.accessToken}'
    };

    final Map<String, String> body = {
      "email": account.email,
      "password": account.password,
      "phone": account.phone,
    };

    final response =
        await http.patch(uri, headers: headers, body: json.encode(body));

    if (response.statusCode == 200) {
      _authentication =
          Authentication.fromJson(json.decode(response.body.toString()));
      await fetchAccount();
      await saveUserPrefs();
      return response.statusCode;
    } else {
      throw Exception(response.body.toString());
    }
  }

  /// The function `patchUser` sends a PATCH request to update a user's information with the provided
  /// `id` and `user` object.
  /// 
  /// Args:
  ///   id (int): The `id` parameter is the unique identifier of the user that you want to update. It is
  /// used to construct the URL for the PATCH request.
  ///   user (User): The "user" parameter is an instance of the "User" class, which contains the
  /// following properties:
  Future<void> patchUser(int id, User user) async {
    final Uri uri = Uri.parse("$BACKEND_ADDRESS/user/$id");

    final Map<String, String> headers = {
      HttpHeaders.contentTypeHeader: 'application/json; charset=UTF-8',
      HttpHeaders.authorizationHeader: 'Bearer ${_authentication.accessToken}'
    };

    final Map<String, String> body = {
      "firstName": user.firstName,
      "lastName": user.lastName,
      "dateOfBirth": DateFormat('yyyy-MM-dd').format(user.dateOfBirth),
      "placeOfBirth": user.placeOfBirth,
      "street": user.address.street,
      "postalCode": user.address.postalCode,
      "city": user.address.city,
      "countryCode": user.address.country,
    };

    final response =
        await http.patch(uri, headers: headers, body: json.encode(body));

    if (response.statusCode == 200) {
      await fetchAccount();
    } else {
      throw Exception(response.body.toString());
    }
  }

  Authentication getAuthentication() {
    return _authentication;
  }

  /// The `static AccountData get instance` getter method is implementing the Singleton design pattern.
  /// It ensures that only one instance of the `AccountData` class is created and provides a global
  /// point of access to that instance.
  static AccountData get instance {
    _instance ??= AccountData._privateConstructor();
    return _instance!;
  }

  bool isLoggedIn() {
    return _account != null;
  }

  bool isEmployee() {
    return (_account!.accountType == AccountType.EMPLOYEE.name ||
        _account!.accountType == AccountType.ADMIN.name);
  }

  Account getAccount() {
    return _account!;
  }

  void setAccount(Account account) {
    _account = account;
  }
}
