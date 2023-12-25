package com.sonha.order.service.domain.ports.input.message.listener.restaurantapproval;

import com.sonha.order.service.domain.dto.message.RestaurantApprovalResponseDto;

public interface RestaurantApprovalResponseMessageListener {
    void orderApproved(RestaurantApprovalResponseDto restaurantApprovalResponseDto);
    void orderRejected(RestaurantApprovalResponseDto restaurantApprovalResponseDto);
}
