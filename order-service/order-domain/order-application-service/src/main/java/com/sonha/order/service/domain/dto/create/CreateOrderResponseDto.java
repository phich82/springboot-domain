package com.sonha.order.service.domain.dto.create;

import com.sonha.domain.valueobject.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;


@Getter
@Builder
@AllArgsConstructor
public class CreateOrderResponseDto {
    @NotNull
    private final UUID orderTrackingId;
    @NotNull
    private final OrderStatus orderStatus;
    @NotNull
    private final String message;
}
