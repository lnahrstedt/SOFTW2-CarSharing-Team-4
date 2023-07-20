package de.flojc.fastlane.controller;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.request.RegisterRequest;
import helper.FailType;
import io.restassured.http.Header;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static de.flojc.fastlane.helper.JsonGenerator.toJsonString;
import static helper.HeaderContainer.*;
import static helper.TestConstants.*;
import static helper.TestStrategy.givenWhenThen_PostRequest;
import static helper.TestStrategy.givenWhenThen_PostRequestExpectException;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
@Order(6)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegistrationControllerTest {

    private static final String REGISTRATION_PATH = "/register";
    private static final String MEMBER_PATH = "/member";
    private static final String EMPLOYEE_PATH = "/employee";

    @Test
    @Order(1)
    void test_registerMember_licenseId_in_use_conflict() {
        run_test_registerMember(FailType.LICENSE_ID_IN_USE, HttpStatus.CONFLICT);
    }

    @Test
    @Order(2)
    void test_registerMember_email_in_use_conflict() {
        run_test_registerMember(FailType.EMAIL_IN_USE, HttpStatus.CONFLICT);
    }

    @Test
    @Order(3)
    void test_registerMember_password_inadequate_bad_request() {
        run_test_registerMember(FailType.PASSWORD_INADEQUATE, HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(4)
    void test_registerMember_field_null_bad_request() {
        run_test_registerMember(FailType.FIELD_NULL, HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(5)
    void test_registerMember_field_blank_bad_request() {
        run_test_registerMember(FailType.FIELD_BLANK, HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(6)
    void test_registerMember_unknown_fare_type_not_found() {
        run_test_registerMember(FailType.UNKNOWN_FARE_TYPE, HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(7)
    void test_registerMember_created() {
        run_test_registerMember(FailType.NONE, HttpStatus.CREATED);
    }

    @Test
    @Order(8)
    void test_registerEmployee_asAdmin_employeeId_in_use_conflict() {
        run_test_registerEmployee(adminHeader, FailType.EMPLOYEE_ID_IN_USE, HttpStatus.CONFLICT);
    }

    @Test
    @Order(9)
    void test_registerEmployee_asAdmin_email_in_use_conflict() {
        run_test_registerEmployee(adminHeader, FailType.EMAIL_IN_USE, HttpStatus.CONFLICT);
    }

    @Test
    @Order(10)
    void test_registerEmployee_asAdmin_password_inadequate_bad_request() {
        run_test_registerEmployee(adminHeader, FailType.PASSWORD_INADEQUATE, HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(11)
    void test_registerEmployee_asAdmin_unknown_account_type_not_found() {
        run_test_registerEmployee(adminHeader, FailType.UNKNOWN_ACCOUNT_TYPE, HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(12)
    void test_registerEmployee_asMember_unauthorized() {
        run_test_registerEmployee(member2Header, FailType.NONE, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(13)
    void test_registerEmployee_asEmployee_unauthorized() {
        run_test_registerEmployee(employeeHeader, FailType.NONE, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(14)
    void test_registerEmployee_asAdmin_created() {
        run_test_registerEmployee(adminHeader, FailType.NONE, HttpStatus.CREATED);
    }

    private void run_test_registerEmployee(Header header, FailType failType, HttpStatus httpStatus) {
        Map<FailType, BackendException> exceptionMapping = Map.of(
                FailType.EMAIL_IN_USE, new BackendException(ErrorCode.EMAIL_ADDRESS_ALREADY_IN_USE, EMAIL_MEMBER_2_ACCOUNT),
                FailType.PASSWORD_INADEQUATE, new BackendException(ErrorCode.PASSWORD_INADEQUATE),
                FailType.EMPLOYEE_ID_IN_USE, new BackendException(ErrorCode.EMPLOYEE_ALREADY_EXIST, EMPLOYEE_ID_OF_ADMIN),
                FailType.UNKNOWN_ACCOUNT_TYPE, new BackendException(ErrorCode.ACCOUNT_TYPE_NOT_FOUND, UNKNOWN_ACCOUNT_TYPE)
        );

        String path = REGISTRATION_PATH + EMPLOYEE_PATH;
        String registerRequestAsJson = toJsonString(getRegisterRequest(AccoutType.EMPLOYEE, failType));

        if (failType == FailType.NONE) {
            Matcher<?> matcher = httpStatus == HttpStatus.UNAUTHORIZED ? equalTo(EMPTY_BODY) : containsString(NEW_EMPLOYEE_EMAIL);
            givenWhenThen_PostRequest(header, registerRequestAsJson, path, httpStatus, matcher);
        } else {
            givenWhenThen_PostRequestExpectException(header, registerRequestAsJson, path, httpStatus, exceptionMapping.get(failType));
        }
    }

    private void run_test_registerMember(FailType failType, HttpStatus httpStatus) {
        Map<FailType, BackendException> exceptionMapping = Map.of(
                FailType.LICENSE_ID_IN_USE, new BackendException(ErrorCode.DRIVER_LICENSE_ALREADY_IN_USE, LICENSE_ID_MEMBER_2),
                FailType.EMAIL_IN_USE, new BackendException(ErrorCode.EMAIL_ADDRESS_ALREADY_IN_USE, EMAIL_MEMBER_2_ACCOUNT),
                FailType.PASSWORD_INADEQUATE, new BackendException(ErrorCode.PASSWORD_INADEQUATE),
                FailType.FIELD_NULL, new BackendException(ErrorCode.UNSET_FIELD, "street"),
                FailType.FIELD_BLANK, new BackendException(ErrorCode.BLANK_FIELD, "city"),
                FailType.UNKNOWN_FARE_TYPE, new BackendException(ErrorCode.FARE_TYPE_NOT_FOUND, UNKNOWN_FARE_TYPE)
        );

        String path = REGISTRATION_PATH + MEMBER_PATH;
        String registerRequestAsJson = toJsonString(getRegisterRequest(AccoutType.MEMBER, failType));

        if (failType == FailType.NONE) {
            givenWhenThen_PostRequest(registerRequestAsJson, path, httpStatus, containsString(NEW_MEMBER_EMAIL));
        } else {
            givenWhenThen_PostRequestExpectException(registerRequestAsJson, path, httpStatus, exceptionMapping.get(failType));
        }
    }

    private RegisterRequest getRegisterRequest(AccoutType accoutType, FailType failType) {

        RegisterRequest registerRequest = accoutType ==
                AccoutType.MEMBER ? new RegisterRequest
                (
                        "2V4RW3DG4BR005036",
                        "PREMIUM",
                        NEW_MEMBER_EMAIL,
                        "fN0!cLG!08i?",
                        "473-208-7926",
                        "Harrietta",
                        "Simonnot",
                        "1949-09-14",
                        "Bayeman",
                        "14904 Comanche Court",
                        "Nurlat",
                        "150-26-0768",
                        "PA"
                ) : new RegisterRequest
                (
                        "0xffa8de69446b855037c27e0629ad33b83a536c2f",
                        "EMPLOYEE",
                        NEW_EMPLOYEE_EMAIL,
                        "qV4&Bh+O",
                        "563-280-7935",
                        "Chiquia",
                        "Halksworth",
                        "1927-07-11",
                        "Himeji",
                        "1824 Pawling Parkway",
                        "Zhaitou",
                        "645-15-2323",
                        "TW"
                );


        switch (failType) {
            case LICENSE_ID_IN_USE -> registerRequest.setId(LICENSE_ID_MEMBER_2);
            case EMPLOYEE_ID_IN_USE -> registerRequest.setId(EMPLOYEE_ID_OF_ADMIN);
            case EMAIL_IN_USE -> registerRequest.setEmail(EMAIL_MEMBER_2_ACCOUNT);
            case PASSWORD_INADEQUATE -> registerRequest.setPassword(INADEQUATE_PASSWORD);
            case FIELD_NULL -> registerRequest.setStreet(null);
            case FIELD_BLANK -> registerRequest.setCity("  ");
            case UNKNOWN_FARE_TYPE -> registerRequest.setTypeName(UNKNOWN_FARE_TYPE);
            case UNKNOWN_ACCOUNT_TYPE -> registerRequest.setTypeName(UNKNOWN_ACCOUNT_TYPE);
            case NONE -> {
                return registerRequest;
            }
        }

        return registerRequest;
    }

    private enum AccoutType {
        MEMBER,
        EMPLOYEE
    }


}