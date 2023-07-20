package de.flojc.fastlane.service;

import de.flojc.fastlane.auth.config.JWTService;
import de.flojc.fastlane.auth.token.Token;
import de.flojc.fastlane.auth.token.TokenRepository;
import de.flojc.fastlane.auth.token.TokenType;
import de.flojc.fastlane.model.Account;
import de.flojc.fastlane.response.AuthenticationResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The TokenService class is responsible for generating authentication responses, saving tokens for user accounts, and
 * revoking all tokens for a given account.
 */
/**
 * The TokenService class is responsible for generating authentication responses, saving tokens for user accounts, and
 * revoking all tokens for a given account.
 */
@Slf4j
@Service
@AllArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    private final JWTService jwtService;

    /**
     * The function generates an authentication response for a given account by generating a JWT token and refresh token,
     * revoking all existing user tokens, saving the account token, and returning the authentication response.
     *
     * @param account The "account" parameter is an object of type "Account" which represents a user account. It contains
     * information such as the account ID, email, and other relevant details.
     * @return The method is returning an instance of the `AuthenticationResponse` class.
     */
    public AuthenticationResponse generateAuthenticationResponse(Account account) {
        log.debug("Starting to generate authentication response for account: {}...", account.getEmail());
        var jwtToken = jwtService.generateToken(account);
        var refreshToken = jwtService.generateRefreshToken(account);
        revokeAllUserTokens(account);
        saveAccountToken(account, jwtToken);
        log.debug("Finished generating authentication response for account: {}", account.getEmail());
        return AuthenticationResponse.builder()
                .accountId(account.getId())
                .email(account.getEmail())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * The function saves a JWT token for a given account in a token repository.
     *
     * @param account The "account" parameter is an object of type Account, which represents a user account. It likely
     * contains information such as the user's email, username, password, and other relevant details.
     * @param jwtToken The jwtToken parameter is a string that represents a JSON Web Token (JWT). A JWT is a compact,
     * URL-safe means of representing claims to be transferred between two parties. It typically contains information about
     * the user or client making the request, as well as any additional metadata or permissions associated with the token
     */
    private void saveAccountToken(Account account, String jwtToken) {
        log.debug("Starting to save token for account: {}...", account.getEmail());
        var token = Token.builder()
                .account(account)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
        log.debug("Finished saving token for account: {}", account.getEmail());
    }

    /**
     * The function revokes all tokens associated with a given account.
     *
     * @param account The "account" parameter is an instance of the Account class, which represents a user account. It
     * likely contains information such as the account ID and email.
     */
    private void revokeAllUserTokens(Account account) {
        log.debug("Starting to revoke all tokens for account: {}...", account.getEmail());
        var validAccountTokens = tokenRepository.findAllValidTokenByAccount(account.getId());
        if (validAccountTokens.isEmpty())
            return;
        validAccountTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validAccountTokens);
        log.debug("Finished revoking all tokens for account: {}", account.getEmail());
    }
}