package com.strivolabs.strivolabsassessmentjava.common.abstractions;

public interface DomainEventHandler<TDomainEvent extends DomainEvent> {
    void handle(TDomainEvent event);

    Class<TDomainEvent> getEventType();
}
