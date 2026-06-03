package com.strivolabs.strivolabsassessmentjava.auth.repositories;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.strivolabs.strivolabsassessmentjava.auth.dtos.RefreshTokenValidationDto;
import com.strivolabs.strivolabsassessmentjava.auth.entities.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    @Modifying
    @Transactional
    @Query("""
                UPDATE RefreshToken rt
                SET rt.status = 'REVOKED',
                    rt.lastUpdatedAt = :now,
                    rt.lastUpdatedBy = :lastUpdatedBy
                WHERE rt.userId = :userId
                  AND rt.status = 'ACTIVE'
            """)
    void revoke(
            @Param("userId") UUID userId,
            @Param("lastUpdatedBy") String lastUpdatedBy,
            @Param("now") OffsetDateTime now);

    @Query("""
                SELECT rt.userId,
                       rt.sessionId
                FROM   RefreshToken rt
                WHERE  rt.tokenHash = :tokenHash
                       AND  rt.expiresAt > :now
                       AND  rt.status = 'ACTIVE'
            """)
    Optional<RefreshTokenValidationDto> findByTokenHash(
            @Param("tokenHash") String tokenHash,
            @Param("now") OffsetDateTime now);

}
