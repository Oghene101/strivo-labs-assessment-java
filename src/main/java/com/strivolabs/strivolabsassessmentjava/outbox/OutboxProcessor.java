package com.strivolabs.strivolabsassessmentjava.outbox;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OutboxProcessor {

    private final OutboxMessageProcessor messageProcessor;

    @Value("${outbox.batch-size}")
    private int batchSize;

    @Scheduled(fixedDelayString = "${outbox.scheduler.delay-ms}")
    public void processOutboxMessages() {
        List<UUID> messageIds = messageProcessor.findUnprocessedBatch(batchSize);

        if (messageIds.isEmpty()) {
            return;
        }

        for (UUID id : messageIds) {
            messageProcessor.processById(id);
        }
    }

}
