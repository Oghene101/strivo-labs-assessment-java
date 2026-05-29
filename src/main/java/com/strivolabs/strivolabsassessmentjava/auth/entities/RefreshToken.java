package com.strivolabs.strivolabsassessmentjava.auth.entities;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.data.domain.Persistable;

import com.github.f4b6a3.uuid.UuidCreator;
import com.strivolabs.strivolabsassessmentjava.auth.enums.RefreshTokenStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "refresh_tokens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class RefreshToken implements Persistable<UUID> {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "session_id", nullable = false, updatable = false)
    private UUID sessionId;

    @Column(name = "token_hash", nullable = false, updatable = false, length = 64)
    private String tokenHash;

    @Column(name = "hash_key_id", nullable = false, updatable = false, length = 50)
    private String hashKeyId;

    @Column(name = "expires_at", nullable = false, updatable = false)
    private OffsetDateTime expiresAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RefreshTokenStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "last_updated_at", nullable = false)
    private OffsetDateTime lastUpdatedAt;

    @Column(name = "last_updated_by", nullable = false, length = 150)
    private String lastUpdatedBy;

    @Transient
    protected boolean isNewEntity = true;

    public static RefreshToken create(
            UUID userId,
            UUID sessionId,
            String tokenHash,
            String hashKeyId,
            OffsetDateTime expiresAt) {
        var refreshToken = new RefreshToken();

        OffsetDateTime timeStamp = OffsetDateTime.now();

        refreshToken.id = UuidCreator.getTimeOrderedEpoch();
        refreshToken.userId = userId;
        refreshToken.sessionId = sessionId;
        refreshToken.tokenHash = tokenHash;
        refreshToken.hashKeyId = hashKeyId;
        refreshToken.expiresAt = expiresAt;
        refreshToken.status = RefreshTokenStatus.ACTIVE;
        refreshToken.createdAt = timeStamp;
        refreshToken.lastUpdatedAt = timeStamp;
        refreshToken.lastUpdatedBy = userId.toString();

        return refreshToken;
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
