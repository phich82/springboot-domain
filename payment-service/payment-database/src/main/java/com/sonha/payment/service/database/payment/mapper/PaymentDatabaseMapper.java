package com.sonha.payment.service.database.payment.mapper;

import com.sonha.domain.valueobject.CustomerId;
import com.sonha.domain.valueobject.Money;
import com.sonha.domain.valueobject.OrderId;
import com.sonha.payment.service.database.payment.entity.PaymentEntity;
import com.sonha.payment.service.domain.entity.Payment;
import com.sonha.payment.service.domain.valueobject.PaymentId;
import org.springframework.stereotype.Component;

@Component
public class PaymentDatabaseMapper {

    public PaymentEntity paymentToPaymentEntity(Payment payment) {
        return PaymentEntity.builder()
                .id(payment.getId().getValue())
                .customerId(payment.getCustomerId().getValue())
                .orderId(payment.getOrderId().getValue())
                .price(payment.getPrice().getAmount())
                .status(payment.getPaymentStatus())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    public Payment paymentEntityToPayment(PaymentEntity paymentEntity) {
        return Payment.builder()
                .paymentId(new PaymentId(paymentEntity.getId()))
                .customerId(new CustomerId(paymentEntity.getCustomerId()))
                .orderId(new OrderId(paymentEntity.getOrderId()))
                .price(new Money(paymentEntity.getPrice()))
                //.paymentStatus(paymentEntity.getStatus())
                .createdAt(paymentEntity.getCreatedAt())
                .build();
    }
}
