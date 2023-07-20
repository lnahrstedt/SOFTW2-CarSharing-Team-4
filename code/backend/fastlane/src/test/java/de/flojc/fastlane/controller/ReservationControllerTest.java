package de.flojc.fastlane.controller;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.exception.ErrorCode;
import de.flojc.fastlane.model.Reservation;
import de.flojc.fastlane.model.ReservationState;
import de.flojc.fastlane.request.ReservationRequest;
import de.flojc.fastlane.service.DriverService;
import de.flojc.fastlane.service.ReservationService;
import de.flojc.fastlane.service.ReservationStateService;
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

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import static de.flojc.fastlane.helper.JsonGenerator.toJsonString;
import static helper.HeaderContainer.*;
import static helper.TestConstants.*;
import static helper.TestStrategy.*;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
@Order(7)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationControllerTest {
    private static final String RESERVATION_PATH = "/reservation";

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationStateService reservationStateService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private VehicleService vehicleService;


    @Test
    @Order(1)
    void test_getAllReservations_asAdmin_ok() {
        run_test_getAllReservations(adminHeader, HttpStatus.OK);
    }

    @Test
    @Order(2)
    void test_getAllReservations_asEmployee_ok() {
        run_test_getAllReservations(employeeHeader, HttpStatus.OK);
    }

    @Test
    @Order(3)
    void test_getAllReservations_asMember_ok() {
        run_test_getAllReservations(member2Header, HttpStatus.OK);
    }

    @Test
    @Order(4)
    void test_getReservationById_asAdmin_ok() {
        run_test_getReservationById(adminHeader, HttpStatus.OK);

    }

    @Test
    @Order(5)
    void test_getReservationById_asEmployee_ok() {
        run_test_getReservationById(employeeHeader, HttpStatus.OK);
    }

    @Test
    @Order(6)
    void test_getReservationById_asMember_ok() {
        run_test_getReservationById(member2Header, HttpStatus.OK);
    }

    @Test
    @Order(7)
    void test_getUnknownReservationById_asEmployee_not_found() {
        run_test_getReservationById(employeeHeader, HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(8)
    void test_getReservationsByDriverId_asAdmin_ok() throws BackendException {
        run_test_getReservationsByDriverId(adminHeader, HttpStatus.OK);
    }

    @Test
    @Order(9)
    void test_getReservationsByDriverId_asEmployee_ok() throws BackendException {
        run_test_getReservationsByDriverId(employeeHeader, HttpStatus.OK);
    }

    @Test
    @Order(10)
    void test_getReservationsByDriverId_asMember_ok() throws BackendException {
        run_test_getReservationsByDriverId(member2Header, HttpStatus.OK);
    }

    @Test
    @Order(11)
    void test_getReservationsByUnknownDriverId_asEmployee_not_found() throws BackendException {
        run_test_getReservationsByDriverId(employeeHeader, HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(12)
    void test_getReservationsByVehicleId_asAdmin_ok() throws BackendException {
        run_test_getReservationsByVehicleId(adminHeader, HttpStatus.OK);
    }

    @Test
    @Order(13)
    void test_getReservationsByVehicleId_asEmployee_ok() throws BackendException {
        run_test_getReservationsByVehicleId(employeeHeader, HttpStatus.OK);
    }

    @Test
    @Order(14)
    void test_getReservationsByVehicleId_asMember_ok() throws BackendException {
        run_test_getReservationsByVehicleId(member2Header, HttpStatus.OK);
    }

    @Test
    @Order(15)
    void test_getReservationsByUnknownVehicleId_asEmployee_not_found() throws BackendException {
        run_test_getReservationsByVehicleId(employeeHeader, HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(16)
    void test_saveReservation_asMember_invalid_period_conflict() {
        run_test_saveReservation(member2Header, FailType.INVALID_PERIOD, HttpStatus.CONFLICT);
    }

    @Test
    @Order(17)
    void test_saveReservation_asMember_wrong_price_forbidden() {
        run_test_saveReservation(member2Header, FailType.WRONG_PRICE, HttpStatus.FORBIDDEN);
    }

    @Test
    @Order(18)
    void test_saveReservation_asMember_field_null_bad_request() {
        run_test_saveReservation(member2Header, FailType.FIELD_NULL, HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(19)
    void test_saveReservation_asMember_field_blank_bad_request() {
        run_test_saveReservation(member2Header, FailType.FIELD_BLANK, HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(20)
    void test_saveReservation_asMember_foreign_driver_unauthorized() {
        run_test_saveReservation(member2Header, FailType.SAVE_RESERVATION_FOR_FOREIGN_MEMBER, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(21)
    void test_saveReservation_asMember_unknown_vehicle_id_not_found() {
        run_test_saveReservation(member2Header, FailType.UNKNOWN_VEHICLE_ID, HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(22)
    void test_saveReservation_asMember_created() {
        run_test_saveReservation(member2Header, FailType.NONE, HttpStatus.CREATED);
    }

    @Test
    @Order(23)
    void test_saveReservation_asMember_double_booked_vehicle_conflict() {
        run_test_saveReservation(member2Header, FailType.DOUBLE_RESERVED_VEHICLE, HttpStatus.CONFLICT);
    }

    @Test
    @Order(24)
    void test_saveReservation_asEmployee_created() {
        run_test_saveReservation(employeeHeader, FailType.NONE, HttpStatus.CREATED);
    }

    @Test
    @Order(25)
    void test_saveReservation_asAdmin_created() {
        run_test_saveReservation(adminHeader, FailType.NONE, HttpStatus.CREATED);
    }

    @Test
    @Order(26)
    void test_updateReservation_asEmployee_patch_driver_forbidden() throws BackendException {
        run_test_updateReservation(employeeHeader, FailType.PATCHING_DRIVER, HttpStatus.FORBIDDEN);
    }

    @Test
    @Order(27)
    void test_updateReservation_asEmployee_patch_vehicle_forbidden() throws BackendException {
        run_test_updateReservation(employeeHeader, FailType.PATCHING_VEHICLE, HttpStatus.FORBIDDEN);
    }

    @Test
    @Order(28)
    void test_updateReservation_asEmployee_field_blank_bad_request() throws BackendException {
        run_test_updateReservation(employeeHeader, FailType.FIELD_BLANK, HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(29)
    void test_updateReservation_asAdmin_unknown_reservation_state_not_found() throws BackendException {
        run_test_updateReservation(adminHeader, FailType.UNKNOWN_RESERVATION_STATE, HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(30)
    void test_updateReservation_asAdmin_patch_start_date_forbidden() throws BackendException {
        run_test_updateReservation(adminHeader, FailType.PATCHING_START_DATE, HttpStatus.FORBIDDEN);
    }

    @Test
    @Order(31)
    void test_updateReservation_asAdmin_patch_end_date_forbidden() throws BackendException {
        run_test_updateReservation(adminHeader, FailType.PATCHING_END_DATE, HttpStatus.FORBIDDEN);
    }

    @Test
    @Order(32)
    void test_updateReservation_asAdmin_patch_price_forbidden() throws BackendException {
        run_test_updateReservation(adminHeader, FailType.PATCH_PRICE, HttpStatus.FORBIDDEN);
    }

    @Test
    @Order(33)
    void test_updateReservation_asAdmin_patch_currency_forbidden() throws BackendException {
        run_test_updateReservation(adminHeader, FailType.PATCHING_CURRENCY, HttpStatus.FORBIDDEN);
    }

    @Test
    @Order(34)
    void test_updateReservation_asMember_foreign_driver_unauthorized() throws BackendException {
        run_test_updateReservation(member2Header, FailType.UPDATE_RESERVATION_FOR_FOREIGN_MEMBER, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(35)
    void test_updateReservation_asMember_ok() throws BackendException {
        run_test_updateReservation(member2Header, FailType.NONE, HttpStatus.OK);
    }

    @Test
    @Order(36)
    void test_updateReservation_asEmployee_unknown_reservation_not_found() throws BackendException {
        run_test_updateReservation(employeeHeader, FailType.UNKNOWN_RESERVATION_ID, HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(37)
    void test_updateReservation_asEmployee_ok() throws BackendException {
        run_test_updateReservation(employeeHeader, FailType.NONE, HttpStatus.OK);
    }

    @Test
    @Order(38)
    void test_updateReservation_asAdmin_ok() throws BackendException {
        run_test_updateReservation(adminHeader, FailType.NONE, HttpStatus.OK);
    }

    @Test
    @Order(39)
    void test_deleteReservation_asMember_foreign_Reservation_unauthorized() throws BackendException {
        run_test_deleteReservation(member2Header, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(40)
    void test_deleteUnknownReservation_asEmployee_not_found() throws BackendException {
        run_test_deleteReservation(employeeHeader, HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(41)
    void test_deleteReservation_asMember_own_Account_no_content() throws BackendException {
        run_test_deleteReservation(member2Header, HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(42)
    void test_deleteReservation_asEmployee_no_content() throws BackendException {
        run_test_deleteReservation(employeeHeader, HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(43)
    void test_deleteReservation_asAdmin_no_content() throws BackendException {
        run_test_deleteReservation(adminHeader, HttpStatus.NO_CONTENT);
    }


    private void run_test_getAllReservations(Header header, HttpStatus expectedHttpStatus) {
        final String allReservationsAsJson = toJsonString(reservationService.findAll());
        givenWhenThen_GetRequest(header, RESERVATION_PATH, expectedHttpStatus, equalTo(allReservationsAsJson));
    }


    private void run_test_getReservationById(Header header, HttpStatus expectedHttpStatus) {
        Reservation reservation = reservationService.findAll().stream().findFirst().get();
        long reservationId = reservation.getId();

        switch (expectedHttpStatus) {
            case OK -> {
                final String reservationAsJson = toJsonString(reservation);
                givenWhenThen_GetRequest(header, RESERVATION_PATH + "/" + reservationId, expectedHttpStatus, equalTo(reservationAsJson));
            }
            case NOT_FOUND -> {
                final BackendException userNotFoundBackendException = new BackendException(ErrorCode.RESERVATION_NOT_FOUND, UNKNOWN_ID);
                givenWhenThen_GetRequest_Expect_BackendException(employeeHeader, RESERVATION_PATH + "/" + UNKNOWN_ID, HttpStatus.NOT_FOUND, userNotFoundBackendException);
            }
        }
    }

    private void run_test_getReservationsByDriverId(Header header, HttpStatus expectedHttpStatus) throws BackendException {

        String path = RESERVATION_PATH + "/driver/";

        switch (expectedHttpStatus) {
            case OK -> {
                long driverId = driverService.findAll().stream().findFirst().get().getId();
                String expectedReservationsOfDriverAsJson = toJsonString(reservationService.getReservationsByDriverId(driverId));
                givenWhenThen_GetRequest(header, path + driverId, expectedHttpStatus, equalTo(expectedReservationsOfDriverAsJson));
            }
            case NOT_FOUND -> {
                BackendException backendException = new BackendException(ErrorCode.DRIVER_NOT_FOUND, UNKNOWN_ID);
                givenWhenThen_GetRequest_Expect_BackendException(header, path + UNKNOWN_ID, expectedHttpStatus, backendException);

            }
        }

    }

    private void run_test_getReservationsByVehicleId(Header header, HttpStatus expectedHttpStatus) throws BackendException {

        String path = RESERVATION_PATH + "/vehicle/";

        switch (expectedHttpStatus) {
            case OK -> {
                long vehicleId = vehicleService.findAll().stream().findFirst().get().getId();
                String expectedReservationsOfDriverAsJson = toJsonString(reservationService.getReservationsByVehicleId(vehicleId));
                givenWhenThen_GetRequest(header, path + vehicleId, expectedHttpStatus, equalTo(expectedReservationsOfDriverAsJson));
            }
            case NOT_FOUND -> {
                BackendException backendException = new BackendException(ErrorCode.VEHICLE_NOT_FOUND, UNKNOWN_ID);
                givenWhenThen_GetRequest_Expect_BackendException(header, path + UNKNOWN_ID, expectedHttpStatus, backendException);

            }
        }

    }

    private void run_test_saveReservation(Header header, FailType failType, HttpStatus expectedHttpStatus) {
        Map<FailType, BackendException> exceptionMapping = Map.of(
                FailType.INVALID_PERIOD, new BackendException(ErrorCode.INVALID_PERIOD, START_DATE_TIME, LocalDateTime.MIN),
                FailType.FIELD_NULL, new BackendException(ErrorCode.UNSET_FIELD, "currencyCode"),
                FailType.FIELD_BLANK, new BackendException(ErrorCode.BLANK_FIELD, "reservationState"),
                FailType.UNKNOWN_VEHICLE_ID, new BackendException(ErrorCode.VEHICLE_NOT_FOUND, UNKNOWN_ID),
                FailType.WRONG_PRICE, new BackendException(ErrorCode.RESERVATION_PRICE_DOES_NOT_MATCH, INVALID_RESERVATION_PRICE),
                FailType.DOUBLE_RESERVED_VEHICLE, new BackendException(ErrorCode.VEHICLE_ALREADY_RESERVED, 2, START_DATE_TIME, END_DATE_TIME)

        );

        String reservationRequestAsJson = toJsonString(getReservationRequest(header, failType));

        if (failType == FailType.NONE) {
            givenWhenThen_PostRequest(header, reservationRequestAsJson, RESERVATION_PATH, expectedHttpStatus, equalTo(EMPTY_BODY));
        } else if (failType == FailType.SAVE_RESERVATION_FOR_FOREIGN_MEMBER) {
            givenWhenThen_PostRequest(header, reservationRequestAsJson, RESERVATION_PATH, expectedHttpStatus, equalTo(EMPTY_BODY));
        } else {
            givenWhenThen_PostRequestExpectException(header, reservationRequestAsJson, RESERVATION_PATH, expectedHttpStatus, exceptionMapping.get(failType));
        }
    }

    private ReservationRequest getReservationRequest(Header header, FailType failType) {
        ReservationRequest reservationRequest = header == member2Header
                ? ReservationRequest.builder()
                .vehicleId(failType == FailType.UNKNOWN_VEHICLE_ID ? UNKNOWN_ID : 2L)
                .driverId(failType == FailType.SAVE_RESERVATION_FOR_FOREIGN_MEMBER ? INVALID_DRIVER_ID : VALID_DRIVER_ID)
                .price(failType == FailType.WRONG_PRICE ? INVALID_RESERVATION_PRICE : failType == FailType.INVALID_PERIOD ? RESERVATION_PRICE_INVALID_PERIOD : VALID_RESERVATION_PRICE)
                .currencyCode("CNY")
                .startDateTime(LocalDateTime.parse(START_DATE_TIME))
                .endDateTime(LocalDateTime.parse(END_DATE_TIME))
                .reservationState("PAID")
                .build()
                : header == employeeHeader
                ? ReservationRequest.builder()
                .vehicleId(3L)
                .driverId(VALID_DRIVER_ID)
                .price(VALID_RESERVATION_PRICE)
                .currencyCode("CNY")
                .startDateTime(LocalDateTime.parse(START_DATE_TIME))
                .endDateTime(LocalDateTime.parse(END_DATE_TIME))
                .reservationState("PAID")
                .build()
                : ReservationRequest.builder()
                .vehicleId(4L)
                .driverId(13L)
                .price(VALID_RESERVATION_PRICE2)
                .currencyCode("CNY")
                .startDateTime(LocalDateTime.parse(START_DATE_TIME))
                .endDateTime(LocalDateTime.parse(END_DATE_TIME))
                .reservationState("PAID")
                .build();

        switch (failType) {
            case INVALID_PERIOD -> reservationRequest.setEndDateTime(LocalDateTime.MIN);
            case FIELD_NULL -> reservationRequest.setCurrencyCode(null);
            case FIELD_BLANK -> reservationRequest.setReservationState(" ");
            case NONE -> {
                return reservationRequest;
            }
        }

        return reservationRequest;
    }

    private void run_test_updateReservation(Header header, FailType failType, HttpStatus expectedHttpStatus) throws BackendException {
        final long reservationId = reservationService.findById((long) (reservationService.findAll().size() - 1)).getId();

        String path = RESERVATION_PATH + "/" + reservationId;

        final String currencyCode = "EUR";

        final String reservationState = "PAID";
        final String reservationState2 = "UNPAID";


        ReservationUpdate reservationUpdate = null;

        switch (failType) {
            case FIELD_BLANK -> {
                reservationUpdate = new ReservationUpdate(reservationId, "  ");
                BackendException backendException = new BackendException(ErrorCode.BLANK_FIELD, "reservationState");
                givenWhenThen_PatchRequest_Expect_BackendException(header, reservationUpdate.updateBody, path, expectedHttpStatus, backendException);
            }
            case UNKNOWN_VEHICLE_ID -> {
                path = RESERVATION_PATH + "/" + UNKNOWN_ID;
                reservationUpdate = new ReservationUpdate(reservationId, reservationState2);
                BackendException backendException = new BackendException(ErrorCode.RESERVATION_NOT_FOUND, UNKNOWN_ID);
                givenWhenThen_PatchRequest_Expect_BackendException(header, reservationUpdate.updateBody, path, expectedHttpStatus, backendException);
            }
            case UNKNOWN_RESERVATION_STATE -> {
                reservationUpdate = new ReservationUpdate(reservationId, UNKNOWN_RESERVATION_STATE);
                BackendException backendException = new BackendException(ErrorCode.RESERVATION_STATE_NOT_FOUND, UNKNOWN_RESERVATION_STATE);
                givenWhenThen_PatchRequest_Expect_BackendException(header, reservationUpdate.updateBody, path, expectedHttpStatus, backendException);
            }
            case PATCHING_DRIVER -> {
                reservationUpdate = new ReservationUpdate(reservationId, null, VALID_DRIVER_ID);
                BackendException backendException = new BackendException(ErrorCode.PATCHING_DRIVER_IN_RESERVATION_FORBIDDEN);
                givenWhenThen_PatchRequest_Expect_BackendException(header, reservationUpdate.updateBody, path, expectedHttpStatus, backendException);
            }
            case PATCHING_VEHICLE -> {
                reservationUpdate = new ReservationUpdate(reservationId, VALID_VEHICLE_ID, null);
                BackendException backendException = new BackendException(ErrorCode.PATCHING_VEHICLE_IN_RESERVATION_FORBIDDEN);
                givenWhenThen_PatchRequest_Expect_BackendException(header, reservationUpdate.updateBody, path, expectedHttpStatus, backendException);
            }
            case PATCHING_START_DATE -> {
                reservationUpdate = new ReservationUpdate(reservationId, null, null, LocalDateTime.now(), null);
                BackendException backendException = new BackendException(ErrorCode.PATCHING_DATES_IN_RESERVATION_FORBIDDEN);
                givenWhenThen_PatchRequest_Expect_BackendException(header, reservationUpdate.updateBody, path, expectedHttpStatus, backendException);
            }
            case PATCHING_END_DATE -> {
                reservationUpdate = new ReservationUpdate(reservationId, null, null, null, LocalDateTime.now());
                BackendException backendException = new BackendException(ErrorCode.PATCHING_DATES_IN_RESERVATION_FORBIDDEN);
                givenWhenThen_PatchRequest_Expect_BackendException(header, reservationUpdate.updateBody, path, expectedHttpStatus, backendException);
            }
            case PATCH_PRICE -> {
                reservationUpdate = new ReservationUpdate(reservationId, VALID_RESERVATION_PRICE, null, null, null);
                BackendException backendException = new BackendException(ErrorCode.PATCHING_PRICE_IN_RESERVATION_FORBIDDEN);
                givenWhenThen_PatchRequest_Expect_BackendException(header, reservationUpdate.updateBody, path, expectedHttpStatus, backendException);
            }

            case PATCHING_CURRENCY -> {
                reservationUpdate = new ReservationUpdate(reservationId, null, currencyCode, null, null);
                BackendException backendException = new BackendException(ErrorCode.PATCHING_CURRENCY_IN_RESERVATION_FORBIDDEN);
                givenWhenThen_PatchRequest_Expect_BackendException(header, reservationUpdate.updateBody, path, expectedHttpStatus, backendException);
            }

        }

        switch (expectedHttpStatus) {
            case OK -> {
                if (header == adminHeader) {
                    reservationUpdate = new ReservationUpdate(reservationId, reservationState);
                } else if (header == employeeHeader) {
                    reservationUpdate = new ReservationUpdate(reservationId, reservationState2);
                } else if (header == member2Header) {
                    reservationUpdate = new ReservationUpdate(reservationId, reservationState);
                }

                assert reservationUpdate != null;
                givenWhenThen_PatchRequest(header, reservationUpdate.getUpdateBody(), path, HttpStatus.OK, equalTo(reservationUpdate.getExpectedUser()));
            }
            case UNAUTHORIZED -> {
                if (header == member2Header) {
                    path = RESERVATION_PATH + "/" + (reservationId + 1);
                    reservationUpdate = new ReservationUpdate(reservationId + 1, reservationState2);
                    givenWhenThen_PatchRequest(header, reservationUpdate.getUpdateBody(), path, HttpStatus.UNAUTHORIZED, equalTo(EMPTY_BODY));
                }
            }
        }
    }

    private void run_test_deleteReservation(Header header, HttpStatus expectedHttpStatus) throws BackendException {
        long idOfReservationToBeDeleted = reservationService.findById((long) reservationService.findAll().size()).getId();
        String path = RESERVATION_PATH + "/" + idOfReservationToBeDeleted;

        switch (expectedHttpStatus) {

            case NO_CONTENT -> {
                if (header == employeeHeader) {
                    path = RESERVATION_PATH + "/" + (idOfReservationToBeDeleted - 2);
                }
                if (header == member2Header) {
                    path = RESERVATION_PATH + "/" + (idOfReservationToBeDeleted - 1);
                }
                givenWhenThen_DeleteRequest(header, path, expectedHttpStatus, equalTo(EMPTY_BODY));
            }
            case NOT_FOUND -> {
                path = RESERVATION_PATH + "/" + UNKNOWN_ID;
                BackendException backendException = new BackendException(ErrorCode.RESERVATION_NOT_FOUND, UNKNOWN_ID);
                givenWhenThen_DeleteRequestExpectException(header, path, expectedHttpStatus, backendException);
            }

        }

    }

    @Getter
    private class ReservationUpdate {


        private final long reservationId;

        private final String reservationState;
        private final String updateBody;
        private final String expectedUser;

        public ReservationUpdate(long reservationId, String reservationState) throws BackendException {
            this.reservationId = reservationId;
            this.reservationState = reservationState;
            this.updateBody = toJsonString(new ReservationRequest(null, null, null, null, null, null, reservationState));
            this.expectedUser = getExpectedReservationAsJson(reservationId, reservationState);
        }

        public ReservationUpdate(long reservationId, Long vehicleId, Long driverId) {
            this.reservationId = reservationId;
            this.reservationState = null;
            this.updateBody = toJsonString(new ReservationRequest(vehicleId, driverId, null, null, null, null, null));
            this.expectedUser = null;
        }

        public ReservationUpdate(long reservationId, Double price, String currencyCode, LocalDateTime startDateTime, LocalDateTime endDateTime) {
            this.reservationId = reservationId;
            this.reservationState = null;
            this.updateBody = toJsonString(new ReservationRequest(null, null, price, currencyCode, startDateTime, endDateTime, null));
            this.expectedUser = null;
        }

        private String getExpectedReservationAsJson(long reservationId, String reservationState) throws BackendException {
            Reservation reservation = reservationService.findById(reservationId);
            ReservationState rState = Objects.equals(reservationState, UNKNOWN_RESERVATION_STATE)
                    || Objects.isNull(reservationState)
                    || reservationState.isBlank()
                    ? null : reservationStateService.findReservationStateByName(reservationState);

            if (Objects.nonNull(reservationState)) {
                reservation.setReservationState(rState);
            }

            return toJsonString(reservation);
        }


    }
}