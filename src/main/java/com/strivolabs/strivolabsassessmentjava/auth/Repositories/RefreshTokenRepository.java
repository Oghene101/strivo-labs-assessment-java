package com.strivolabs.strivolabsassessmentjava.auth.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.strivolabs.strivolabsassessmentjava.auth.entities.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    @Modifying
    @Transactional
    @Query("""
                UPDATE RefreshToken rt
                SET rt.status = 'REVOKED'
                WHERE rt.userId = :userId
                  AND rt.status = 'ACTIVE'
            """)
    void revoke(@Param("userId") UUID userId);

}
