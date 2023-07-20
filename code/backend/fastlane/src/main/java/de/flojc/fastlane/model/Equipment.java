package de.flojc.fastlane.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * The Equipment class represents a piece of equipment with various features, such as air conditioning, navigation, cruise
 * control, and driving assistant, and it can be associated with multiple configurations.
 */
@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Accessors(fluent = true)
    @JsonFormat(shape = JsonFormat.Shape.BOOLEAN)
    private Boolean hasAc;


    @Accessors(fluent = true)
    @JsonFormat(shape = JsonFormat.Shape.BOOLEAN)
    private Boolean hasNavigation;


    @Accessors(fluent = true)
    @JsonFormat(shape = JsonFormat.Shape.BOOLEAN)
    private Boolean hasCruiseControl;

    @Accessors(fluent = true)
    @JsonFormat(shape = JsonFormat.Shape.BOOLEAN)
    private Boolean hasDrivingAssistant;

    @ToString.Exclude
    @OneToMany(mappedBy = "equipment")
    private Set<Configuration> configurations;
}