package de.flojc.fastlane.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * The EntityIdSerializer class is a custom JSON serializer that extracts the ID property from an object and serializes it.
 */
public class EntityIdSerializer extends JsonSerializer<Object> {

    /**
     * The serialize function takes an object, retrieves its id using reflection, and writes the id to a JSON generator.
     *
     * @param o The "o" parameter represents the object that needs to be serialized.
     * @param jsonGenerator The `jsonGenerator` parameter is an instance of the `JsonGenerator` class, which is used to
     * write JSON data. It provides methods for writing JSON objects, arrays, strings, numbers, booleans, and null values.
     * In this code snippet, the `writeObject` method is used to
     * @param serializers The `serializers` parameter is an instance of `SerializerProvider`, which is responsible for
     * providing serializers for different types of objects. It is used to access serializers for specific types and to
     * configure serialization settings.
     */
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        try {
            Method idMethod;
            idMethod = o.getClass().getMethod("getId");
            Object id = idMethod.invoke(o);
            jsonGenerator.writeObject(id);
        } catch (Exception e) {
            throw new IOException("Failed to serialize object to id", e);
        }
    }
}