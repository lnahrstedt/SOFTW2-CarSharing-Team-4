package de.flojc.fastlane.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.flojc.fastlane.serializer.AddressSerializer;
import de.flojc.fastlane.serializer.CitySerializer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

/**
 * The User class is a Java entity that represents a user with various attributes such as first name, last name, date of
 * birth, place of birth, accounts, driver information, employee information, and address.
 */
@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    @ManyToOne
    @ToString.Exclude
    @JsonSerialize(using = CitySerializer.class)
    private City placeOfBirth;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private Set<Account> accountSet;

    @ToString.Exclude
    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private Driver driver;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "user")
    private Employee employee;

    @ManyToOne
    @ToString.Exclude
    @JsonSerialize(using = AddressSerializer.class)
    private Address address;
}