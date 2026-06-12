package com.strivolabs.strivolabsassessmentjava.users.domainevents;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.strivolabs.strivolabsassessmentjava.common.abstractions.DomainEvent;

public final record UserCreatedDomainEvent(
                UUID id,
                OffsetDateTime occurredOn,
                String email) implements DomainEvent {

}