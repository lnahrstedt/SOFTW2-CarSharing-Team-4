package de.flojc.fastlane.request;

import lombok.*;

/**
 * The RegisterRequest class is a subclass of Request and contains various properties for registering a user.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest extends Request {

    protected String id;
    protected String typeName;
    protected String email;
    protected String password;
    protected String phone;
    protected String firstName;
    protected String lastName;
    protected String dateOfBirth;
    protected String placeOfBirth;
    protected String street;
    protected String postalCode;
    protected String city;
    protected String countryCode;
}