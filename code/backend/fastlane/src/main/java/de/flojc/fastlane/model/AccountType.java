package de.flojc.fastlane.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * The AccountType class is a Java entity that represents a type of account and has a one-to-many relationship with the
 * Account class.
 */
@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "accountType")
    private Set<Account> accountSet;
}
