package com.strivolabs.strivolabsassessmentjava.outbox;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.DomainEvent;
import com.strivolabs.strivolabsassessmentjava.common.abstractions.DomainEventHandler;
import com.strivolabs.strivolabsassessmentjava.outbox.entities.DeadLetterOutboxMessage;
import com.strivolabs.strivolabsassessmentjava.outbox.entities.OutboxMessage;
import com.strivolabs.strivolabsassessmentjava.outbox.entities.OutboxMessageConsumer;
import com.strivolabs.strivolabsassessmentjava.outbox.entities.OutboxMessageConsumerId;
import com.strivolabs.strivolabsassessmentjava.outbox.repositories.DeadLetterOutboxMessageRepository;
import com.strivolabs.strivolabsassessmentjava.outbox.repositories.OutboxMessageConsumerRepository;
import com.strivolabs.strivolabsassessmentjava.outbox.repositories.OutboxMessageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxMessageProcessor {

    private final ApplicationContext context;
    private final OutboxMessageRepository outboxMessages;
    private final DeadLetterOutboxMessageRepository deadLetterOutboxMessages;
    private final OutboxMessageConsumerRepository outboxMessageconsumers;
    private final ObjectMapper objectMapper;

    @Value("${outbox.max-retries}")
    private int maxRetries;

    @Value("${outbox.retry-delay-seconds}")
    private int retryDelaySeconds;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<UUID> findUnprocessedBatch(int batchSize) {
        return outboxMessages.findUnprocessedMessageIds(OffsetDateTime.now(), PageRequest.of(0, batchSize));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW) // own transaction
    public void processById(UUID id) {
        OutboxMessage message = outboxMessages.findById(id).orElse(null);
        if (message == null || message.getProcessedOn() != null) {
            return;
        }

        try {
            DomainEvent event = objectMapper.treeToValue(message.getContent(), DomainEvent.class);

            List<DomainEventHandler<DomainEvent>> handlers = findHandlers(event.getClass());

            if (handlers.isEmpty()) {
                log.warn("No handlers found for event type: {}", event.getClass().getName());
            }

            for (DomainEventHandler<DomainEvent> handler : handlers) {
                processIdempotently(event, handler);
            }

            message.markAsProcessed();
            outboxMessages.save(message);

        } catch (Exception ex) {
            log.error("Failed to process outbox message: {}", message.getId(), ex);
            handleFailure(message, ex);
        }
    }

    @SuppressWarnings("unchecked")
    private List<DomainEventHandler<DomainEvent>> findHandlers(Class<?> eventClass) {
        return context.getBeansOfType(DomainEventHandler.class)
                .values()
                .stream()
                .filter(handler -> handler.getEventType().equals(eventClass))
                .map(handler -> (DomainEventHandler<DomainEvent>) handler)
                .toList();
    }

    private void processIdempotently(
            DomainEvent event,
            DomainEventHandler<DomainEvent> handler) {

        String handlerName = handler.getClass().getName();
        OutboxMessageConsumerId consumerId = OutboxMessageConsumerId.create(
                event.id(), handlerName);

        if (outboxMessageconsumers.existsById(consumerId)) {
            log.info("Event {} already processed by handler {} - skipping",
                    event.id(), handlerName);
            return;
        }

        handler.handle(event);

        outboxMessageconsumers.save(OutboxMessageConsumer.create(event.id(), handlerName));

        log.info("Event {} processed by handler {}", event.id(), handlerName);
    }

    private void handleFailure(OutboxMessage message, Exception ex) {
        message.recordFailure(ex.toString());

        if (message.getRetryCount() >= maxRetries) {
            log.error("Message {} exceeded max retries - moving to dead letter queue",
                    message.getId());

            DeadLetterOutboxMessage dlqMessage = DeadLetterOutboxMessage.create(message);
            deadLetterOutboxMessages.save(dlqMessage);

            outboxMessages.delete(message);
        } else {
            log.warn("Message {} failed - retry {} of {}",
                    message.getId(), message.getRetryCount(), maxRetries);

            // Exponential backoff calculation
            int delaySeconds = retryDelaySeconds * (int) Math.pow(2, message.getRetryCount() - 1);
            message.setNextRetryOn(OffsetDateTime.now().plusSeconds(delaySeconds));

            outboxMessages.save(message);
        }
    }
}