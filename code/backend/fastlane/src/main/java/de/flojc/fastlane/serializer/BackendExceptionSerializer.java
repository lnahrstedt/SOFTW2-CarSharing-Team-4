package de.flojc.fastlane.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.flojc.fastlane.exception.BackendException;

import java.io.IOException;

/**
 * The BackendExceptionSerializer class is a custom serializer in Java that converts a BackendException object into a JSON
 * representation.
 */
public class BackendExceptionSerializer extends StdSerializer<BackendException> {

    public BackendExceptionSerializer() {
        this(null);
    }

    public BackendExceptionSerializer(Class<BackendException> t) {
        super(t);
    }

    /**
     * The function serializes a BackendException object into a JSON format.
     *
     * @param backendException The backendException parameter is an instance of the BackendException class, which
     * represents an exception that occurred in the backend of an application. It contains information such as the error
     * code, error name, description, and timestamp of the exception.
     * @param jsonGenerator The jsonGenerator parameter is an instance of the JsonGenerator class, which is used to write
     * JSON data. It provides methods to write various JSON elements such as objects, arrays, strings, numbers, etc.
     * @param provider The `provider` parameter is an instance of `SerializerProvider` class. It provides access to various
     * serialization configuration and helper methods.
     */
    @Override
    public void serialize(BackendException backendException, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("errorCode", backendException.getErrorCode().getCode());
        jsonGenerator.writeStringField("errorName", backendException.getErrorCode().toString());
        jsonGenerator.writeStringField("description", backendException.getDescription());
        jsonGenerator.writeStringField("timestamp", backendException.getTimestamp().toString());
        jsonGenerator.writeEndObject();
    }
}