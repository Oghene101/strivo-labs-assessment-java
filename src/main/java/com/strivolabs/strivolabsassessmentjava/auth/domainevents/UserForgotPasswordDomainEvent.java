package com.strivolabs.strivolabsassessmentjava.auth.domainevents;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.strivolabs.strivolabsassessmentjava.common.abstractions.DomainEvent;

public final record UserForgotPasswordDomainEvent(
        UUID id,
        OffsetDateTime occurredOn,
        String email) implements DomainEvent {

}
