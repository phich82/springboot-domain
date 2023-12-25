package com.sonha.payment.service.domain.ports.output.message.publisher;

import com.sonha.domain.event.publisher.DomainEventPublisher;
import com.sonha.payment.service.domain.event.PaymentCancelledEvent;

public interface PaymentCancelledMessagePublisher extends DomainEventPublisher<PaymentCancelledEvent> {
}
