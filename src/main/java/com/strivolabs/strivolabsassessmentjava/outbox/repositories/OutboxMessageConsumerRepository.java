package com.strivolabs.strivolabsassessmentjava.outbox.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.strivolabs.strivolabsassessmentjava.outbox.entities.OutboxMessageConsumer;
import com.strivolabs.strivolabsassessmentjava.outbox.entities.OutboxMessageConsumerId;

public interface OutboxMessageConsumerRepository extends JpaRepository<OutboxMessageConsumer, OutboxMessageConsumerId> {

    boolean existsById(OutboxMessageConsumerId outboxMessageId);
}