package de.flojc.fastlane.auth.config;

import de.flojc.fastlane.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * The ApplicationConfig class is responsible for configuring the authentication and user details services for the
 * application.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final AccountRepository accountRepository;

    /**
     * The function returns a UserDetailsService implementation that retrieves a user account by email from an account
     * repository.
     *
     * @return The method is returning an instance of UserDetailsService.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> accountRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found!"));
    }

    /**
     * The function returns an instance of an AuthenticationProvider that uses a DaoAuthenticationProvider with a
     * userDetailsService and passwordEncoder.
     *
     * @return The method is returning an instance of the AuthenticationProvider interface.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * The function returns an instance of the AuthenticationManager interface based on the provided
     * AuthenticationConfiguration.
     *
     * @param configuration The `configuration` parameter is an instance of `AuthenticationConfiguration`. It is used to
     * configure and create an `AuthenticationManager` bean. The `AuthenticationConfiguration` class provides methods to
     * customize the authentication manager, such as setting the authentication providers, configuring the authentication
     * details source, and specifying the authentication event publisher
     * @return The method is returning an instance of the AuthenticationManager interface.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * The function returns a BCryptPasswordEncoder object, which is used for encoding passwords in Java.
     *
     * @return The method is returning a new instance of the BCryptPasswordEncoder class.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
