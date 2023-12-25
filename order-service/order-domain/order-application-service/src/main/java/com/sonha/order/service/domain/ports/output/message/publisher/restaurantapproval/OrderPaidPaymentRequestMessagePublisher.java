package com.sonha.order.service.domain.ports.output.message.publisher.restaurantapproval;

import com.sonha.domain.event.publisher.DomainEventPublisher;
import com.sonha.order.service.domain.event.OrderPaidEvent;

public interface OrderPaidPaymentRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
}
