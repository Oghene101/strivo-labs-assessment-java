package com.strivolabs.strivolabsassessmentjava.auth.entities;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.github.f4b6a3.uuid.UuidCreator;

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
@Table(name = "refresh_tokens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class RefreshToken {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "token_hash", nullable = false, length = 64)
    private String tokenHash;

    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;

    @Column(name = "is_revoked", nullable = false)
    private boolean isRevoked;

    @Column(name = "is_used", nullable = false)
    private boolean isUsed;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "created_by", nullable = false, updatable = false, length = 150)
    private String createdBy;

    @Column(name = "last_updated_at", nullable = false)
    private OffsetDateTime lastUpdatedAt;

    @Column(name = "last_updated_by", nullable = false, length = 150)
    private String lastUpdatedBy;

    public static RefreshToken create(
            UUID userId,
            String tokenHash,
            OffsetDateTime expiresAt) {
        var refreshToken = new RefreshToken();

        OffsetDateTime timeStamp = OffsetDateTime.now();
        String userIdString = userId.toString();

        refreshToken.id = UuidCreator.getTimeOrderedEpoch();
        refreshToken.userId = userId;
        refreshToken.tokenHash = tokenHash;
        refreshToken.expiresAt = expiresAt;
        refreshToken.isRevoked = false;
        refreshToken.isUsed = false;
        refreshToken.createdAt = timeStamp;
        refreshToken.createdBy = userIdString;
        refreshToken.lastUpdatedAt = timeStamp;
        refreshToken.lastUpdatedBy = userIdString;

        return refreshToken;
    }

}
