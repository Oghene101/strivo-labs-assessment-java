package com.strivolabs.strivolabsassessmentjava.auditlogs;

import java.util.Objects;
import java.util.UUID;

import org.hibernate.event.spi.PostCommitDeleteEventListener;
import org.hibernate.event.spi.PostCommitInsertEventListener;
import org.hibernate.event.spi.PostCommitUpdateEventListener;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import com.strivolabs.strivolabsassessmentjava.common.abstractions.EntityBase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InsertAuditLogListener
        implements PostCommitInsertEventListener,
        PostCommitUpdateEventListener,
        PostCommitDeleteEventListener {

    private final ObjectProvider<AuditContext> auditContext;
    private final ObjectProvider<AuditLogRepository> auditLogs;
    private final ObjectProvider<PlatformTransactionManager> transactionManager;

    @Override
    public void onPostInsert(PostInsertEvent event) {
        Object entity = event.getEntity();
        if (!(entity instanceof EntityBase)) {
            return;
        }

        insertAuditLog(entity, "ADDED", getChangesForInsert(entity));
    }

    @Override
    public void onPostInsertCommitFailed(PostInsertEvent event) {
        // called if commit failed - ignore
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        Object entity = event.getEntity();
        if (!(entity instanceof EntityBase)) {
            return;
        }

        String changes = getChangesForUpdate(event);
        if (!changes.isEmpty()) {
            insertAuditLog(entity, "MODIFIED", changes);
        }
    }

    @Override
    public void onPostUpdateCommitFailed(PostUpdateEvent event) {
        // called if commit failed - ignore
    }

    @Override
    public void onPostDelete(PostDeleteEvent event) {
        Object entity = event.getEntity();
        if (!(entity instanceof EntityBase)) {
            return;
        }

        insertAuditLog(entity, "DELETED", getChangesForDelete(entity));
    }

    @Override
    public void onPostDeleteCommitFailed(PostDeleteEvent event) {
        // called if commit failed - ignore
    }

    private void insertAuditLog(Object entity, String action, String changes) {
        TransactionTemplate template = new TransactionTemplate(
                transactionManager.getObject());

        template.setPropagationBehavior(
                TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        template.execute(status -> {
            EntityBase baseEntity = (EntityBase) entity;
            UUID entityId = baseEntity.getId();

            UUID userId;
            try {
                userId = UUID.fromString(auditContext.getObject().getCurrentUser().toString());
            } catch (IllegalArgumentException ex) {
                userId = new UUID(0L, 0L);
            }

            AuditLog log = AuditLog.create(
                    action,
                    userId,
                    entity.getClass().getSimpleName(),
                    entityId,
                    changes);

            auditLogs.getObject().save(log);

            return null;
        });
    }

    public String getChangesForInsert(Object entity) {
        return entity.getClass().getSimpleName() + " entity created";
    }

    public String getChangesForDelete(Object entity) {
        return entity.getClass().getSimpleName() + " entity deleted";
    }

    public String getChangesForUpdate(PostUpdateEvent event) {
        StringBuilder changes = new StringBuilder();

        String[] propertyNames = event.getPersister().getPropertyNames();
        Object[] oldState = event.getOldState();
        Object[] currentState = event.getState();

        if (oldState == null || currentState == null) {
            return "Entity updated (detailed changes unavailable)";
        }

        for (int i = 0; i < propertyNames.length; i++) {
            Object originalValue = oldState[i];
            Object currentValue = currentState[i];

            if (isIgnoredProperty(propertyNames[i])) {
                continue;
            }

            if (!Objects.equals(originalValue, currentValue)) {
                changes.append(String.format("%s: from '%s' to '%s'\n",
                        propertyNames[i],
                        originalValue != null ? originalValue.toString() : "NULL",
                        currentValue != null ? currentValue.toString() : "NULL"));
            }
        }

        return changes.toString();
    }

    private boolean isIgnoredProperty(String propertyName) {
        return propertyName.equalsIgnoreCase("password")
                || propertyName.equalsIgnoreCase("lastUpdatedAt")
                || propertyName.equalsIgnoreCase("lastUpdatedBy");
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister persister) {
        return true;
    }
}