package com.sonha.order.service.domain;

import com.sonha.order.service.domain.dto.create.CreateOrderCommand;
import com.sonha.order.service.domain.dto.create.CreateOrderResponseDto;
import com.sonha.order.service.domain.dto.track.TrackOrderQuery;
import com.sonha.order.service.domain.dto.track.TrackOrderResponseDto;
import com.sonha.order.service.domain.entity.Order;
import com.sonha.order.service.domain.exception.OrderNotFoundException;
import com.sonha.order.service.domain.mapper.OrderDataMapper;
import com.sonha.order.service.domain.ports.output.repository.OrderRepository;
import com.sonha.order.service.domain.valueobject.TrackingId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
public class OrderTrackCommandHandler {

    //@Autowired
    private final OrderDataMapper orderDataMapper;
    //@Autowired
    private final OrderRepository orderRepository;

    public OrderTrackCommandHandler(OrderDataMapper orderDataMapper, OrderRepository orderRepository) {
        this.orderDataMapper = orderDataMapper;
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public TrackOrderResponseDto trackOrder(TrackOrderQuery trackOrderQuery) {
        Optional<Order> orderResult = orderRepository.findByTrackingId(new TrackingId(trackOrderQuery.getOrderTrackingId()));
        if (orderResult.isEmpty()) {
            log.warn("Could not find order with tracking id: {}", trackOrderQuery.getOrderTrackingId());
            throw new OrderNotFoundException("Could not find order with tracking id: " + trackOrderQuery.getOrderTrackingId());
        }
        return orderDataMapper.orderToTrackOrderResponse(orderResult.get());
    }
}
