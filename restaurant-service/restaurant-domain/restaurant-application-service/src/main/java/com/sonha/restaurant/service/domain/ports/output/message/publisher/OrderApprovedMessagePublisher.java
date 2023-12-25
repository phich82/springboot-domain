package com.sonha.restaurant.service.domain.ports.output.message.publisher;

import com.sonha.domain.event.publisher.DomainEventPublisher;
import com.sonha.restaurant.service.domain.event.OrderApprovedEvent;

public interface OrderApprovedMessagePublisher extends DomainEventPublisher<OrderApprovedEvent> {
}
