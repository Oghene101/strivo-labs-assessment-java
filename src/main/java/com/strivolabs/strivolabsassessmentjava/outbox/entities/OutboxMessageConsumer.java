package com.strivolabs.strivolabsassessmentjava.outbox.entities;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "outbox_message_consumers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OutboxMessageConsumer {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private OutboxMessageConsumerId id;

    @Column(name = "consumed_at", nullable = false, updatable = false)
    private OffsetDateTime consumedAt;

    public static OutboxMessageConsumer create(UUID outboxMessageId, String name) {
        OutboxMessageConsumer consumer = new OutboxMessageConsumer();

        consumer.id = OutboxMessageConsumerId.create(outboxMessageId, name);
        consumer.consumedAt = OffsetDateTime.now();

        return consumer;
    }
}