package com.sonha.restaurant.service.domain.service.impl;

import com.sonha.domain.event.publisher.DomainEventPublisher;
import com.sonha.domain.valueobject.OrderApprovalStatus;
import com.sonha.restaurant.service.domain.entity.Restaurant;
import com.sonha.restaurant.service.domain.event.OrderApprovalEvent;
import com.sonha.restaurant.service.domain.event.OrderApprovedEvent;
import com.sonha.restaurant.service.domain.event.OrderRejectedEvent;
import com.sonha.restaurant.service.domain.service.RestaurantDomainService;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.sonha.domain.constant.DomainConst.ZONE_ID;

@Slf4j
public class RestaurantDomainServiceImpl implements RestaurantDomainService {
    @Override
    public OrderApprovalEvent validateOrder(Restaurant restaurant,
                                            List<String> failureMessages,
                                            DomainEventPublisher<OrderApprovedEvent> orderApprovedEventDomainEventPublisher,
                                            DomainEventPublisher<OrderRejectedEvent> orderRejectedEventDomainEventPublisher) {
        restaurant.validateOrder(failureMessages);
        log.info("Validating order with id: {}", restaurant.getOrderDetail().getId().getValue());
        if (failureMessages.isEmpty()) {
            log.info("Order is approved with order id: {}", restaurant.getOrderDetail().getId().getValue());
            restaurant.constructOrderApproval(OrderApprovalStatus.APPROVED);
            return new OrderApprovedEvent(
                    restaurant.getOrderApproval(),
                    restaurant.getId(),
                    failureMessages,
                    ZonedDateTime.now(ZoneId.of(ZONE_ID)),
                    orderApprovedEventDomainEventPublisher
            );
        }
        log.info("Order is rejected with order id: {}", restaurant.getOrderDetail().getId().getValue());
        restaurant.constructOrderApproval(OrderApprovalStatus.REJECTED);
        return new OrderRejectedEvent(
                restaurant.getOrderApproval(),
                restaurant.getId(),
                failureMessages,
                ZonedDateTime.now(ZoneId.of(ZONE_ID)),
                orderRejectedEventDomainEventPublisher
        );
    }
}
