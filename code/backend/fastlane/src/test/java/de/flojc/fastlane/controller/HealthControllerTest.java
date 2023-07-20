package de.flojc.fastlane.controller;

import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static helper.TestConstants.API_BASE_URI;
import static helper.TestConstants.EMPTY_BODY;
import static helper.TestStrategy.givenWhenThen_GetRequest;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
@Order(1)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HealthControllerTest {
    private static final String HEALTH_BASE = "/health";
    private static boolean isPortSet = false;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        if (!isPortSet) {
            RestAssured.port = port;
            RestAssured.baseURI = API_BASE_URI;
            isPortSet = true;
        }
    }

    @Test
    @Order(1)
    void test_ping_ok() {
        run_test_ping(HttpStatus.OK);
    }

    @Test
    @Order(2)
    void test_checkDataBaseHealth_ok() {
        run_test_checkDataBaseHealth(HttpStatus.OK);
    }

    private void run_test_ping(HttpStatus expectedHttpStatus) {
        String path = HEALTH_BASE + "/ping";
        Matcher<?> matcher = containsString(LocalDate.now().toString());
        givenWhenThen_GetRequest(path, expectedHttpStatus, matcher);
    }

    private void run_test_checkDataBaseHealth(HttpStatus expectedHttpStatus) {
        String path = HEALTH_BASE + "/database";
        givenWhenThen_GetRequest(path, expectedHttpStatus, equalTo(EMPTY_BODY));
    }

}