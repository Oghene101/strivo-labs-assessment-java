package com.strivolabs.strivolabsassessmentjava.auth.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.strivolabs.strivolabsassessmentjava.auth.entities.Session;

public interface SessionRepository extends JpaRepository<Session, UUID> {
    @Modifying
    @Transactional
    @Query("""
                UPDATE Session s
                SET status = 'REVOKED',
                    s.lastUpdatedAt = CURRENT_TIMESTAMP,
                    s.lastUpdatedBy = :lastUpdatedBy
                WHERE s.userId = :userId
                  AND s.status = 'ACTIVE'
            """)
    void revoke(@Param("userId") UUID userId, @Param("lastUpdatedBy") String lastUpdatedBy);

    @Query("""
            SELECT CASE
                     WHEN Count(s) > 0 THEN TRUE
                     ELSE FALSE
                   END
            FROM   Session s
            WHERE  s.jwtId = :jwtId
                   AND s.status = 'ACTIVE'
            """)
    boolean existsByJwtId(@Param("jwtId") String jwtId);

}
