package com.sonha.order.service.domain.ports.input.message.listener.payment.impl;

import com.sonha.order.service.domain.dto.message.PaymentResponseDto;
import com.sonha.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {
    @Override
    public void paymentCompleted(PaymentResponseDto paymentResponseDto) {
        //TODO
    }

    @Override
    public void paymentCancelled(PaymentResponseDto paymentResponseDto) {
        //TODO
    }
}
