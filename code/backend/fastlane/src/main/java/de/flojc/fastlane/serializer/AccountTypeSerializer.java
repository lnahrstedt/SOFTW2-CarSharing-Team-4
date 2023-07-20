package de.flojc.fastlane.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import de.flojc.fastlane.model.AccountType;

import java.io.IOException;

/**
 * The AccountTypeSerializer class is a custom serializer for the AccountType class in Java that converts an AccountType
 * object into a JSON string representation.
 */
public class AccountTypeSerializer extends JsonSerializer<AccountType> {

    /**
     * The serialize function takes an AccountType object and writes its name as a string to a JSON generator.
     *
     * @param accountType The accountType parameter is an instance of the AccountType class. It represents the type of
     * account that needs to be serialized into JSON format.
     * @param jsonGenerator The jsonGenerator parameter is an object that allows you to write JSON data. It provides
     * methods for writing JSON values, objects, arrays, and more.
     * @param serializerProvider The `serializerProvider` parameter is an object that provides access to various
     * serialization features and services. It is used to configure and customize the serialization process.
     */
    @Override
    public void serialize(AccountType accountType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(accountType.getName());
    }
}