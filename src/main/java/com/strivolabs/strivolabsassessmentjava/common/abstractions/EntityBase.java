package com.strivolabs.strivolabsassessmentjava.common.abstractions;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.domain.Persistable;

import com.github.f4b6a3.uuid.UuidCreator;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class EntityBase implements Auditable, Persistable<UUID> {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "created_by", nullable = false, updatable = false, length = 150)
    private String createdBy;

    @Column(name = "last_updated_at", nullable = false)
    private OffsetDateTime lastUpdatedAt;

    @Column(name = "last_updated_by", nullable = false, length = 150)
    private String lastUpdatedBy;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name = "deleted_by", length = 150)
    private String deletedBy;

    protected void initializeAudit(String createdBy) {
        OffsetDateTime timeStamp = OffsetDateTime.now();

        this.id = UuidCreator.getTimeOrderedEpoch();
        this.createdAt = timeStamp;
        this.createdBy = createdBy.trim();
        this.lastUpdatedAt = timeStamp;
        this.lastUpdatedBy = createdBy.trim();
    }

    protected void updateAudit(String updatedBy) {
        this.lastUpdatedAt = OffsetDateTime.now();
        this.lastUpdatedBy = updatedBy.trim();
    }

    @Override
    public void softDelete(String deletedBy) {
        OffsetDateTime timeStamp = OffsetDateTime.now();

        this.deletedAt = timeStamp;
        this.deletedBy = deletedBy;
        this.lastUpdatedBy = deletedBy;
        this.lastUpdatedAt = timeStamp;
    }

    @Override
    public boolean isDeleted() {
        return deletedAt != null;
    }

    @Transient
    protected boolean isNewEntity = true;

    @PostLoad
    @PostPersist
    protected void markNotNew() {
        this.isNewEntity = false;
    }

    @Transient
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected void raise(DomainEvent domainEvent) {
        domainEvents.add(domainEvent);
    }

    @DomainEvents
    protected List<DomainEvent> publish() {
        return this.domainEvents;
    }

    @AfterDomainEventPublication
    public void clearDomainEvents() {
        domainEvents.clear();
    }
}