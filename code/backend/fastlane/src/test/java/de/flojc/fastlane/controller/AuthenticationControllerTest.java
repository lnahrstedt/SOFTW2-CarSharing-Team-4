package de.flojc.fastlane.controller;

import de.flojc.fastlane.request.AuthenticationRequest;
import de.flojc.fastlane.response.AuthenticationResponse;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static de.flojc.fastlane.helper.JsonGenerator.toJsonString;
import static helper.HeaderContainer.member2Header;
import static helper.TestConstants.*;
import static helper.TestStrategy.givenWhenThen_PostRequest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
@Order(3)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationControllerTest {

    private static final String LOGIN_PATH = "/auth/login";

    @Test
    @Order(1)
    public void test_login_without_password_unauthorized() {
        run_test_login(EMAIL_MEMBER_2_ACCOUNT, null, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(2)
    public void test_login_without_email_unauthorized() {
        run_test_login(null, PASSWORD_MEMBER2_ACCOUNT, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(3)
    public void test_login_email_unknown_unauthorized() {
        run_test_login(UNKNOWN_EMAIL, PASSWORD_MEMBER2_ACCOUNT, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(4)
    public void test_login_wrong_password_unauthorized() {
        run_test_login(EMAIL_MEMBER_2_ACCOUNT, WRONG_PASSWORD, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(5)
    public void test_login_correct_email_and_password_ok() {
        run_test_login(EMAIL_MEMBER_2_ACCOUNT, PASSWORD_MEMBER2_ACCOUNT, HttpStatus.OK);
    }

    private void run_test_login(String email, String password, HttpStatus expectedHttpStatus) {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder().email(email).password(password).build();
        String authRequestAsJson = toJsonString(authenticationRequest);


        switch (expectedHttpStatus) {
            case OK -> {

                AuthenticationResponse authenticationResponse = given()
                        .contentType(ContentType.JSON)
                        .body(authRequestAsJson)
                        .when()
                        .post(LOGIN_PATH)
                        .then()
                        .statusCode(expectedHttpStatus.value())
                        .body(notNullValue(AuthenticationResponse.class))
                        .contentType(ContentType.JSON)
                        .extract()
                        .as(AuthenticationResponse.class);

                member2Header = new Header("Authorization", "Bearer " + authenticationResponse.getAccessToken());

            }
            case UNAUTHORIZED ->
                    givenWhenThen_PostRequest(authRequestAsJson, LOGIN_PATH, expectedHttpStatus, equalTo(EMPTY_BODY));
        }

    }

}