package com.sonha.payment.service.domain.mapper;

import com.sonha.domain.valueobject.CustomerId;
import com.sonha.domain.valueobject.Money;
import com.sonha.domain.valueobject.OrderId;
import com.sonha.payment.service.domain.dto.PaymentRequest;
import com.sonha.payment.service.domain.entity.Payment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentMapper {
    public Payment paymentRequestModelToPayment(PaymentRequest paymentRequest) {
        return Payment.builder()
                .orderId(new OrderId(UUID.fromString(paymentRequest.getOrderId())))
                .customerId(new CustomerId(UUID.fromString(paymentRequest.getCustomerId())))
                .price(new Money(paymentRequest.getPrice()))
                .build();
    }
}
