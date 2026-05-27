// package com.strivolabs.strivolabsassessmentjava.config;

// public class JacksonConfig {

// }

package com.strivolabs.strivolabsassessmentjava.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule()) // handles OffsetDateTime, LocalDate etc
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // writes dates as ISO strings not numbers
    }
}
