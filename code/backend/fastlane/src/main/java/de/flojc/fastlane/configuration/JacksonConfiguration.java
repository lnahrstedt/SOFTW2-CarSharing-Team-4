package de.flojc.fastlane.configuration;

import com.fasterxml.jackson.databind.module.SimpleModule;
import de.flojc.fastlane.exception.BackendException;
import de.flojc.fastlane.serializer.BackendExceptionSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The JacksonConfiguration class is a Java configuration class that defines a SimpleModule bean for serializing
 * BackendException objects.
 */
@Configuration
public class JacksonConfiguration {
    @Bean
    public SimpleModule backendExceptionModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(BackendException.class, new BackendExceptionSerializer());
        return module;
    }
}