package com.strivolabs.strivolabsassessmentjava.users.domainevents;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.strivolabs.strivolabsassessmentjava.common.abstractions.DomainEvent;

public record UserCreatedDomainEvent(
        UUID id,
        OffsetDateTime occurredOn,
        UUID userId,
        String firstName,
        String email) implements DomainEvent {

}