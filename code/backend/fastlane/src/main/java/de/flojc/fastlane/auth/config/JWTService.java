package de.flojc.fastlane.auth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * The JWTService class provides methods for generating and validating JSON Web Tokens (JWTs) using a secret key.
 */
@Service
public class JWTService {

    private static final String SECRET_KEY = "397A24432646294A404E635266556A576E5A7234753778214125442A472D4B61";

    /**
     * The function extracts the email from a token by using the Claims::getSubject method.
     *
     * @param token The "token" parameter is a string that represents a token, typically a JSON Web Token (JWT), which is
     * used for authentication and authorization purposes in web applications.
     * @return The method is returning a String.
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * The function `extractClaim` takes a token and a function that resolves claims, and returns the result of applying
     * the claims resolver function to the claims extracted from the token.
     *
     * @param token The token parameter is a string that represents a token, typically a JSON Web Token (JWT).
     * @param claimsResolver The `claimsResolver` parameter is a `Function` that takes a `Claims` object as input and
     * returns a value of type `T`. It is used to extract specific information from the `Claims` object. The `extractClaim`
     * method calls this `claimsResolver` function with the `Claims
     * @return The method is returning the result of applying the claimsResolver function to the claims object.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * The function generates a token for a given user details.
     *
     * @param userDetails The userDetails parameter is an object that contains information about the user, such as their
     * username, password, and any other relevant details.
     * @return The method is returning a String value.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * The function generates a token with extra claims and user details, with a JWT expiration time of 86400000
     * milliseconds.
     *
     * @param extraClaims A map containing additional claims to be included in the generated token. Claims are key-value
     * pairs that provide additional information about the token or the user associated with the token.
     * @param userDetails The `userDetails` parameter is an object that contains information about the user. It typically
     * includes details such as the user's username, password, roles, and other relevant information.
     * @return The method is returning a string, which is the generated token.
     */
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        long jwtExpiration = 86400000;
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * The function generates a refresh token for a given user with no expiration time.
     *
     * @param userDetails The `userDetails` parameter is an object that contains information about the user. It typically
     * includes details such as the user's username, password, roles, and other relevant information.
     * @return The method is returning a String value.
     */
    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        long refreshExpiration = Long.MAX_VALUE;
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    /**
     * The function builds a JSON Web Token (JWT) with extra claims, user details, and expiration time.
     *
     * @param extraClaims The extraClaims parameter is a Map that contains additional claims to be included in the JWT
     * (JSON Web Token). Claims are statements about an entity (typically, the user) and additional information. These
     * claims can be used to provide additional context or data about the user.
     * @param userDetails The `userDetails` parameter is an object that contains information about the user. It typically
     * includes details such as the user's username, password, roles, and other relevant information.
     * @param expiration The "expiration" parameter is the duration in milliseconds for which the token will be valid. It
     * represents the time after which the token will expire and become invalid.
     * @return The method is returning a String.
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * The function checks if a token is valid by comparing the email extracted from the token with the username in the
     * user details and checking if the token is not expired.
     *
     * @param token A string representing a token that is used for authentication or authorization purposes.
     * @param userDetails The `userDetails` parameter is an object that contains information about the user. It typically
     * includes details such as the user's username, password, and any additional attributes or roles associated with the
     * user. In this case, the `userDetails` object is expected to have a `getUsername()` method
     * @return The method is returning a boolean value.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * The function checks if a given token has expired by comparing its expiration date with the current date.
     *
     * @param token The "token" parameter is a string that represents a token, typically used for authentication or
     * authorization purposes.
     * @return The method is returning a boolean value.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * The function "extractExpiration" extracts the expiration date from a token using the "extractClaim" function.
     *
     * @param token The "token" parameter is a string that represents a token, typically a JSON Web Token (JWT).
     * @return The method is returning a Date object.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * The function extracts all claims from a JWT token using a signing key.
     *
     * @param token The "token" parameter is a string that represents a JSON Web Token (JWT).
     * @return The method is returning a Claims object.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * The function returns a Key object for signing in, using a secret key that is decoded from a base64 string.
     *
     * @return The method is returning a private key.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
