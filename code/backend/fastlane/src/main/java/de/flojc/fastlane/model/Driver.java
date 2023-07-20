package de.flojc.fastlane.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.flojc.fastlane.serializer.EntityIdSerializer;
import de.flojc.fastlane.serializer.FareTypeSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * The Driver class is a Java entity that represents a driver, with properties such as licenseId, reservations, user, and
 * fareType.
 */
@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String licenseId;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "driver")
    private Set<Reservation> reservations;

    @OneToOne
    @ToString.Exclude
    @JsonSerialize(using = EntityIdSerializer.class)
    private User user;

    @ManyToOne
    @ToString.Exclude
    @JsonSerialize(using = FareTypeSerializer.class)
    private FareType fareType;
}
