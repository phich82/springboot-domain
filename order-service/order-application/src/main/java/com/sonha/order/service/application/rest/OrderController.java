package com.sonha.order.service.application.rest;

import com.sonha.order.service.domain.dto.create.CreateOrderCommand;
import com.sonha.order.service.domain.dto.create.CreateOrderResponseDto;
import com.sonha.order.service.domain.dto.track.TrackOrderQuery;
import com.sonha.order.service.domain.dto.track.TrackOrderResponseDto;
import com.sonha.order.service.domain.ports.input.service.OrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/orders", produces = "application/vnd.api.v1+json")
public class OrderController {

    //@Autowired
    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponseDto> createOrder(@RequestBody CreateOrderCommand createOrderCommand) {
        log.info("creating order for customer: {} at restaurant: {}", createOrderCommand.getCustomerId(), createOrderCommand.getRestaurantId());
        CreateOrderResponseDto createOrderResponseDto = orderApplicationService.createOrder(createOrderCommand);
        log.info("Order created with tracking id: {}", createOrderResponseDto.getOrderTrackingId());
        return ResponseEntity.ok(createOrderResponseDto);
    }

    @GetMapping("/{trackingId}")
    public ResponseEntity<TrackOrderResponseDto> getOrderByTrackingId(@PathVariable UUID trackingId) {
        TrackOrderResponseDto trackOrderResponseDto = orderApplicationService.trackOrder(
                TrackOrderQuery.builder().orderTrackingId(trackingId).build()
        );
        log.info("Returning order status with tracking id: {}", trackOrderResponseDto.getOrderTrackingId());
        return ResponseEntity.ok(trackOrderResponseDto);
    }
}
