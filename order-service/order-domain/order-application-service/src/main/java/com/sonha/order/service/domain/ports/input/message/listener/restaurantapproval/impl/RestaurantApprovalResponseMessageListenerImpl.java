package com.sonha.order.service.domain.ports.input.message.listener.restaurantapproval.impl;

import com.sonha.order.service.domain.dto.message.RestaurantApprovalResponseDto;
import com.sonha.order.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
public class RestaurantApprovalResponseMessageListenerImpl implements RestaurantApprovalResponseMessageListener {
    @Override
    public void orderApproved(RestaurantApprovalResponseDto restaurantApprovalResponseDto) {
        //TODO
    }

    @Override
    public void orderRejected(RestaurantApprovalResponseDto restaurantApprovalResponseDto) {
        //TODO
    }
}
