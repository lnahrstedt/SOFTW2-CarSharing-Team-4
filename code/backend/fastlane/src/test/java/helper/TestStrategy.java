package helper;

import de.flojc.fastlane.exception.BackendException;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.hamcrest.Matcher;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TestStrategy {

    public static void givenWhenThen_GetRequest(Header header, String path, HttpStatus httpStatus, Matcher<?> matcher) {

        given()
                .header(header)
                .when()
                .get(path)
                .then()
                .statusCode(httpStatus.value())
                .and()
                .body(matcher)
        ;
    }

    public static void givenWhenThen_GetRequest(String path, HttpStatus httpStatus, Matcher<?> matcher) {

        given()
                .when()
                .get(path)
                .then()
                .statusCode(httpStatus.value())
                .and()
                .body(matcher)
        ;
    }

    public static void givenWhenThen_PostRequest(Header header, String body, String path, HttpStatus httpStatus, Matcher<?> matcher) {

        given()
                .header(header)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(path)
                .then()
                .statusCode(httpStatus.value())
                .body(matcher)
        ;
    }

    public static void givenWhenThen_PostRequest(String body, String path, HttpStatus httpStatus, Matcher<?> matcher) {

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(path)
                .then()
                .statusCode(httpStatus.value())
                .body(matcher)
        ;
    }

    public static void givenWhenThen_PostRequestExpectException(Header header, String body, String path, HttpStatus httpStatus, BackendException backendException) {

        given()
                .header(header)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(path)
                .then()
                .statusCode(httpStatus.value())
                .body("errorCode", equalTo(backendException.getErrorCode().getCode()))
                .body("errorName", equalTo(backendException.getErrorCode().toString()))
                .body("description", equalTo(backendException.getDescription()))
        ;
    }

    public static void givenWhenThen_PostRequestExpectException(String body, String path, HttpStatus httpStatus, BackendException backendException) {

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(path)
                .then()
                .statusCode(httpStatus.value())
                .body("errorCode", equalTo(backendException.getErrorCode().getCode()))
                .body("errorName", equalTo(backendException.getErrorCode().toString()))
                .body("description", equalTo(backendException.getDescription()))
        ;
    }

    public static void givenWhenThen_DeleteRequest(Header header, String path, HttpStatus httpStatus, Matcher<?> matcher) {

        given()
                .header(header)
                .contentType(ContentType.JSON)
                .when()
                .delete(path)
                .then()
                .statusCode(httpStatus.value())
                .and()
                .body(matcher)
        ;
    }

    public static void givenWhenThen_DeleteRequestExpectException(Header header, String path, HttpStatus httpStatus, BackendException backendException) {

        given()
                .header(header)
                .contentType(ContentType.JSON)
                .when()
                .delete(path)
                .then()
                .statusCode(httpStatus.value())
                .and()
                .body("errorCode", equalTo(backendException.getErrorCode().getCode()))
                .body("errorName", equalTo(backendException.getErrorCode().toString()))
                .body("description", equalTo(backendException.getDescription()))
        ;
    }

    public static void givenWhenThen_PatchRequest(Header header, String body, String path, HttpStatus httpStatus, Matcher<?> matcher) {

        given()
                .header(header)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .patch(path)
                .then()
                .statusCode(httpStatus.value())
                .and()
                .body(matcher)
        ;
    }

    public static void givenWhenThen_PatchRequest_Expect_BackendException(Header header, String body, String path, HttpStatus httpStatus, BackendException backendException) {

        given()
                .header(header)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .patch(path)
                .then()
                .statusCode(httpStatus.value())
                .and()
                .body("errorCode", equalTo(backendException.getErrorCode().getCode()))
                .body("errorName", equalTo(backendException.getErrorCode().toString()))
                .body("description", equalTo(backendException.getDescription()))
        ;
    }

    public static void givenWhenThen_GetRequest_Expect_BackendException(Header header, String path, HttpStatus httpStatus, BackendException backendException) {

        given()
                .header(header)
                .contentType(ContentType.JSON)
                .when()
                .get(path)
                .then()
                .statusCode(httpStatus.value())
                .and()
                .body("errorCode", equalTo(backendException.getErrorCode().getCode()))
                .body("errorName", equalTo(backendException.getErrorCode().toString()))
                .body("description", equalTo(backendException.getDescription()))
        ;
    }

    public static void givenWhenThen_GetRequest_Expect_BackendException(String path, HttpStatus httpStatus, BackendException backendException) {

        given()
                .when()
                .get(path)
                .then()
                .statusCode(httpStatus.value())
                .and()
                .body("errorCode", equalTo(backendException.getErrorCode().getCode()))
                .body("errorName", equalTo(backendException.getErrorCode().toString()))
                .body("description", equalTo(backendException.getDescription()))
        ;
    }
}
