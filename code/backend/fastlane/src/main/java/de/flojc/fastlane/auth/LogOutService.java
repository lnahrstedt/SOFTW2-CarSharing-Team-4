package de.flojc.fastlane.auth;

import de.flojc.fastlane.auth.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

/**
 * The `LogOutService` class is a Java service that handles the logout process by invalidating and revoking a JWT token
 * stored in a repository.
 */
@Service
@RequiredArgsConstructor
public class LogOutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    /**
     * The `logout` function handles the logout process by invalidating and revoking the JWT token, and clearing
     * the security context.
     *
     * @param request The `HttpServletRequest` object represents the HTTP request made by the client. It contains
     * information such as the request method, headers, parameters, and body.
     * @param response The `response` parameter is an instance of the `HttpServletResponse` class, which represents the
     * response that will be sent back to the client. It is used to manipulate the response headers and body.
     * @param authentication The authentication parameter represents the authenticated user's information and credentials.
     * It contains details such as the user's username, password, authorities, and other authentication-related
     * information.
     */
    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return;
        }
        jwt = authHeader.substring(7);
        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}