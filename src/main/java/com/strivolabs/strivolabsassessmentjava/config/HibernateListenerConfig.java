package com.strivolabs.strivolabsassessmentjava.config;

import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.context.annotation.Configuration;

import com.strivolabs.strivolabsassessmentjava.auditlogs.InsertAuditLogListener;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class HibernateListenerConfig {

    private final EntityManagerFactory entityManagerFactory;
    private final InsertAuditLogListener auditLogListener;

    @PostConstruct
    public void registerListeners() {
        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);

        // Connect the lifecycle hooks
        registry.getEventListenerGroup(EventType.POST_COMMIT_INSERT).appendListener(auditLogListener);
        registry.getEventListenerGroup(EventType.POST_COMMIT_UPDATE).appendListener(auditLogListener);
        registry.getEventListenerGroup(EventType.POST_COMMIT_DELETE).appendListener(auditLogListener);
    }
}