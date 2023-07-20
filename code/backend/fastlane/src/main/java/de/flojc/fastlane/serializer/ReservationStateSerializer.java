package de.flojc.fastlane.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import de.flojc.fastlane.model.ReservationState;

import java.io.IOException;

/**
 * The ReservationStateSerializer class is a custom serializer for the ReservationState class in Java that converts the
 * ReservationState object into a JSON string representation.
 */
public class ReservationStateSerializer extends JsonSerializer<ReservationState> {

    /**
     * The function serializes a ReservationState object by writing its name as a string to a JSON generator.
     *
     * @param reservationState The `reservationState` parameter is an object of type `ReservationState`. It represents the
     * state of a reservation.
     * @param jsonGenerator The jsonGenerator parameter is an object that allows you to write JSON data. It provides
     * methods for writing JSON values, objects, arrays, and more.
     * @param serializerProvider The `serializerProvider` parameter is an object that provides access to the serialization
     * context and configuration. It allows you to access various serialization features and settings, such as the object
     * mapper, serialization view, and serialization configuration.
     */
    @Override
    public void serialize(ReservationState reservationState, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(reservationState.getName());
    }
}