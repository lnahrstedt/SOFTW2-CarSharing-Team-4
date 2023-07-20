package de.flojc.fastlane.controller;

import de.flojc.fastlane.auth.token.TokenRepository;
import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.model.Account;
import de.flojc.fastlane.request.AccountRequest;
import de.flojc.fastlane.response.AuthenticationResponse;
import de.flojc.fastlane.service.AccountService;
import de.flojc.fastlane.service.RegistrationService;
import helper.TestInitializer;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Objects;

import static de.flojc.fastlane.helper.JsonGenerator.toJsonString;
import static helper.HeaderContainer.*;
import static helper.TestConstants.*;
import static helper.TestStrategy.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
@Order(2)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTest {
    private static final String ACCOUNT_PATH = "/account";
    private static boolean isSetupDone = false;


    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private AccountService accountService;

    @BeforeEach
    void setUp() throws BackendException {
        log.info("Starting setUp()");
        if (!isSetupDone) {
            new TestInitializer(registrationService).start();
            isSetupDone = true;
            log.info("Done setUp()");
        }
    }

    @Test
    @Order(1)
    void test_getAllAccounts_asAdmin_ok() {
        run_test_getAllAccounts(adminHeader, HttpStatus.OK);
    }

    @Test
    @Order(2)
    void test_getAllAccounts_asEmployee_ok() {
        run_test_getAllAccounts(employeeHeader, HttpStatus.OK);
    }

    @Test
    @Order(3)
    void test_getAllAccounts_asMember_unauthorized() {
        run_test_getAllAccounts(member1Header, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(4)
    void test_getAccountById_asAdmin_ok() throws BackendException {
        run_test_getAccountById(adminHeader, HttpStatus.OK);

    }

    @Test
    @Order(5)
    void test_getAccountById_asEmployee_ok() throws BackendException {
        run_test_getAccountById(employeeHeader, HttpStatus.OK);
    }

    @Test
    @Order(6)
    void test_getAccountById_asMember_ownAccount_ok() throws BackendException {
        run_test_getAccountById(member1Header, HttpStatus.OK);
    }

    @Test
    @Order(7)
    void test_getForeignAccountById_asMember_unauthorized() throws BackendException {
        run_test_getAccountById(member1Header, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(8)
    void test_getUnknownAccountById_asEmployee_not_found() throws BackendException {
        run_test_getAccountById(employeeHeader, HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(9)
    void test_getAccountByEmail_asAdmin_ok() throws BackendException {
        run_test_getAccountByEmail(adminHeader, HttpStatus.OK);
    }

    @Test
    @Order(10)
    void test_getAccountByEmail_asEmployee_ok() throws BackendException {
        run_test_getAccountByEmail(employeeHeader, HttpStatus.OK);
    }

    @Test
    @Order(11)
    void test_getAccountByEmail_asMember_unauthorized() throws BackendException {
        run_test_getAccountByEmail(member1Header, HttpStatus.UNAUTHORIZED);
    }


    @Test
    @Order(12)
    void test_getUnknownAccountByEmail_asEmployee_not_found() throws BackendException {
        run_test_getAccountByEmail(employeeHeader, HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(13)
    void test_doesEmailExist_not_in_use_ok() {
        run_test_doesEmailExist(UNKNOWN_EMAIL, HttpStatus.OK);
    }

    @Test
    @Order(14)
    void test_doesEmailExist_in_use_conflict() {
        run_test_doesEmailExist(EMAIL_ADMIN_ACCOUNT, HttpStatus.CONFLICT);
    }

    @Test
    @Order(15)
    void test_updateAccount_asAdmin_ok() throws BackendException {
        run_test_updateAccount(adminHeader, HttpStatus.OK);
    }

    @Test
    @Order(16)
    void test_updateAccount_asEmployee_ok() throws BackendException {
        run_test_updateAccount(employeeHeader, HttpStatus.OK);
    }

    @Test
    @Order(17)
    void test_updateOwnAccount_asMember_ok() throws BackendException {
        run_test_updateAccount(member1Header, HttpStatus.OK);
    }

    @Test
    @Order(18)
    void test_updateForeignAccount_as_Member_unauthorized() throws BackendException {
        run_test_updateAccount(member1Header, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(19)
    void test_deleteUnknownAccount_asAdmin_not_found() throws BackendException {
        run_test_deleteAccount(adminHeader, HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(20)
    void test_deleteAccount_asAdmin_no_content() throws BackendException {
        run_test_deleteAccount(adminHeader, HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(21)
    void test_deleteAccount_asEmployee_no_content() throws BackendException {
        run_test_deleteAccount(employeeHeader, HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(22)
    void test_deleteForeignAccount_asMember_unauthorized() throws BackendException {
        run_test_deleteAccount(member1Header, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(23)
    void test_deleteOwnAccount_asMember_no_content() throws BackendException {
        run_test_deleteAccount(member1Header, HttpStatus.NO_CONTENT);
    }


    private void run_test_getAllAccounts(Header header, HttpStatus expectedHttpStatus) {
        final String allAccountsAsJson = toJsonString(accountService.findAll());
        final String expected = expectedHttpStatus == HttpStatus.UNAUTHORIZED ? EMPTY_BODY : allAccountsAsJson;

        givenWhenThen_GetRequest(header, ACCOUNT_PATH, expectedHttpStatus, equalTo(expected));
    }


    private void run_test_getAccountById(Header header, HttpStatus expectedHttpStatus) throws BackendException {
        switch (expectedHttpStatus) {
            case OK -> {
                final String accountAsJson = toJsonString(accountService.findById(ID_OF_MEMBER_1));
                final String PATH_TO_MEMBER_ACCOUNT = ACCOUNT_PATH + "/id/" + ID_OF_MEMBER_1;
                givenWhenThen_GetRequest(header, PATH_TO_MEMBER_ACCOUNT, expectedHttpStatus, equalTo(accountAsJson));
            }
            case UNAUTHORIZED -> {
                final String PATH_TO_ADMIN_ACCOUNT = ACCOUNT_PATH + "/id/" + ID_OF_MEMBER_2;
                givenWhenThen_GetRequest(header, PATH_TO_ADMIN_ACCOUNT, HttpStatus.UNAUTHORIZED, equalTo(EMPTY_BODY));
            }
            case NOT_FOUND -> {
                final BackendException accountNotFoundBackendException = new BackendException(ErrorCode.ACCOUNT_NOT_FOUND, UNKNOWN_ID);
                final String PATH_TO_UNKNOWN_ACCOUNT = ACCOUNT_PATH + "/id/" + UNKNOWN_ID;
                givenWhenThen_GetRequest_Expect_BackendException(employeeHeader, PATH_TO_UNKNOWN_ACCOUNT, HttpStatus.NOT_FOUND, accountNotFoundBackendException);
            }
        }
    }

    private void run_test_getAccountByEmail(Header header, HttpStatus expectedHttpStatus) throws BackendException {
        final String getByEmailPath = "/account/email/";
        switch (expectedHttpStatus) {
            case OK -> {
                final String account3AsJson = toJsonString(accountService.findByEmail(EMAIL_MEMBER_1_ACCOUNT));
                final String PATH_TO_MEMBER_ACCOUNT = getByEmailPath + EMAIL_MEMBER_1_ACCOUNT;
                givenWhenThen_GetRequest(header, PATH_TO_MEMBER_ACCOUNT, HttpStatus.OK, equalTo(account3AsJson));
            }
            case UNAUTHORIZED -> {
                final String PATH_TO_FOREIGN_ACCOUNT = getByEmailPath + EMAIL_ADMIN_ACCOUNT;
                givenWhenThen_GetRequest(header, PATH_TO_FOREIGN_ACCOUNT, HttpStatus.UNAUTHORIZED, equalTo(EMPTY_BODY));

            }
            case NOT_FOUND -> {

                final BackendException accountNotFoundBackendException = new BackendException(ErrorCode.ACCOUNT_NOT_FOUND, UNKNOWN_EMAIL);
                final String PATH_TO_UNKNOWN_ACCOUNT = getByEmailPath + UNKNOWN_EMAIL;
                givenWhenThen_GetRequest_Expect_BackendException(header, PATH_TO_UNKNOWN_ACCOUNT, HttpStatus.NOT_FOUND, accountNotFoundBackendException);
            }
        }

    }

    private void run_test_doesEmailExist(String email, HttpStatus httpStatus) {
        final String EMAIL_EXIST_PATH = ACCOUNT_PATH + "/exist/" + email;
        switch (httpStatus) {
            case OK -> givenWhenThen_GetRequest(EMAIL_EXIST_PATH, HttpStatus.OK, equalTo(EMPTY_BODY));
            case CONFLICT -> {
                final BackendException backendException = new BackendException(ErrorCode.EMAIL_ADDRESS_ALREADY_IN_USE, email);
                givenWhenThen_GetRequest_Expect_BackendException(EMAIL_EXIST_PATH, HttpStatus.CONFLICT, backendException);
            }
        }
    }

    private void run_test_deleteAccount(Header header, HttpStatus expectedHttpStatus) throws BackendException {
        long idOfAccountToBeDeleted = 0;

        if (Objects.equals(header, adminHeader)) {
            idOfAccountToBeDeleted = 1;
        } else if (Objects.equals(header, employeeHeader)) {
            idOfAccountToBeDeleted = 2;
        } else if (Objects.equals(header, member1Header)) {
            idOfAccountToBeDeleted = accountService.findByEmail(EMAIL_MEMBER_1_ACCOUNT).getId();
        }

        String PATH_TO_ACCOUNT_TO_BE_DELETED = ACCOUNT_PATH + "/" + idOfAccountToBeDeleted;

        switch (expectedHttpStatus) {
            case NO_CONTENT ->
                    givenWhenThen_DeleteRequest(header, PATH_TO_ACCOUNT_TO_BE_DELETED, HttpStatus.NO_CONTENT, equalTo(EMPTY_BODY));
            case UNAUTHORIZED -> {
                if (Objects.equals(header, member1Header)) {
                    PATH_TO_ACCOUNT_TO_BE_DELETED = ACCOUNT_PATH + idOfAccountToBeDeleted;
                    givenWhenThen_DeleteRequest(member1Header, PATH_TO_ACCOUNT_TO_BE_DELETED, HttpStatus.UNAUTHORIZED, equalTo(EMPTY_BODY));
                }
            }
            case NOT_FOUND -> {
                PATH_TO_ACCOUNT_TO_BE_DELETED = ACCOUNT_PATH + "/" + UNKNOWN_ID;
                BackendException backendException = new BackendException(ErrorCode.ACCOUNT_NOT_FOUND, UNKNOWN_ID);
                givenWhenThen_DeleteRequestExpectException(header, PATH_TO_ACCOUNT_TO_BE_DELETED, expectedHttpStatus, backendException);
            }
        }
    }

    private void run_test_updateAccount(Header header, HttpStatus expectedHttpStatus) throws BackendException {
        final String initialEmail = EMAIL_MEMBER_1_ACCOUNT;
        final String newEmail = "ttukesbyf@opera.com";
        final String newPhone = "+591 856 354 8531";
        final String newPhone2 = "+1 292 323 0697";
        final String newPhone3 = "+355 680 557 5529";

        AccountUpdate accountUpdate = null;

        String emailToUpdate = initialEmail;
        String newEmailToUpdate = null;
        String newPhoneToUpdate = null;

        if (header == adminHeader) {
            newEmailToUpdate = newEmail;
            newPhoneToUpdate = newPhone;
        } else if (header == employeeHeader) {
            emailToUpdate = newEmail;
            newEmailToUpdate = initialEmail;
            newPhoneToUpdate = newPhone2;
        } else if (header == member1Header) {
            newPhoneToUpdate = newPhone3;
        }

        accountUpdate = new AccountUpdate(emailToUpdate, newEmailToUpdate, newPhoneToUpdate);
        final String accountPath = ACCOUNT_PATH + "/" + emailToUpdate + "/" + accountService.findByEmail(emailToUpdate).getId();

        switch (expectedHttpStatus) {
            case OK -> {
                if (header == member1Header) {
                    AuthenticationResponse authenticationResponse = given()
                            .header(header)
                            .contentType(ContentType.JSON)
                            .body(accountUpdate.updateBody)
                            .when()
                            .patch(accountPath)
                            .then()
                            .statusCode(expectedHttpStatus.value())
                            .body(notNullValue(AuthenticationResponse.class))
                            .contentType(ContentType.JSON)
                            .extract()
                            .as(AuthenticationResponse.class);

                    member1Header = new Header("Authorization", "Bearer " + authenticationResponse.getAccessToken());
                } else {
                    givenWhenThen_PatchRequest(header, accountUpdate.getUpdateBody(), accountPath, HttpStatus.OK, equalTo(accountUpdate.getExpectedAccount()));
                }
            }
            case UNAUTHORIZED -> {
                if (header == member1Header) {
                    final String FOREIGN_EMAIL = "rgoosnell0@paginegialle.it";
                    final String FOREIGN_ACCOUNT = ACCOUNT_PATH + "/" + FOREIGN_EMAIL + "/" + accountService.findByEmail(EMAIL_MEMBER_1_ACCOUNT).getId();
                    givenWhenThen_PatchRequest(header, toJsonString(new AccountRequest(null, initialEmail, null, newPhone)), FOREIGN_ACCOUNT, HttpStatus.UNAUTHORIZED, equalTo(EMPTY_BODY));
                }
            }
        }
    }

    private String getExpectedAccountAsJson(String emailOfAccount, String updatedEmail, String updatedPhone) throws BackendException {
        Account account = accountService.findByEmail(emailOfAccount);

        if (Objects.nonNull(updatedEmail)) {
            account.setEmail(updatedEmail);
        }
        if (Objects.nonNull(updatedPhone)) {
            account.setPhone(updatedPhone);
        }

        return toJsonString(account);
    }

    @Getter
    private class AccountUpdate {

        private final String emailOfAccount;
        private final String updatedEmail;
        private final String updatedPhone;
        private final String updateBody;
        private final String expectedAccount;

        public AccountUpdate(String emailOfAccount, String updatedEmail, String updatedPhone) throws BackendException {
            this.emailOfAccount = emailOfAccount;
            this.updatedEmail = updatedEmail;
            this.updatedPhone = updatedPhone;
            this.updateBody = toJsonString(new AccountRequest(null, updatedEmail, null, updatedPhone));
            this.expectedAccount = getExpectedAccountAsJson(emailOfAccount, updatedEmail, updatedPhone);
        }

    }

}