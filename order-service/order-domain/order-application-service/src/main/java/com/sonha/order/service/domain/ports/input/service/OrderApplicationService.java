package com.sonha.order.service.domain.ports.input.service;

import com.sonha.order.service.domain.dto.create.CreateOrderCommand;
import com.sonha.order.service.domain.dto.create.CreateOrderResponseDto;
import com.sonha.order.service.domain.dto.track.TrackOrderQuery;
import com.sonha.order.service.domain.dto.track.TrackOrderResponseDto;
import jakarta.validation.Valid;

public interface OrderApplicationService {
    CreateOrderResponseDto createOrder(@Valid CreateOrderCommand createOrderCommand);
    TrackOrderResponseDto trackOrder(@Valid TrackOrderQuery trackOrderQuery);
}
