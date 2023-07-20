package de.flojc.fastlane.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// The code snippet is defining an enumeration called `ErrorCode`. Each enum constant represents a specific error code with
// its corresponding HTTP status, description, and code.
@Getter
public enum ErrorCode {

    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "No account found for id/email: %s", 1001),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "No user found for id: %s", 1002),
    DRIVER_NOT_FOUND(HttpStatus.NOT_FOUND, "No driver found for user with id: %s", 1003),
    EMPLOYEE_NOT_FOUND(HttpStatus.NOT_FOUND, "No employee found for id: %s", 1004),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "No reservation found for id: %s", 1005),
    RESERVATION_STATE_NOT_FOUND(HttpStatus.NOT_FOUND, "No reservation state found for name: %s", 1006),
    VEHICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "No vehicle found for id: %s", 1007),
    VEHICLE_BRAND_NOT_FOUND(HttpStatus.NOT_FOUND, "No vehicle brand found for id/name: %s", 1008),
    VEHICLE_MODEL_NOT_FOUND(HttpStatus.NOT_FOUND, "No vehicle model found for id/name: %s", 1009),
    VEHICLE_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "No vehicle category found for id: %s", 1010),
    VEHICLE_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "No vehicle type found for id: %s", 1011),
    VEHICLE_TRANSMISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "No vehicle transmission found for id: %s", 1012),
    VEHICLE_FUEL_NOT_FOUND(HttpStatus.NOT_FOUND, "No vehicle fuel found for id: %s", 1013),
    ACCOUNT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "No account type found for account type name: %s", 1014),
    FARE_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "No fare type found for fare type name: %s", 1015),
    COUNTRY_NOT_FOUND(HttpStatus.NOT_FOUND, "No country found for country code: %s", 1016),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Invalid credentials", 7001),
    PASSWORD_INADEQUATE(HttpStatus.BAD_REQUEST, "Password does not meet the requirements", 7002),

    DRIVER_LICENSE_ALREADY_IN_USE(HttpStatus.CONFLICT, "Provided drivers license id is already in use: %s", 9001),
    EMAIL_ADDRESS_ALREADY_IN_USE(HttpStatus.CONFLICT, "Provided email address is already in use: %s", 9002),
    ACCOUNT_TYPE_USED_TWICE(HttpStatus.CONFLICT, "Account type '%s' already exists for user with id: %s", 9003),
    ACCOUNT_TYPE_CONFLICT(HttpStatus.CONFLICT, "User with id: %s cannot have both an Employee and Admin account concurrently", 9004),
    EMPLOYEE_ALREADY_EXIST(HttpStatus.CONFLICT, "Employee with id: '%s' already exists", 9005),
    VEHICLE_NUMBER_PLATE_ALREADY_EXIST(HttpStatus.CONFLICT, "Vehicle with number plate: %s already exists", 9006),
    VEHICLE_ALREADY_RESERVED(HttpStatus.CONFLICT, "Vehicle with id: %s has already been reserved between: %s and: %s", 9007),
    INVALID_PERIOD(HttpStatus.CONFLICT, "Provided dates are not a proper periode! start %s, end: %s", 9008),
    UNSET_FIELD(HttpStatus.BAD_REQUEST, "Field '%s' must be set", 9009),
    BLANK_FIELD(HttpStatus.BAD_REQUEST, "Field '%s' is blank", 9010),
    PATCHING_DRIVER_IN_RESERVATION_FORBIDDEN(HttpStatus.FORBIDDEN, "Not allowed to patch driver of reservation", 9011),
    PATCHING_VEHICLE_IN_RESERVATION_FORBIDDEN(HttpStatus.FORBIDDEN, "Not allowed to patch vehicle of reservation", 9012),
    PATCHING_PRICE_IN_RESERVATION_FORBIDDEN(HttpStatus.FORBIDDEN, "Not allowed to patch price of reservation", 9013),
    PATCHING_DATES_IN_RESERVATION_FORBIDDEN(HttpStatus.FORBIDDEN, "Not allowed to patch dates of reservation", 9014),
    PATCHING_CURRENCY_IN_RESERVATION_FORBIDDEN(HttpStatus.FORBIDDEN, "Not allowed to patch currency of reservation", 9015),
    RESERVATION_NOT_PAID(HttpStatus.FORBIDDEN, "Cannot delete Account due to unpaid reservation with id {}", 9016),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad request", 10001),

    RESERVATION_PRICE_DOES_NOT_MATCH(HttpStatus.FORBIDDEN, "Given Price: '%s' does not match the calculated price", 11001),

    UNEXPECTED_ERROR(HttpStatus.I_AM_A_TEAPOT, "Oops! Our system took an unscheduled coffee break...", 3676);

    private final int code;
    private final String description;
    private final HttpStatus httpStatus;

    // The `ErrorCode(HttpStatus httpStatus, String description, int code)` constructor is used to initialize the
    // `ErrorCode` enum constants with the provided parameters.
    ErrorCode(HttpStatus httpStatus, String description, int code) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
