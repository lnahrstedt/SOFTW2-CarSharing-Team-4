package de.flojc.fastlane.controller;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.model.Vehicle;
import de.flojc.fastlane.request.VehicleRequest;
import de.flojc.fastlane.service.VehicleService;
import helper.FailType;
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

import java.util.Map;
import java.util.Objects;

import static de.flojc.fastlane.helper.JsonGenerator.toJsonString;
import static helper.HeaderContainer.*;
import static helper.TestConstants.*;
import static helper.TestStrategy.*;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
@Order(9)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VehicleControllerTest {
    private static final String VEHICLE_PATH = "/vehicle";

    @Autowired
    private VehicleService vehicleService;

    @Test
    @Order(1)
    void test_getAllVehicles_asAdmin_ok() {
        run_test_getAllVehicles(adminHeader, HttpStatus.OK);
    }

    @Test
    @Order(2)
    void test_getAllVehicles_asEmployee_ok() {
        run_test_getAllVehicles(employeeHeader, HttpStatus.OK);
    }

    @Test
    @Order(3)
    void test_getAllVehicles_asMember_ok() {
        run_test_getAllVehicles(member2Header, HttpStatus.OK);
    }

    @Test
    @Order(4)
    void test_getVehicleById_asMember_ok() throws BackendException {
        run_test_getVehicleById(member2Header, HttpStatus.OK);
    }

    @Test
    @Order(5)
    void test_getVehicleById_asEmployee_ok() throws BackendException {
        run_test_getVehicleById(employeeHeader, HttpStatus.OK);
    }

    @Test
    @Order(6)
    void test_getVehicleById_asAdmin_ok() throws BackendException {
        run_test_getVehicleById(adminHeader, HttpStatus.OK);

    }

    @Test
    @Order(7)
    void test_getUnknownVehicleById_asMember_not_found() throws BackendException {
        run_test_getVehicleById(member2Header, HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(8)
    void test_saveVehicle_asEmployee_number_plate_used_twice_conflict() {
        run_test_saveVehicle(employeeHeader, FailType.NUMBER_PLATE_USED_TWICE, HttpStatus.CONFLICT);
    }

    @Test
    @Order(9)
    void test_saveVehicle_asEmployee_field_null_bad_request() {
        run_test_saveVehicle(employeeHeader, FailType.FIELD_NULL, HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(10)
    void test_saveVehicle_asEmployee_field_blank_bad_request() {
        run_test_saveVehicle(employeeHeader, FailType.FIELD_BLANK, HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(11)
    void test_saveVehicle_asMember_unauthorized() {
        run_test_saveVehicle(member2Header, FailType.NONE, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(12)
    void test_saveVehicle_asEmployee_created() {
        run_test_saveVehicle(employeeHeader, FailType.NONE, HttpStatus.CREATED);
    }

    @Test
    @Order(13)
    void test_saveVehicle_asAdmin_created() {
        run_test_saveVehicle(adminHeader, FailType.NONE, HttpStatus.CREATED);
    }

    @Test
    @Order(14)
    void test_updateVehicle_asMember_unauthorized() throws BackendException {
        run_test_updateVehicle(member2Header, FailType.NONE, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(15)
    void test_updateVehicle_asEmployee_field_blank_bad_request() throws BackendException {
        run_test_updateVehicle(employeeHeader, FailType.FIELD_BLANK, HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(16)
    void test_updateUnknownVehicle_asEmployee_not_found() throws BackendException {
        run_test_updateVehicle(employeeHeader, FailType.UNKNOWN_VEHICLE_ID, HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(17)
    void test_updateVehicle_asEmployee_ok() throws BackendException {
        run_test_updateVehicle(employeeHeader, FailType.NONE, HttpStatus.OK);
    }

    @Test
    @Order(18)
    void test_updateVehicle_asAdmin_ok() throws BackendException {
        run_test_updateVehicle(adminHeader, FailType.NONE, HttpStatus.OK);
    }

    @Test
    @Order(19)
    void test_deleteVehicle_asMember_unauthorized() {
        run_test_deleteVehicle(member2Header, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(20)
    void test_deleteUnknownVehicle_asEmployee_not_found() {
        run_test_deleteVehicle(employeeHeader, HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(21)
    void test_deleteVehicle_asEmployee_no_content() {
        run_test_deleteVehicle(employeeHeader, HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(22)
    void test_deleteVehicle_asAdmin_no_content() {
        run_test_deleteVehicle(adminHeader, HttpStatus.NO_CONTENT);
    }

    private void run_test_getAllVehicles(Header header, HttpStatus expectedHttpStatus) {
        final String allVehiclesAsJson = toJsonString(vehicleService.findAll());
        givenWhenThen_GetRequest(header, VEHICLE_PATH, expectedHttpStatus, equalTo(allVehiclesAsJson));
    }


    private void run_test_getVehicleById(Header header, HttpStatus expectedHttpStatus) throws BackendException {
        switch (expectedHttpStatus) {
            case OK -> {
                final long vehicleId = vehicleService.findAll().stream().findFirst().get().getId();
                final String vehicleAsJson = toJsonString(vehicleService.findById(vehicleId));
                givenWhenThen_GetRequest(header, VEHICLE_PATH + "/" + vehicleId, expectedHttpStatus, equalTo(vehicleAsJson));
            }
            case NOT_FOUND -> {
                final BackendException userNotFoundBackendException = new BackendException(ErrorCode.VEHICLE_NOT_FOUND, UNKNOWN_ID);
                givenWhenThen_GetRequest_Expect_BackendException(header, VEHICLE_PATH + "/" + UNKNOWN_ID, HttpStatus.NOT_FOUND, userNotFoundBackendException);
            }
        }
    }

    private void run_test_saveVehicle(Header header, FailType failType, HttpStatus expectedHttpStatus) {
        Map<FailType, BackendException> exceptionMapping = Map.of(
                FailType.NUMBER_PLATE_USED_TWICE, new BackendException(ErrorCode.VEHICLE_NUMBER_PLATE_ALREADY_EXIST, USED_NUMBER_PLATE),
                FailType.FIELD_NULL, new BackendException(ErrorCode.UNSET_FIELD, "numberPlate"),
                FailType.FIELD_BLANK, new BackendException(ErrorCode.BLANK_FIELD, "vehicleTransmission")
        );

        String vehicleRequestAsJson = toJsonString(getVehicleRequest(header, failType));

        if (failType == FailType.NONE) {
            givenWhenThen_PostRequest(header, vehicleRequestAsJson, VEHICLE_PATH, expectedHttpStatus, equalTo(EMPTY_BODY));
        } else {
            givenWhenThen_PostRequestExpectException(header, vehicleRequestAsJson, VEHICLE_PATH, expectedHttpStatus, exceptionMapping.get(failType));
        }
    }

    private void run_test_updateVehicle(Header header, FailType failType, HttpStatus expectedHttpStatus) throws BackendException {
        final long vehicleId = vehicleService.findAll().stream().findFirst().get().getId();

        String path = VEHICLE_PATH + "/" + vehicleId;

        final String newNumberPlate = "20-2D-15-8B-05-7B";
        final String newNumberPlate2 = "A9-DA-BF-AC-D0-97";
        final String newNumberPlate3 = "E0-D7-F1-FC-9B-44";


        VehicleUpdate vehicleUpdate = null;

        switch (failType) {
            case FIELD_BLANK -> {
                vehicleUpdate = new VehicleUpdate(vehicleId, newNumberPlate, ' ');
                BackendException backendException = new BackendException(ErrorCode.BLANK_FIELD, "vehicleTransmission");
                givenWhenThen_PatchRequest_Expect_BackendException(header, vehicleUpdate.updateBody, path, expectedHttpStatus, backendException);
            }
            case UNKNOWN_VEHICLE_ID -> {
                path = VEHICLE_PATH + "/" + UNKNOWN_ID;
                vehicleUpdate = new VehicleUpdate(vehicleId, newNumberPlate);
                BackendException backendException = new BackendException(ErrorCode.VEHICLE_NOT_FOUND, UNKNOWN_ID);
                givenWhenThen_PatchRequest_Expect_BackendException(header, vehicleUpdate.updateBody, path, expectedHttpStatus, backendException);

            }
        }

        switch (expectedHttpStatus) {
            case OK -> {
                if (header == adminHeader) {
                    vehicleUpdate = new VehicleUpdate(vehicleId, newNumberPlate);
                } else if (header == employeeHeader) {
                    vehicleUpdate = new VehicleUpdate(vehicleId, newNumberPlate2);
                } else if (header == member2Header) {
                    vehicleUpdate = new VehicleUpdate(vehicleId, newNumberPlate3);
                }

                assert vehicleUpdate != null;
                givenWhenThen_PatchRequest(header, vehicleUpdate.getUpdateBody(), path, HttpStatus.OK, equalTo(vehicleUpdate.getExpectedVehicle()));
            }
            case UNAUTHORIZED -> {
                if (header == member2Header) {
                    vehicleUpdate = new VehicleUpdate(vehicleId, newNumberPlate3);
                    givenWhenThen_PatchRequest(header, vehicleUpdate.getUpdateBody(), path, HttpStatus.UNAUTHORIZED, equalTo(EMPTY_BODY));
                }
            }
        }
    }

    private void run_test_deleteVehicle(Header header, HttpStatus expectedHttpStatus) {
        long idOfVehicleToBeDeleted = vehicleService.findAll().stream().findFirst().get().getId();
        String path = VEHICLE_PATH + "/" + idOfVehicleToBeDeleted;

        switch (expectedHttpStatus) {
            case NO_CONTENT, UNAUTHORIZED ->
                    givenWhenThen_DeleteRequest(header, path, expectedHttpStatus, equalTo(EMPTY_BODY));
            case NOT_FOUND -> {
                path = VEHICLE_PATH + "/" + UNKNOWN_ID;
                BackendException backendException = new BackendException(ErrorCode.VEHICLE_NOT_FOUND, UNKNOWN_ID);
                givenWhenThen_DeleteRequestExpectException(header, path, expectedHttpStatus, backendException);
            }

        }

    }

    private VehicleRequest getVehicleRequest(Header header, FailType failType) {

        VehicleRequest vehicleRequest = header == employeeHeader
                ? VehicleRequest.builder()
                .vehicleModel("Arteon")
                .numberPlate("22-8A-E4-73-26-98")
                .mileage(618692D)
                .latitude(53.086D)
                .longitude(8.7842)
                .constructionYear((short) 2000)
                .vehicleCategory('G')
                .vehicleType('V')
                .vehicleTransmission('A')
                .vehicleFuel('L')
                .hasAc(true)
                .hasNavigation(true)
                .hasCruiseControl(false)
                .hasDrivingAssistant(false)
                .build()
                : VehicleRequest.builder()
                .vehicleModel("e-Up!")
                .numberPlate("34-52-B3-70-3B-05")
                .mileage(647450D)
                .latitude(53.0914D)
                .longitude(8.791)
                .constructionYear((short) 1987)
                .vehicleCategory('S')
                .vehicleType('D')
                .vehicleTransmission('M')
                .vehicleFuel('E')
                .hasAc(true)
                .hasNavigation(false)
                .hasCruiseControl(false)
                .hasDrivingAssistant(true)
                .build();


        switch (failType) {
            case NUMBER_PLATE_USED_TWICE -> vehicleRequest.setNumberPlate(USED_NUMBER_PLATE);
            case FIELD_NULL -> vehicleRequest.setNumberPlate(null);
            case FIELD_BLANK -> vehicleRequest.setVehicleTransmission(' ');
            case NONE -> {
                return vehicleRequest;
            }
        }

        return vehicleRequest;
    }

    @Getter
    private class VehicleUpdate {


        private final long vehicleId;
        private final String numberPlate;
        private final String updateBody;
        private final String expectedVehicle;

        public VehicleUpdate(long vehicleId, String numberPlate) throws BackendException {
            this.vehicleId = vehicleId;
            this.numberPlate = numberPlate;
            this.updateBody = toJsonString(new VehicleRequest(null, numberPlate, null, null, null, null, null, null, null, null, null, null, null, null));
            this.expectedVehicle = getExpectedVehicleAsJson(vehicleId, numberPlate);
        }

        public VehicleUpdate(long vehicleId, String numberPlate, Character vehicleTransmission) throws BackendException {
            this.vehicleId = vehicleId;
            this.numberPlate = numberPlate;
            this.updateBody = toJsonString(new VehicleRequest(null, numberPlate, null, null, null, null, null, null, vehicleTransmission, null, null, null, null, null));
            this.expectedVehicle = getExpectedVehicleAsJson(vehicleId, numberPlate);
        }

        private String getExpectedVehicleAsJson(long vehicleId, String numberPlate) throws BackendException {
            Vehicle vehicle = vehicleService.findById(vehicleId);

            if (Objects.nonNull(numberPlate)) {
                vehicle.setNumberPlate(numberPlate);
            }

            return toJsonString(vehicle);
        }

    }

}