package com.strivolabs.strivolabsassessmentjava.outbox.entities;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "dead_letter_outbox_messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DeadLetterOutboxMessage {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "type", nullable = false, updatable = false, length = 200)
    private String type;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "content", nullable = false, updatable = false)
    private JsonNode content;

    @Column(name = "dead_lettered_on", nullable = false, updatable = false)
    private OffsetDateTime deadLetteredOn;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @Column(name = "error")
    private String error;

    public static DeadLetterOutboxMessage create(OutboxMessage message) {
        DeadLetterOutboxMessage dlqMessage = new DeadLetterOutboxMessage();

        dlqMessage.id = message.getId();
        dlqMessage.type = message.getType();
        dlqMessage.content = message.getContent();
        dlqMessage.deadLetteredOn = OffsetDateTime.now();
        dlqMessage.retryCount = message.getRetryCount();
        dlqMessage.error = message.getError();

        return dlqMessage;
    }

}