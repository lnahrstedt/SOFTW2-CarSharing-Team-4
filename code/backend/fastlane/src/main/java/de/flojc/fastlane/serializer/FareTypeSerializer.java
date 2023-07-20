package de.flojc.fastlane.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import de.flojc.fastlane.model.FareType;

import java.io.IOException;

/**
 * The FareTypeSerializer class is a custom serializer for the FareType class in Java that converts FareType objects into
 * JSON format.
 */
public class FareTypeSerializer extends JsonSerializer<FareType> {

    /**
     * The serialize function takes a FareType object and writes its name and price fields to a JSON object using a
     * JsonGenerator.
     *
     * @param fareType The `fareType` parameter is an object of type `FareType`. It contains information about a fare type,
     * such as its name and price.
     * @param jsonGenerator The `jsonGenerator` parameter is an instance of the `JsonGenerator` class, which is used to
     * generate JSON output. It provides methods to write JSON values, objects, arrays, and fields.
     * @param serializerProvider The `serializerProvider` parameter is an object that provides access to the serialization
     * context and configuration. It allows you to access various serialization features and settings, such as the
     * configured serializers for different types.
     */
    @Override
    public void serialize(FareType fareType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("name", fareType.getName());
        jsonGenerator.writeObjectField("price", fareType.getPrice());
        jsonGenerator.writeEndObject();
    }
}