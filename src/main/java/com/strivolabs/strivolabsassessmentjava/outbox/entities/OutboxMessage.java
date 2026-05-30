package com.strivolabs.strivolabsassessmentjava.outbox.entities;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "outbox_messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OutboxMessage implements Persistable<UUID> {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "type", nullable = false, updatable = false, length = 200)
    private String type;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "content", nullable = false, updatable = false)
    private JsonNode content;

    @Column(name = "occurred_on", nullable = false, updatable = false)
    private OffsetDateTime occurredOn;

    @Column(name = "processed_on")
    private OffsetDateTime processedOn;

    @Column(name = "next_retry_on")
    private OffsetDateTime nextRetryOn;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @Column(name = "error")
    private String error;

    @Transient
    protected boolean isNewEntity = true;

    public static OutboxMessage create(UUID domainEventId, String type, JsonNode content) {
        OutboxMessage message = new OutboxMessage();

        message.id = domainEventId;
        message.type = type;
        message.content = content;
        message.occurredOn = OffsetDateTime.now();

        return message;
    }

    public void markAsProcessed() {
        this.processedOn = OffsetDateTime.now();
    }

    public void recordFailure(String error) {
        this.retryCount++;
        this.error = error;
    }

    public void setNextRetryOn(OffsetDateTime nextRetryOn) {
        this.nextRetryOn = nextRetryOn;
    }

    @Override
    public boolean isNew() {
        return isNewEntity;
    }

    @PostLoad
    @PostPersist
    protected void markNotNew() {
        this.isNewEntity = false;
    }

}
