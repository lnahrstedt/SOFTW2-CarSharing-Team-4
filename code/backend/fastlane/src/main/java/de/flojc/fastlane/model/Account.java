package de.flojc.fastlane.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.flojc.fastlane.response.UpdateAccountResponse;
import de.flojc.fastlane.serializer.AccountSerializer;
import de.flojc.fastlane.serializer.AccountTypeSerializer;
import de.flojc.fastlane.serializer.EntityIdSerializer;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * The `Account` class is a Java entity that represents a user account with properties such as email, password, phone,
 * creation date, user, and account type.
 */
@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(using = AccountSerializer.class)
public class Account implements UserDetails, UpdateAccountResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;
    private String password;
    private String phone;
    private LocalDate creationDate;

    @ManyToOne()
    @ToString.Exclude
    @JsonSerialize(using = EntityIdSerializer.class)
    private User user;

    @ManyToOne()
    @ToString.Exclude
    @JsonSerialize(using = AccountTypeSerializer.class)
    private AccountType accountType;

    /**
     * The function returns a collection of granted authorities based on the account type.
     *
     * @return The method is returning a collection of GrantedAuthority objects.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(accountType.getName()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}