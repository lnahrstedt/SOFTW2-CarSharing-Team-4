// To parse this JSON data, do
//
//     final authentication = authenticationFromJson(jsonString);

import 'dart:convert';

Authentication authenticationFromJson(String str) =>
    Authentication.fromJson(json.decode(str));

String authenticationToJson(Authentication data) => json.encode(data.toJson());

/// The `Authentication` class represents the authentication details of a user, including their account
/// ID, email, access token, and refresh token.
class Authentication {
  int accountId;
  String email;
  String accessToken;
  String refreshToken;

  Authentication({
    required this.accountId,
    required this.email,
    required this.accessToken,
    required this.refreshToken,
  });

  factory Authentication.fromJson(Map<String, dynamic> json) => Authentication(
        accountId: json["accountId"],
        email: json["email"],
        accessToken: json["access_token"],
        refreshToken: json["refresh_token"],
      );

  Map<String, dynamic> toJson() => {
        "accountId": accountId,
        "email": email,
        "access_token": accessToken,
        "refresh_token": refreshToken,
      };
}
