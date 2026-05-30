package com.strivolabs.strivolabsassessmentjava.auth.domainevents;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.strivolabs.strivolabsassessmentjava.common.abstractions.DomainEvent;

public record EmailConfirmedDomainEvent(
                UUID id,
                OffsetDateTime occurredOn,
                String firstName,
                String email) implements DomainEvent {

}