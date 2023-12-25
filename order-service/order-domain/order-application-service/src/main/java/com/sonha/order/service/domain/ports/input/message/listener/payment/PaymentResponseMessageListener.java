package com.sonha.order.service.domain.ports.input.message.listener.payment;

import com.sonha.order.service.domain.dto.message.PaymentResponseDto;

public interface PaymentResponseMessageListener {
    void paymentCompleted(PaymentResponseDto paymentResponseDto);
    void paymentCancelled(PaymentResponseDto paymentResponseDto);
}
