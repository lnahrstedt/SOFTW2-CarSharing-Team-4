package de.flojc.fastlane.exception;

import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


/**
 * The GlobalExceptionHandler class handles different types of exceptions and returns appropriate responses for each.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * The function handles a ServletException by logging an error message and returning a ResponseEntity with a
     * BackendException and its corresponding HttpStatus.
     *
     * @return A ResponseEntity object is being returned.
     */
    @ExceptionHandler(ServletException.class)
    public ResponseEntity<?> handleServletException() {
        log.error("A client send a bad request");
        BackendException backendException = new BackendException(
                ErrorCode.BAD_REQUEST);
        return new ResponseEntity<>(backendException, backendException.getErrorCode().getHttpStatus());
    }

    /**
     * The function handles a MethodArgumentTypeMismatchException by logging an error message and returning a
     * ResponseEntity with a BackendException and its corresponding HttpStatus.
     *
     * @return A ResponseEntity object is being returned.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException() {
        log.error("A client send a bad request");
        BackendException backendException = new BackendException(
                ErrorCode.BAD_REQUEST);
        return new ResponseEntity<>(backendException, backendException.getErrorCode().getHttpStatus());
    }

    /**
     * The function handles an AccessDeniedException by logging an error message and returning an HTTP 401 Unauthorized
     * response.
     *
     * @return The method is returning a ResponseEntity object.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException() {
        log.error("A client tried to access an endpoint to which they are not authorized.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * The above function is a generic exception handler in Java that logs the error, creates a custom exception object,
     * and returns a response entity with the exception details.
     *
     * @param exception The "exception" parameter is the exception object that was thrown during the execution of the code.
     * It represents the specific type of exception that occurred, such as a NullPointerException or an IOException.
     * @return A ResponseEntity object is being returned.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BackendException> handleGenericException(Exception exception) {
        log.error("Internal server error occurred", exception);
        BackendException backendException = new BackendException(
                ErrorCode.UNEXPECTED_ERROR, exception);
        return new ResponseEntity<>(backendException, backendException.getErrorCode().getHttpStatus());
    }
}