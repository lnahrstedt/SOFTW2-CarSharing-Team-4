package de.flojc.fastlane.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * The `BackendException` class is a custom exception that represents an error in the backend and includes an error code,
 * description, and timestamp.
 */
@Getter
@Setter
@NoArgsConstructor
public class BackendException extends Exception {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private ErrorCode errorCode;
    private String description;

    // The `public BackendException(ErrorCode errorCode, Object... args)` constructor is creating a new instance of the
    // `BackendException` class with the provided `errorCode` and `args`.
    public BackendException(ErrorCode errorCode, Object... args) {
        this.errorCode = errorCode;
        this.description = args != null ? String.format(errorCode.getDescription(), args) : errorCode.getDescription();
    }

    // The `public BackendException(ErrorCode errorCode, Throwable cause, Object... args)` constructor is creating a new
    // instance of the `BackendException` class with the provided `errorCode`, `cause`, and `args`.
    public BackendException(ErrorCode errorCode, Throwable cause, Object... args) {
        super(cause);
        this.errorCode = errorCode;
        this.description = args != null ? String.format(errorCode.getDescription(), args) : errorCode.getDescription();
    }
}
