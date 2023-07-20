// To parse this JSON data, do
//
//     final account = accountFromJson(jsonString);

import 'dart:convert';

AccountRequest accountFromJson(String str) =>
    AccountRequest.fromJson(json.decode(str));

String accountToJson(AccountRequest data) => json.encode(data.toJson());

/// The `AccountRequest` class represents a request for creating a new account, with various properties
/// such as email, password, phone number, name, date of birth, and address.
class AccountRequest {
  String id;
  String typeName;
  String email;
  String password;
  String phone;
  String firstName;
  String lastName;
  DateTime dateOfBirth;
  String placeOfBirth;
  String street;
  String city;
  String postalCode;
  String countryCode;

  AccountRequest({
    required this.id,
    required this.typeName,
    required this.email,
    required this.password,
    required this.phone,
    required this.firstName,
    required this.lastName,
    required this.dateOfBirth,
    required this.placeOfBirth,
    required this.street,
    required this.city,
    required this.postalCode,
    required this.countryCode,
  });

  factory AccountRequest.fromJson(Map<String, dynamic> json) => AccountRequest(
        id: json["id"],
        typeName: json["typeName"],
        email: json["email"],
        password: json["password"],
        phone: json["phone"],
        firstName: json["firstName"],
        lastName: json["lastName"],
        dateOfBirth: DateTime.parse(json["dateOfBirth"]),
        placeOfBirth: json["placeOfBirth"],
        street: json["street"],
        city: json["city"],
        postalCode: json["postalCode"],
        countryCode: json["countryCode"],
      );

  Map<String, dynamic> toJson() => {
        "id": id,
        "typeName": typeName,
        "email": email,
        "password": password,
        "phone": phone,
        "firstName": firstName,
        "lastName": lastName,
        "dateOfBirth":
            "${dateOfBirth.year.toString().padLeft(4, '0')}-${dateOfBirth.month.toString().padLeft(2, '0')}-${dateOfBirth.day.toString().padLeft(2, '0')}",
        "placeOfBirth": placeOfBirth,
        "street": street,
        "city": city,
        "postalCode": postalCode,
        "countryCode": countryCode,
      };
}
