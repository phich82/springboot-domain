package com.sonha.domain.event.publisher;

import com.sonha.domain.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {
    void publish(T domainEvent);
}
