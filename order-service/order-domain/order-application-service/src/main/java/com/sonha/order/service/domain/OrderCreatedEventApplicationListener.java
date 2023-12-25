package com.sonha.order.service.domain;

import com.sonha.order.service.domain.event.OrderCreatedEvent;
import com.sonha.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class OrderCreatedEventApplicationListener {

    //@Autowired
    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;

    public OrderCreatedEventApplicationListener(OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher) {
        this.orderCreatedPaymentRequestMessagePublisher = orderCreatedPaymentRequestMessagePublisher;
    }

    @TransactionalEventListener
    void process(OrderCreatedEvent orderCreatedEvent) {
        orderCreatedPaymentRequestMessagePublisher.publish(orderCreatedEvent);
    }
}
