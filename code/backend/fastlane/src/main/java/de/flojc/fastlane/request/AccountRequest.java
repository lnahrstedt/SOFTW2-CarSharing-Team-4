package de.flojc.fastlane.request;

import lombok.*;

/**
 * The class "AccountRequest" is a subclass of "Request" and represents a request for creating an account, with properties
 * such as account type, email, password, and phone number.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest extends Request {

    protected String accountType;
    protected String email;
    protected String password;
    protected String phone;

}