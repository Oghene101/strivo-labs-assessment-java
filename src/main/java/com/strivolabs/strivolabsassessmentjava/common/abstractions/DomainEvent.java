package com.strivolabs.strivolabsassessmentjava.common.abstractions;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(//
        use = JsonTypeInfo.Id.CLASS, // Writes the full Java class name into the JSON
        include = JsonTypeInfo.As.PROPERTY, // Includes it as a standard JSON property field
        property = "@class" // The JSON property key name
)
public interface DomainEvent {

    public UUID id();

    OffsetDateTime occurredOn();
}
