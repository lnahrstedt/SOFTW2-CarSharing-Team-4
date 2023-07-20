package de.flojc.fastlane.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The `LocalDateAttributeConverter` class is a Java class that converts `LocalDate` objects to `String` for database
 * storage and vice versa, using a specified date format pattern.
 */
@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, String> {

    @Value("${date-format}")
    private String pattern;

    /**
     * The function converts a LocalDate object to a formatted string representation.
     *
     * @param locDate locDate is a LocalDate object that represents a date.
     * @return The method is returning a String representation of the given LocalDate object, formatted according to the
     * specified pattern. If the locDate parameter is null, then the method returns null.
     */
    @Override
    public String convertToDatabaseColumn(LocalDate locDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return (locDate == null ? null : locDate.format(dateTimeFormatter));
    }

    /**
     * The function converts a string representation of a date in a specific format to a LocalDate object.
     *
     * @param sqlDate The `sqlDate` parameter is a string representation of a date in SQL format.
     * @return The method is returning a LocalDate object.
     */
    /**
     * The function converts a string representation of a date in SQL format to a LocalDate object in Java.
     *
     * @param sqlDate The `sqlDate` parameter is a string representation of a date in SQL format.
     * @return The method is returning a LocalDate object.
     */
    @Override
    public LocalDate convertToEntityAttribute(String sqlDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return (sqlDate == null ? null : LocalDate.parse(sqlDate, dateTimeFormatter));
    }
}