// To parse this JSON data, do
//
//     final account = accountFromJson(jsonString);

import 'dart:convert';

Account accountFromJson(String str) => Account.fromJson(json.decode(str));

String accountToJson(Account data) => json.encode(data.toJson());

/// The Account class represents a user account with properties such as id, email, phone, password, user
/// details, and account type.
class Account {
  int id;
  String email;
  String phone;
  String password;
  User user;
  String accountType;

  Account({
    required this.id,
    required this.email,
    required this.phone,
    required this.password,
    required this.user,
    required this.accountType,
  });

  factory Account.fromJson(Map<String, dynamic> json) => Account(
        id: json["id"],
        email: json["email"],
        phone: json["phone"],
        password: json["password"],
        user: User.fromJson(json["user"]),
        accountType: json["accountType"],
      );

  Map<String, dynamic> toJson() => {
        "id": id,
        "email": email,
        "phone": phone,
        "password": password,
        "user": user.toJson(),
        "accountType": accountType,
      };
}

/// The User class represents a user with properties such as id, firstName, lastName, dateOfBirth,
/// placeOfBirth, and address.
class User {
  int id;
  String firstName;
  String lastName;
  DateTime dateOfBirth;
  String placeOfBirth;
  Address address;

  User({
    required this.id,
    required this.firstName,
    required this.lastName,
    required this.dateOfBirth,
    required this.placeOfBirth,
    required this.address,
  });

  factory User.fromJson(Map<String, dynamic> json) => User(
        id: json["id"],
        firstName: json["firstName"],
        lastName: json["lastName"],
        dateOfBirth: DateTime.parse(json["dateOfBirth"]),
        placeOfBirth: json["placeOfBirth"],
        address: Address.fromJson(json["address"]),
      );

  Map<String, dynamic> toJson() => {
        "id": id,
        "firstName": firstName,
        "lastName": lastName,
        "dateOfBirth":
            "${dateOfBirth.year.toString().padLeft(4, '0')}-${dateOfBirth.month.toString().padLeft(2, '0')}-${dateOfBirth.day.toString().padLeft(2, '0')}",
        "placeOfBirth": placeOfBirth,
        "address": address.toJson(),
      };
}

/// The Address class represents a physical address with properties for street, postal code, city, and
/// country.
class Address {
  String street;
  String postalCode;
  String city;
  String country;

  Address({
    required this.street,
    required this.postalCode,
    required this.city,
    required this.country,
  });

  factory Address.fromJson(Map<String, dynamic> json) => Address(
        street: json["street"],
        postalCode: json["postalCode"],
        city: json["city"],
        country: json["country"],
      );

  Map<String, dynamic> toJson() => {
        "street": street,
        "postalCode": postalCode,
        "city": city,
        "country": country,
      };
}
