package de.flojc.fastlane.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * The JsonGenerator class provides a static method toJsonString() that converts an object to a JSON string using the
 * Jackson library.
 */
public abstract class JsonGenerator {

    /**
     * The function converts an object to a JSON string using the Jackson library in Java.
     *
     * @param o The parameter "o" is an object that you want to convert to a JSON string.
     * @return The method is returning a JSON string representation of the given object.
     */
    public static String toJsonString(Object o) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
