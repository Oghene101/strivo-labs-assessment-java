package com.strivolabs.strivolabsassessmentjava.auditlogs;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, java.util.UUID> {

}
