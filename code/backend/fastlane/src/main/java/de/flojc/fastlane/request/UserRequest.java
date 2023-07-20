package de.flojc.fastlane.request;

import lombok.*;

import java.time.LocalDate;

/**
 * The UserRequest class is a subclass of the Request class and represents a user request with various properties such as
 * first name, last name, date of birth, place of birth, street, postal code, city, and country code.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest extends Request {

    protected String firstName;
    protected String lastName;
    protected LocalDate dateOfBirth;
    protected String placeOfBirth;
    protected String street;
    protected String postalCode;
    protected String city;
    protected String countryCode;

}