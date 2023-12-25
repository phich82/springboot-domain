package com.sonha.order.service.domain.impl;

import com.sonha.order.service.domain.OrderCreateCommandHandler;
import com.sonha.order.service.domain.OrderTrackCommandHandler;
import com.sonha.order.service.domain.dto.create.CreateOrderCommand;
import com.sonha.order.service.domain.dto.create.CreateOrderResponseDto;
import com.sonha.order.service.domain.dto.track.TrackOrderQuery;
import com.sonha.order.service.domain.dto.track.TrackOrderResponseDto;
import com.sonha.order.service.domain.ports.input.service.OrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
public class OrderApplicationServiceImpl implements OrderApplicationService {

    //@Autowired
    private final OrderCreateCommandHandler orderCreateCommandHandler;
    //@Autowired
    private final OrderTrackCommandHandler orderTrackCommandHandler;

    public OrderApplicationServiceImpl(OrderCreateCommandHandler orderCreateCommandHandler, OrderTrackCommandHandler orderTrackCommandHandler) {
        this.orderCreateCommandHandler = orderCreateCommandHandler;
        this.orderTrackCommandHandler = orderTrackCommandHandler;
    }

    @Override
    public CreateOrderResponseDto createOrder(CreateOrderCommand createOrderCommand) {
        return orderCreateCommandHandler.createOrder(createOrderCommand);
    }

    @Override
    public TrackOrderResponseDto trackOrder(TrackOrderQuery trackOrderQuery) {
        return orderTrackCommandHandler.trackOrder(trackOrderQuery);
    }
}
