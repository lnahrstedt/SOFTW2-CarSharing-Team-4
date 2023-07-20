package de.flojc.fastlane.controller;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.model.Account;
import de.flojc.fastlane.model.Driver;
import de.flojc.fastlane.request.DriverRequest;
import de.flojc.fastlane.service.AccountService;
import de.flojc.fastlane.service.DriverService;
import de.flojc.fastlane.service.FareTypeService;
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
import static helper.TestConstants.*;
import static helper.TestStrategy.*;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
@Order(4)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DriverControllerTest {

    private static final String DRIVER_PATH = "/driver";


    @Autowired
    private DriverService driverService;

    @Autowired
    private FareTypeService fareTypeService;

    @Autowired
    private AccountService accountService;

    @Test
    @Order(1)
    public void test_doesDriverExist_not_in_use_ok() {
        run_test_doesDriverExist(UNUSED_LICENSE_ID, HttpStatus.OK);
    }

    @Test
    @Order(2)
    public void test_doesDriverExist_in_use_conflict() {
        run_test_doesDriverExist(LICENSE_ID_MEMBER_2, HttpStatus.CONFLICT);
    }

    @Test
    @Order(3)
    public void test_getDriverByUserId_asAdmin_ok() throws BackendException {
        run_test_getByUserId(adminHeader, HttpStatus.OK);
    }

    @Test
    @Order(4)
    public void test_getDriverByUserId_asEmployee_ok() throws BackendException {
        run_test_getByUserId(employeeHeader, HttpStatus.OK);
    }

    @Test
    @Order(5)
    public void test_getDriverByUserId_asMember_ok() throws BackendException {
        run_test_getByUserId(member2Header, HttpStatus.OK);
    }

    @Test
    @Order(6)
    public void test_getForeignDriverByUserId_asMember_unauthorized() throws BackendException {
        run_test_getByUserId(member2Header, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(7)
    public void test_getDriverByUnknownUserId_asEmployee_not_found() throws BackendException {
        run_test_getByUserId(employeeHeader, HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(8)
    public void test_updateDriver_asAdmin_ok() throws BackendException {
        run_test_updateDriver(adminHeader, HttpStatus.OK);
    }

    @Test
    @Order(9)
    public void test_updateDriver_asEmployee_ok() throws BackendException {
        run_test_updateDriver(employeeHeader, HttpStatus.OK);
    }

    @Test
    @Order(10)
    public void test_updateOwnDriver_asMember_ok() throws BackendException {
        run_test_updateDriver(member2Header, HttpStatus.OK);
    }

    @Test
    @Order(11)
    public void test_updateForeignDriver_asMember_unauthorized() throws BackendException {
        run_test_updateDriver(member2Header, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(12)
    public void test_updateUnknownDriver_asEmployee_not_found() throws BackendException {
        run_test_updateDriver(member2Header, HttpStatus.NOT_FOUND);
    }

    private void run_test_getByUserId(Header header, HttpStatus expectedHttpStatus) throws BackendException {
        Account account = accountService.findByEmail(EMAIL_MEMBER_2_ACCOUNT);
        Driver driver = driverService.findByUserId(account.getUser().getId(), false);

        long userId = account.getUser().getId();
        long accountId = account.getId();

        switch (expectedHttpStatus) {
            case UNAUTHORIZED -> userId++;
            case NOT_FOUND -> userId = UNKNOWN_ID;
        }

        String path = getPath(userId, accountId);

        if (expectedHttpStatus == HttpStatus.NOT_FOUND) {
            givenWhenThen_GetRequest_Expect_BackendException(header, path, HttpStatus.NOT_FOUND, new BackendException(ErrorCode.DRIVER_NOT_FOUND, userId));
        } else if (expectedHttpStatus == HttpStatus.UNAUTHORIZED) {
            givenWhenThen_GetRequest(header, path, expectedHttpStatus, equalTo(EMPTY_BODY));
        } else {
            givenWhenThen_GetRequest(header, path, expectedHttpStatus, equalTo(toJsonString(driver)));
        }
    }

    private String getPath(long userId, long accountId) {
        return DRIVER_PATH + "/user/" + userId + "/" + accountId;
    }

    private void run_test_doesDriverExist(String licenseId, HttpStatus httpStatus) {
        final String DRIVER_EXIST_PATH = DRIVER_PATH + "/exist/" + licenseId;
        switch (httpStatus) {
            case OK -> givenWhenThen_GetRequest(DRIVER_EXIST_PATH, HttpStatus.OK, equalTo(EMPTY_BODY));
            case CONFLICT -> {
                final BackendException backendException = new BackendException(ErrorCode.DRIVER_LICENSE_ALREADY_IN_USE, licenseId);
                givenWhenThen_GetRequest_Expect_BackendException(DRIVER_EXIST_PATH, HttpStatus.CONFLICT, backendException);
            }
        }
    }

    private void run_test_updateDriver(Header header, HttpStatus expectedHttpStatus) throws BackendException {
        DriverUpdate driverUpdate = getDriverUpdate(header, expectedHttpStatus);

        String path = getPath(driverUpdate);
        String driverRequestAsJson = constructDriverRequestAsJson(driverUpdate.fareType);

        Driver updatedDriver = updateDriverFareType(driverUpdate);
        String updatedDriverAsJson = toJsonString(updatedDriver);

        if (expectedHttpStatus == HttpStatus.OK || expectedHttpStatus == HttpStatus.UNAUTHORIZED) {
            Matcher<?> matcher = expectedHttpStatus == HttpStatus.UNAUTHORIZED ? equalTo(EMPTY_BODY) : equalTo(updatedDriverAsJson);
            givenWhenThen_PatchRequest(header, driverRequestAsJson, path, expectedHttpStatus, matcher);
        } else {
            BackendException backendException = new BackendException(ErrorCode.DRIVER_NOT_FOUND, driverUpdate.id);
            givenWhenThen_PatchRequest_Expect_BackendException(header, driverRequestAsJson, path, HttpStatus.NOT_FOUND, backendException);
        }

    }

    private String getPath(DriverUpdate driverUpdate) throws BackendException {
        return DRIVER_PATH + "/" + driverUpdate.id + "/" + accountService.findByEmail(driverUpdate.emailOfAccount).getId();
    }

    private String constructDriverRequestAsJson(String fareType) {
        DriverRequest driverRequest = DriverRequest.builder().fareTypeName(fareType).build();
        return toJsonString(driverRequest);
    }

    private Driver updateDriverFareType(DriverUpdate driverUpdate) throws BackendException {
        if (driverUpdate.id != UNKNOWN_ID) {
            Driver driver = driverService.findById(driverUpdate.id);
            driver.setFareType(fareTypeService.getFareTypeByName(driverUpdate.fareType));
            return driver;
        } else {
            return new Driver();
        }
    }

    private DriverUpdate getDriverUpdate(Header header, HttpStatus expectedHttpStatus) throws BackendException {
        long driverId = getDriverId(expectedHttpStatus);

        String fareType = getFareTypeBasedOnHeader(header);
        return new DriverUpdate(driverId, fareType, EMAIL_MEMBER_2_ACCOUNT);
    }

    private long getDriverId(HttpStatus expectedHttpStatus) throws BackendException {

        long driverId = 0;

        switch (expectedHttpStatus) {
            case OK -> {
                Account accountMember2 = accountService.findByEmail(EMAIL_MEMBER_2_ACCOUNT);
                driverId = driverService.findByUserId(accountMember2.getUser().getId(), false).getId();
            }
            case UNAUTHORIZED -> driverId = driverService.findAll().stream().findFirst().get().getId();

            case NOT_FOUND -> driverId = UNKNOWN_ID;
        }

        return driverId;
    }

    private String getFareTypeBasedOnHeader(Header header) {
        if (header == adminHeader) {
            return FARE_TYPE_JUNIOR;
        } else if (header == employeeHeader) {
            return FARE_TYPE_PREMIUM;
        } else {
            return FARE_TYPE_COMFORT;
        }
    }

    private record DriverUpdate(long id, String fareType, String emailOfAccount) {
    }


}