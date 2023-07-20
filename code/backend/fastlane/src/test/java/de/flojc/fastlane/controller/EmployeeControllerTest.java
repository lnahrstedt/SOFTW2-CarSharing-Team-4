package de.flojc.fastlane.controller;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.service.EmployeeService;
import io.restassured.http.Header;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static de.flojc.fastlane.helper.JsonGenerator.toJsonString;
import static helper.HeaderContainer.*;
import static helper.TestConstants.EMPTY_BODY;
import static helper.TestStrategy.givenWhenThen_GetRequest;
import static helper.TestStrategy.givenWhenThen_GetRequest_Expect_BackendException;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
@Order(5)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeControllerTest {
    private static final String EMPLOYEE_PATH = "/employee";

    @Autowired
    private EmployeeService employeeService;

    @Test
    @Order(1)
    void test_findAllEmployees_asAdmin_ok() {
        run_test_getEmployees(adminHeader, HttpStatus.OK);
    }

    @Test
    @Order(2)
    void test_findAllEmployees_asEmployee_unauthorized() {
        run_test_getEmployees(employeeHeader, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(3)
    void test_findAllEmployees_asMember_unauthorized() {
        run_test_getEmployees(member2Header, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(4)
    void test_findEmployeeById_asAdmin_ok() throws BackendException {
        run_test_findEmployeeById(adminHeader, HttpStatus.OK);
    }

    @Test
    @Order(5)
    void test_findEmployeeById_asEmployee_unauthorized() throws BackendException {
        run_test_findEmployeeById(employeeHeader, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(6)
    void test_findEmployeeById_asMember_unauthorized() throws BackendException {
        run_test_findEmployeeById(member2Header, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(7)
    void test_findEmployeeByUnknownId_asAdmin_not_found() throws BackendException {
        run_test_findEmployeeById(adminHeader, HttpStatus.NOT_FOUND);
    }

    private void run_test_findEmployeeById(Header header, HttpStatus expectedHttpStatus) throws BackendException {
        String employeeId = expectedHttpStatus == HttpStatus.NOT_FOUND ? "fastlane" : employeeService.findAll().stream().findFirst().get().getId();
        String path = EMPLOYEE_PATH + "/" + employeeId;

        if (expectedHttpStatus == HttpStatus.NOT_FOUND) {
            BackendException backendException = new BackendException(ErrorCode.EMPLOYEE_NOT_FOUND, employeeId);
            givenWhenThen_GetRequest_Expect_BackendException(header, path, expectedHttpStatus, backendException);
        } else {
            Matcher<?> matcher = expectedHttpStatus == HttpStatus.OK ? equalTo(toJsonString(employeeService.findById(employeeId))) : equalTo(EMPTY_BODY);
            givenWhenThen_GetRequest(header, path, expectedHttpStatus, matcher);
        }
    }

    private void run_test_getEmployees(Header header, HttpStatus expectedHttpStatus) {
        Matcher<?> matcher = expectedHttpStatus == HttpStatus.OK ? equalTo(toJsonString(employeeService.findAll())) : equalTo(EMPTY_BODY);
        givenWhenThen_GetRequest(header, EMPLOYEE_PATH, expectedHttpStatus, matcher);
    }
}