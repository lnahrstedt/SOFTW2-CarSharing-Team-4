package de.flojc.fastlane.controller;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.request.AuthenticationRequest;
import de.flojc.fastlane.response.AuthenticationResponse;
import de.flojc.fastlane.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The AuthenticationController class is a REST controller that handles login requests and returns a response based on the
 * authentication result.
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * The function handles a login request by validating the user's credentials and returning an authentication response
     * or an error response.
     *
     * @param authenticationRequest The authenticationRequest parameter is an object of type AuthenticationRequest. It is
     * annotated with @RequestBody, which means that it will be deserialized from the request body JSON.
     * @return The method is returning a ResponseEntity object. If the login is successful, it returns
     * ResponseEntity.ok(authenticationResponse), which means a response with HTTP status code 200 (OK) and the
     * authenticationResponse object as the response body. If there is an exception (BackendException), it returns
     * ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e), which means a response with the HTTP status code
     * specified by the error.
     */
    @PostMapping("/login")
    // Tested
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        log.info("Received a login request for the account: {}", authenticationRequest.getEmail());
        try {
            AuthenticationResponse authenticationResponse = authenticationService.login(authenticationRequest);
            log.info("Login for account: {} was successful", authenticationRequest.getEmail());
            return ResponseEntity.ok(authenticationResponse);
        } catch (BackendException e) {
            log.error(e.getDescription());
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e);
        }
    }
}
