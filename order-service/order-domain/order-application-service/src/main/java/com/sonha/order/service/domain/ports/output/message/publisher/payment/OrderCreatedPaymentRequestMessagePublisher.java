package com.sonha.order.service.domain.ports.output.message.publisher.payment;

import com.sonha.domain.event.publisher.DomainEventPublisher;
import com.sonha.order.service.domain.event.OrderCreatedEvent;

public interface OrderCreatedPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCreatedEvent> {
}
