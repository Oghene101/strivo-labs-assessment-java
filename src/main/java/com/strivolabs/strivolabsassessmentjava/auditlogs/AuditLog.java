package com.strivolabs.strivolabsassessmentjava.auditlogs;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.data.domain.Persistable;

import com.github.f4b6a3.uuid.UuidCreator;

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
@Table(name = "audit_logs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AuditLog implements Persistable<UUID> {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "action", length = 20, nullable = false, updatable = false)
    private String action;

    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "entity_name", length = 50, nullable = false, updatable = false)
    private String entityName;

    @Column(name = "entity_id", nullable = false, updatable = false)
    private UUID entityId;

    @Column(name = "occurred_on", nullable = false, updatable = false)
    private OffsetDateTime occurredOn;

    @Column(name = "changes", nullable = false, updatable = false)
    private String changes;

    @Transient
    protected boolean isNewEntity = true;

    public static AuditLog create(
            String action,
            UUID userId,
            String entityName,
            UUID entityId,
            String changes) {
        AuditLog auditLog = new AuditLog();

        auditLog.id = UuidCreator.getTimeOrderedEpoch();
        auditLog.action = action;
        auditLog.userId = userId;
        auditLog.entityName = entityName;
        auditLog.entityId = entityId;
        auditLog.occurredOn = OffsetDateTime.now();
        auditLog.changes = changes;

        return auditLog;
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