package com.strivolabs.strivolabsassessmentjava.outbox.repositories;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.strivolabs.strivolabsassessmentjava.outbox.entities.OutboxMessage;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;

public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, UUID> {
  @Query("""
          SELECT o.id
          FROM OutboxMessage o
          WHERE o.processedOn IS NULL
            AND (o.nextRetryOn IS NULL OR o.nextRetryOn <= :now)
          ORDER BY o.occurredOn
      """)
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @QueryHints(@QueryHint(name = "jakarta.persistence.lock.timeout", value = "-2")) // SKIP LOCKED
  List<UUID> findUnprocessedMessageIds(
      @Param("now") OffsetDateTime now,
      Pageable pageable);
}
