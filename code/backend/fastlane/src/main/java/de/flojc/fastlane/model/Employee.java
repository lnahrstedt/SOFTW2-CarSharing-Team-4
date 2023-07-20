package de.flojc.fastlane.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.flojc.fastlane.serializer.EntityIdSerializer;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.*;

/**
 * The Employee class is a Java entity that represents an employee and has a one-to-one relationship with a User entity.
 */
@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    private String id;

    @OneToOne
    @ToString.Exclude
    @JsonSerialize(using = EntityIdSerializer.class)
    private User user;
}