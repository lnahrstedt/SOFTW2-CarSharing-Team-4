package helper;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.request.RegisterRequest;
import de.flojc.fastlane.response.AuthenticationResponse;
import de.flojc.fastlane.service.RegistrationService;
import io.restassured.http.Header;

public class TestInitializer {
    private final RegistrationService registrationService;

    public TestInitializer(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    public void start() throws BackendException {

        HeaderContainer.adminHeader = generateAuthHeader(AccountType.ADMIN);
        HeaderContainer.employeeHeader = generateAuthHeader(AccountType.EMPLOYEE);
        HeaderContainer.member1Header = generateAuthHeader(AccountType.MEMBER1);
        HeaderContainer.member2Header = generateAuthHeader(AccountType.MEMBER2);


    }

    private Header generateAuthHeader(AccountType accountType) throws BackendException {
        final String authKeyword = "Authorization";
        final String tokenPrefix = "Bearer ";
        String token = getTokenForAccount(accountType);
        return new Header(authKeyword, tokenPrefix + token);
    }


    private String getTokenForAccount(AccountType accountType) throws BackendException {
        RegisterRequest registerRequest = registerRequestGenerator(accountType);
        boolean isMember = accountType == AccountType.MEMBER1 || accountType == AccountType.MEMBER2;
        assert registerRequest != null;
        AuthenticationResponse authenticationResponse = registrationService.register(registerRequest, isMember);
        return authenticationResponse.getAccessToken();
    }


    private RegisterRequest registerRequestGenerator(AccountType accountType) {
        switch (accountType) {
            case ADMIN -> {
                return new RegisterRequest(
                        "1",
                        "ADMIN",
                        "rgoosnell0@paginegialle.it",
                        "Hallo1010!",
                        "+86 212 947 2545",
                        "Reginauld",
                        "Goosnell",
                        "1978-06-10",
                        "Xiaochuan",
                        "59715 Prairieview Circle",
                        "519-43-3249",
                        "Calachuchi",
                        "PW"
                );
            }
            case EMPLOYEE -> {
                return new RegisterRequest(
                        "2",
                        "EMPLOYEE",
                        "sfillimore1@booking.com",
                        "Hallo1010!",
                        "+380 685 481 3147",
                        "Sly",
                        "Fillimore",
                        "2020-12-08",
                        "Chernelytsya",
                        "9 Haas Drive",
                        "758-50-1024",
                        "Cibeureum",
                        "BV"
                );
            }
            case MEMBER1 -> {
                {
                    return new RegisterRequest(
                            "WA1VFBFL6DA894145",
                            "JUNIOR",
                            "atrevascus1@reuters.com",
                            "Hallo1010!",
                            "+86 999 152 7449",
                            "Bénédicte",
                            "Trevascus",
                            "1983-05-11",
                            "Chendong",
                            "31765 Sage Crossing",
                            "826-35-9089",
                            "Shangma",
                            "NZ"
                    );
                }
            }
            case MEMBER2 -> {
                {
                    return new RegisterRequest(
                            "1D4SE6GT3BC381294",
                            "COMFORT",
                            "djambrozek1b@technorati.com",
                            "Hallo1010!",
                            "+66 398 670 1245",
                            "Françoise",
                            "Jambrozek",
                            "1936-08-05",
                            "Chumphon Buri",
                            "99209 Village Green Terrace",
                            "547-99-6406",
                            "Villanueva",
                            "SK"
                    );
                }
            }

            default -> {
                return null;
            }
        }

    }


    private enum AccountType {
        ADMIN,
        EMPLOYEE,
        MEMBER1,
        MEMBER2
    }
}


