package com.sonha.order.service.domain;

import com.sonha.order.service.domain.dto.create.CreateOrderCommand;
import com.sonha.order.service.domain.dto.create.CreateOrderResponseDto;
import com.sonha.order.service.domain.event.OrderCreatedEvent;
import com.sonha.order.service.domain.mapper.OrderDataMapper;
import com.sonha.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    //@Autowired
    private final OrderCreateHelper orderCreateHelper;
    //@Autowired
    private final OrderDataMapper orderDataMapper;
    //@Autowired
    private final OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher;
    //@Autowired
    private final ApplicationDomainEventPublisher applicationDomainEventPublisher;

    public OrderCreateCommandHandler(
            OrderCreateHelper orderCreateHelper,
            OrderDataMapper orderDataMapper,
            OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher,
            ApplicationDomainEventPublisher applicationDomainEventPublisher) {
        this.orderCreateHelper = orderCreateHelper;
        this.orderDataMapper = orderDataMapper;
        this.orderCreatedPaymentRequestMessagePublisher = orderCreatedPaymentRequestMessagePublisher;
        this.applicationDomainEventPublisher = applicationDomainEventPublisher;
    }


    @Transactional
    public CreateOrderResponseDto createOrder(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent orderCreatedEvent = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("Order is created with id: {}", orderCreatedEvent.getOrder().getId().getValue());
        orderCreatedPaymentRequestMessagePublisher.publish(orderCreatedEvent);
        //applicationDomainEventPublisher.publish(orderCreatedEvent);
        return orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder(), "Order created successfully");
    }
}
