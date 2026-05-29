package com.strivolabs.strivolabsassessmentjava.auth.entities;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.data.domain.Persistable;

import com.github.f4b6a3.uuid.UuidCreator;
import com.strivolabs.strivolabsassessmentjava.auth.enums.SessionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sessions")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Session implements Persistable<UUID> {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "jwt_id", nullable = false, updatable = false, length = 50)
    private String jwtId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SessionStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "expires_at", nullable = false, updatable = false)
    private OffsetDateTime expiresAt;

    @Column(name = "last_updated_at", nullable = false)
    private OffsetDateTime lastUpdatedAt;

    @Column(name = "last_updated_by", nullable = false, length = 150)
    private String lastUpdatedBy;

    @Transient
    protected boolean isNewEntity = true;

    public static Session create(
            UUID userId,
            String jwtId,
            long expiresAtInDays,
            String lastUpdatedBy) {
        Session session = new Session();

        OffsetDateTime now = OffsetDateTime.now();

        session.id = UuidCreator.getTimeOrderedEpoch();
        session.userId = userId;
        session.jwtId = jwtId;
        session.status = SessionStatus.ACTIVE;
        session.createdAt = now;
        session.expiresAt = session.createdAt.plusDays(expiresAtInDays);
        session.lastUpdatedAt = now;
        session.lastUpdatedBy = lastUpdatedBy.trim();

        return session;
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
