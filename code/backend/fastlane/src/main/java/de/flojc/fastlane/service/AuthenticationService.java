package de.flojc.fastlane.service;

import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.model.Account;
import de.flojc.fastlane.request.AuthenticationRequest;
import de.flojc.fastlane.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * The AuthenticationService class handles the authentication process for user login.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountService accountService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    /**
     * The login function performs authentication for a user based on their email and password, and returns an
     * authentication response.
     *
     * @param authenticationRequest An object of type AuthenticationRequest that contains the email and password for
     * authentication.
     * @return The method is returning an object of type AuthenticationResponse.
     */
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) throws BackendException {
        log.debug("Starting authentication process for account with email: {}...", authenticationRequest.getEmail());
        authenticationRequest.validateFieldsNotNullOrBlank();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new AccessDeniedException("");
        }
        Account account = accountService.findByEmail(authenticationRequest.getEmail());
        AuthenticationResponse authenticationResponse = tokenService.generateAuthenticationResponse(account);
        log.debug("Finished authentication process for account with email: {}", authenticationRequest.getEmail());
        return authenticationResponse;
    }
}