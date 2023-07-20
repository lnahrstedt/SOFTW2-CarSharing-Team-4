package de.flojc.fastlane.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import de.flojc.fastlane.model.Configuration;
import de.flojc.fastlane.model.Equipment;

import java.io.IOException;

/**
 * The ConfigurationSerializer class is a Java class that serializes a Configuration object into a JSON format.
 */
public class ConfigurationSerializer extends JsonSerializer<Configuration> {

    /**
     * The serialize function takes a Configuration object and writes its properties to a JSON object using a
     * JsonGenerator.
     *
     * @param configuration The `configuration` parameter is an object of the `Configuration` class. It contains various
     * properties related to a vehicle configuration, such as vehicle category, type, transmission, fuel, and equipment.
     * @param jsonGenerator The `jsonGenerator` parameter is an instance of the `JsonGenerator` class, which is used to
     * generate JSON output. It provides methods to write JSON values, objects, arrays, and fields.
     * @param serializerProvider The `serializerProvider` parameter is an object that provides access to serializers and
     * other serialization-related functionality. It is used to obtain serializers for specific types and to configure
     * serialization settings.
     */
    @Override
    public void serialize(Configuration configuration, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        Equipment equipment = configuration.getEquipment();

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("category", configuration.getVehicleCategory().getName());
        jsonGenerator.writeObjectField("type", configuration.getVehicleType().getName());
        jsonGenerator.writeObjectField("transmission", configuration.getVehicleTransmission().getName());
        jsonGenerator.writeObjectField("fuel", configuration.getVehicleFuel().getName());
        jsonGenerator.writeObjectField("ac", equipment.hasAc());
        jsonGenerator.writeObjectField("navigation", equipment.hasNavigation());
        jsonGenerator.writeObjectField("cruiseControl", equipment.hasCruiseControl());
        jsonGenerator.writeObjectField("drivingAssistent", equipment.hasDrivingAssistant());
        jsonGenerator.writeEndObject();
    }
}