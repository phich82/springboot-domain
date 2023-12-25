package com.sonha.restaurant.service.domain.ports.output.message.publisher;

import com.sonha.domain.event.publisher.DomainEventPublisher;
import com.sonha.restaurant.service.domain.event.OrderRejectedEvent;

public interface OrderRejectedMessagePublisher extends DomainEventPublisher<OrderRejectedEvent> {
}
