package com.sonha.restaurant.service.domain.event;

import com.sonha.domain.event.publisher.DomainEventPublisher;
import com.sonha.domain.valueobject.RestaurantId;
import com.sonha.restaurant.service.domain.entity.OrderApproval;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderApprovedEvent extends OrderApprovalEvent {

    private final DomainEventPublisher<OrderApprovedEvent> orderApprovedEventDomainEventPublisher;

    public OrderApprovedEvent(OrderApproval orderApproval,
                                 RestaurantId restaurantId,
                                 List<String> failureMessages,
                                 ZonedDateTime createdAt,
                                 DomainEventPublisher<OrderApprovedEvent> orderApprovedEventDomainEventPublisher) {
        super(orderApproval, restaurantId, failureMessages, createdAt);
        this.orderApprovedEventDomainEventPublisher = orderApprovedEventDomainEventPublisher;
    }

    @Override
    public void fire() {
        orderApprovedEventDomainEventPublisher.publish(this);
    }
}
