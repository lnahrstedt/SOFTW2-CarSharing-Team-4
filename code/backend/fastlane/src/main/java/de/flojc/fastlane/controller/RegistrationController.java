package de.flojc.fastlane.controller;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.request.RegisterRequest;
import de.flojc.fastlane.response.AuthenticationResponse;
import de.flojc.fastlane.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The RegistrationController class is a REST controller that handles registration requests for members and employees.
 */
@Slf4j
@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    /**
     * The function `registerMember` registers a new member by calling the `register` method of the `registrationService`
     * and returns the authentication response or an error response.
     *
     * @param newMember The `newMember` parameter is of type `RegisterRequest`. It is an object that contains the
     * information needed to register a new member. The `@RequestBody` annotation indicates that the `newMember` object
     * will be populated with the JSON data from the request body.
     * @return The method is returning a ResponseEntity object.
     */
    @PostMapping("/member")
    //Tested
    public ResponseEntity<?> registerMember(@RequestBody RegisterRequest newMember) {
        log.info("Received request to register a member with email: {}", newMember.getEmail());
        try {
            AuthenticationResponse authenticationResponse = registrationService.register(newMember, true);
            log.info("Account registration for email: {} was successful", newMember.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(authenticationResponse);
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }

    /**
     * The function `registerEmployee` registers a new employee by calling the `register` method of the
     * `registrationService` and returns a response entity with the authentication response or an error response.
     *
     * @param newEmployee The parameter `newEmployee` is of type `RegisterRequest`. It is annotated with `@RequestBody`,
     * which means that the data for this parameter will be received in the request body of the HTTP POST request. The
     * `RegisterRequest` class is a custom class that represents the data required to register a
     * @return The method is returning a ResponseEntity object. The response entity can contain a body, which in this case
     * is an AuthenticationResponse object if the registration is successful. The response entity also has an HTTP status
     * code, which is set to HttpStatus.CREATED if the registration is successful.
     */
    @PostMapping("/employee")
    //Tested
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> registerEmployee(@RequestBody RegisterRequest newEmployee) {
        log.info("Received request to register a {} with email: {}", newEmployee.getTypeName(), newEmployee.getEmail());
        try {
            AuthenticationResponse authenticationResponse = registrationService.register(newEmployee, false);
            log.info("Account registration for email: {} was successful", newEmployee.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(authenticationResponse);
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }
}