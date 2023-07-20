package de.flojc.fastlane.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import de.flojc.fastlane.model.Address;

import java.io.IOException;

/**
 * The AddressSerializer class is a custom serializer for the Address class that converts an Address object into a JSON
 * representation.
 */
public class AddressSerializer extends JsonSerializer<Address> {

    /**
     * The function serializes an Address object into a JSON format.
     *
     * @param address The "address" parameter is an instance of the Address class, which contains information about a
     * specific address, such as the street, postal code, city, and country.
     * @param jsonGenerator The `jsonGenerator` parameter is an instance of the `JsonGenerator` class, which is used to
     * generate JSON output. It provides methods to write JSON values, objects, arrays, and other JSON constructs.
     * @param serializerProvider The `serializerProvider` parameter is an object that provides contextual information and
     * helper methods for the serialization process. It allows you to access configuration settings, such as the currently
     * active serialization view, and provides methods for resolving serializers for specific types.
     */
    @Override
    public void serialize(Address address, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("street", address.getStreet());
        jsonGenerator.writeObjectField("postalCode", address.getLocation().getPostalCode());
        jsonGenerator.writeObjectField("city", address.getLocation().getCity().getName());
        jsonGenerator.writeObjectField("country", address.getLocation().getCountry().getName());
        jsonGenerator.writeEndObject();
    }
}