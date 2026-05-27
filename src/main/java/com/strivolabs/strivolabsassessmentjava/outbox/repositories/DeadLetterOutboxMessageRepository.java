package com.strivolabs.strivolabsassessmentjava.outbox.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.strivolabs.strivolabsassessmentjava.outbox.entities.DeadLetterOutboxMessage;

public interface DeadLetterOutboxMessageRepository extends JpaRepository<DeadLetterOutboxMessage, UUID> {
}
