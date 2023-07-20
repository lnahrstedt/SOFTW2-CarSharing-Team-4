package de.flojc.fastlane.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import de.flojc.fastlane.model.Account;

import java.io.IOException;

/**
 * The AccountSerializer class is a custom JSON serializer for the Account class, which converts an Account object into a
 * JSON representation.
 */
public class AccountSerializer extends JsonSerializer<Account> {

    /**
     * The function serializes an Account object into a JSON format using a JsonGenerator.
     *
     * @param account The "account" parameter is an instance of the Account class that we want to serialize into JSON
     * format.
     * @param jsonGenerator The jsonGenerator parameter is an instance of the JsonGenerator class, which is used to write
     * JSON data. It provides methods to write various types of JSON values, such as strings, numbers, booleans, objects,
     * and arrays.
     * @param serializerProvider The `serializerProvider` parameter is an object that provides access to various
     * serialization features and configuration settings. It is used to obtain serializers for specific types and to
     * configure serialization behavior.
     */
    @Override
    public void serialize(Account account, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("id", account.getId());
        jsonGenerator.writeStringField("email", account.getEmail());
        jsonGenerator.writeStringField("phone", account.getPhone());
        jsonGenerator.writeStringField("password", account.getPassword());
        jsonGenerator.writeObjectField("user", account.getUser());
        jsonGenerator.writeObjectField("accountType", account.getAccountType().getName());
        jsonGenerator.writeEndObject();
    }
}