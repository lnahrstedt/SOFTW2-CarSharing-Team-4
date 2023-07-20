package de.flojc.fastlane.request;

import lombok.*;

/**
 * The class "AuthenticationRequest" is a subclass of "Request" and includes fields for email and password, along with
 * getters, setters, and constructors.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest extends Request {

    protected String email;
    protected String password;

}