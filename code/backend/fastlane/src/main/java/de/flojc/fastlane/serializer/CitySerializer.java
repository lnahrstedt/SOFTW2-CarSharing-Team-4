package de.flojc.fastlane.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import de.flojc.fastlane.model.City;

import java.io.IOException;

/**
 * The CitySerializer class is a custom serializer for the City class in Java that converts a City object into a JSON
 * string representation by serializing its name property.
 */
public class CitySerializer extends JsonSerializer<City> {

    /**
     * The serialize function takes a City object and writes its name as a string to a JSON generator.
     *
     * @param city The `city` parameter is an object of type `City` that represents a city.
     * @param jsonGenerator The jsonGenerator parameter is an object that allows you to write JSON data. It provides
     * methods for writing JSON values, objects, arrays, and more.
     * @param serializerProvider The `serializerProvider` parameter is an object that provides access to various
     * serialization features and services. It is used to configure and customize the serialization process.
     */
    @Override
    public void serialize(City city, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(city.getName());
    }
}