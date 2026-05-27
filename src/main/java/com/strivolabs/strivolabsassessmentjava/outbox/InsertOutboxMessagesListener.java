package com.strivolabs.strivolabsassessmentjava.outbox;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.DomainEvent;
import com.strivolabs.strivolabsassessmentjava.outbox.entities.OutboxMessage;
import com.strivolabs.strivolabsassessmentjava.outbox.repositories.OutboxMessageRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InsertOutboxMessagesListener {
    private final OutboxMessageRepository outboxMessages;
    private final ObjectMapper objectMapper;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleDomainEvent(DomainEvent event) {
        OutboxMessage message = OutboxMessage.create(event.id(), event.getClass().getSimpleName(),
                objectMapper.valueToTree(event));

        outboxMessages.save(message);
    }
}
