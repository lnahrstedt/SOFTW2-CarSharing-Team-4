package de.flojc.fastlane.controller;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.model.User;
import de.flojc.fastlane.service.AccountService;
import de.flojc.fastlane.service.UserService;
import io.restassured.http.Header;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Objects;

import static de.flojc.fastlane.helper.JsonGenerator.toJsonString;
import static helper.HeaderContainer.*;
import static helper.TestConstants.*;
import static helper.TestStrategy.*;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
@Order(8)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private static final String USER_PATH = "/user";

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Test
    @Order(1)
    void test_getAllUser_asAdmin_ok() {
        run_test_getAllUser(adminHeader, HttpStatus.OK);
    }

    @Test
    @Order(2)
    void test_getAllUser_asEmployee_ok() {
        run_test_getAllUser(employeeHeader, HttpStatus.OK);
    }

    @Test
    @Order(3)
    void test_getAllUser_asMember_unauthorized() {
        run_test_getAllUser(member2Header, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(4)
    void test_getUserById_asAdmin_ok() throws BackendException {
        run_test_getUserById(adminHeader, HttpStatus.OK);

    }

    @Test
    @Order(5)
    void test_getUserById_asEmployee_ok() throws BackendException {
        run_test_getUserById(employeeHeader, HttpStatus.OK);
    }

    @Test
    @Order(6)
    void test_getUserById_asMember_ownAccount_ok() throws BackendException {
        run_test_getUserById(member2Header, HttpStatus.OK);
    }

    @Test
    @Order(7)
    void test_getForeignUserById_asMember_unauthorized() throws BackendException {
        run_test_getUserById(member2Header, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(8)
    void test_getUnknownUserById_asEmployee_not_found() throws BackendException {
        run_test_getUserById(employeeHeader, HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(9)
    void test_updateUser_asAdmin_ok() throws BackendException {
        run_test_updateUser(adminHeader, HttpStatus.OK);
    }

    @Test
    @Order(10)
    void test_updateUser_asEmployee_ok() throws BackendException {
        run_test_updateUser(employeeHeader, HttpStatus.OK);
    }

    @Test
    @Order(11)
    void test_updateOwnUser_asMember_ok() throws BackendException {
        run_test_updateUser(member2Header, HttpStatus.OK);
    }

    @Test
    @Order(12)
    void test_updateForeignUser_asMember_unauthorized() throws BackendException {
        run_test_updateUser(member2Header, HttpStatus.UNAUTHORIZED);
    }

    private void run_test_getAllUser(Header header, HttpStatus expectedHttpStatus) {
        final String allUserAsJson = toJsonString(userService.findAll());
        final String expected = expectedHttpStatus == HttpStatus.UNAUTHORIZED ? EMPTY_BODY : allUserAsJson;

        givenWhenThen_GetRequest(header, USER_PATH, expectedHttpStatus, equalTo(expected));
    }


    private void run_test_getUserById(Header header, HttpStatus expectedHttpStatus) throws BackendException {
        switch (expectedHttpStatus) {
            case OK -> {
                final long userIdOfMember2 = accountService.findByEmail(EMAIL_MEMBER_2_ACCOUNT).getUser().getId();
                final String userAsJson = toJsonString(userService.findById(userIdOfMember2));
                givenWhenThen_GetRequest(header, USER_PATH + "/" + userIdOfMember2, expectedHttpStatus, equalTo(userAsJson));
            }
            case UNAUTHORIZED -> {
                final long userIdOfAdmin = accountService.findByEmail(EMAIL_ADMIN_ACCOUNT).getUser().getId();
                givenWhenThen_GetRequest(header, USER_PATH + "/" + userIdOfAdmin, HttpStatus.UNAUTHORIZED, equalTo(EMPTY_BODY));
            }
            case NOT_FOUND -> {
                final BackendException userNotFoundBackendException = new BackendException(ErrorCode.USER_NOT_FOUND, UNKNOWN_ID);
                givenWhenThen_GetRequest_Expect_BackendException(employeeHeader, USER_PATH + "/" + UNKNOWN_ID, HttpStatus.NOT_FOUND, userNotFoundBackendException);
            }
        }
    }

    private void run_test_updateUser(Header header, HttpStatus expectedHttpStatus) throws BackendException {
        final long userId = accountService.findByEmail(EMAIL_MEMBER_2_ACCOUNT).getUser().getId();

        final String newFirstName = "Sascha";
        final String newLastName = "McConaghy";

        final String newFirstName2 = "Rusty";
        final String newLastName2 = "Troillet";

        final String newFirstName3 = "Jacqui";

        UserUpdate userUpdate = null;

        switch (expectedHttpStatus) {
            case OK -> {

                if (header == adminHeader) {
                    userUpdate = new UserUpdate(userId, newFirstName, newLastName);
                } else if (header == employeeHeader) {
                    userUpdate = new UserUpdate(userId, newFirstName2, newLastName2);
                } else if (header == member2Header) {
                    userUpdate = new UserUpdate(userId, newFirstName3, null);
                }

                assert userUpdate != null;
                givenWhenThen_PatchRequest(header, userUpdate.getUpdateBody(), USER_PATH + "/" + userId, HttpStatus.OK, equalTo(userUpdate.getExpectedUser()));
            }
            case UNAUTHORIZED -> {
                if (header == member2Header) {
                    final long foreignUserId = accountService.findByEmail(EMAIL_ADMIN_ACCOUNT).getUser().getId();
                    userUpdate = new UserUpdate(foreignUserId, newFirstName3, null);
                    givenWhenThen_PatchRequest(header, userUpdate.updateBody, USER_PATH + "/" + foreignUserId, HttpStatus.UNAUTHORIZED, equalTo(EMPTY_BODY));
                }
            }
        }
    }

    @Getter
    private class UserUpdate {

        private final long userId;
        private final String lastName;
        private final String firstName;
        private final String updateBody;
        private final String expectedUser;

        public UserUpdate(long userId, String lastName, String firstName) throws BackendException {
            this.userId = userId;
            this.lastName = lastName;
            this.firstName = firstName;
            this.updateBody = toJsonString(new User(null, firstName, lastName, null, null, null, null, null, null));
            this.expectedUser = getExpectedUserAsJson(userId, lastName, firstName);
        }

        private String getExpectedUserAsJson(long userId, String lastName, String firstName) throws BackendException {
            User user = userService.findById(userId);

            if (Objects.nonNull(lastName)) {
                user.setLastName(lastName);
            }
            if (Objects.nonNull(firstName)) {
                user.setFirstName(firstName);
            }

            return toJsonString(user);
        }

    }
}