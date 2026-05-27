package com.strivolabs.strivolabsassessmentjava.outbox.entities;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class OutboxMessageConsumerId implements Serializable {

    @Column(name = "outbox_message_id", nullable = false, updatable = false)
    private UUID outboxMessageId;

    @Column(name = "name", nullable = false, updatable = false, length = 200)
    private String name;

    public static OutboxMessageConsumerId create(
            UUID outboxMessageId,
            String name) {
        OutboxMessageConsumerId id = new OutboxMessageConsumerId();

        id.outboxMessageId = outboxMessageId;
        id.name = name;

        return id;
    }

}