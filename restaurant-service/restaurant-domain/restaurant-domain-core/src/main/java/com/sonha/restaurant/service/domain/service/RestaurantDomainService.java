package com.sonha.restaurant.service.domain.service;

import com.sonha.domain.event.publisher.DomainEventPublisher;
import com.sonha.restaurant.service.domain.entity.Restaurant;
import com.sonha.restaurant.service.domain.event.OrderApprovalEvent;
import com.sonha.restaurant.service.domain.event.OrderApprovedEvent;
import com.sonha.restaurant.service.domain.event.OrderRejectedEvent;

import java.util.List;

public interface RestaurantDomainService {
    OrderApprovalEvent validateOrder(Restaurant restaurant,
                                     List<String> failureMessages,
                                     DomainEventPublisher<OrderApprovedEvent> orderApprovedEventDomainEventPublisher,
                                     DomainEventPublisher<OrderRejectedEvent> orderRejectedEventDomainEventPublisher);
}
