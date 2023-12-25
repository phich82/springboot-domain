package com.sonha.payment.service.database.payment.adapter;

import com.sonha.payment.service.database.payment.mapper.PaymentDatabaseMapper;
import com.sonha.payment.service.database.payment.repository.PaymentJpaRepository;
import com.sonha.payment.service.domain.entity.Payment;
import com.sonha.payment.service.domain.ports.output.repository.PaymentRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;
    private final PaymentDatabaseMapper paymentDatabaseMapper;

    public PaymentRepositoryImpl(PaymentJpaRepository paymentJpaRepository,
                                 PaymentDatabaseMapper paymentDatabaseMapper) {
        this.paymentJpaRepository = paymentJpaRepository;
        this.paymentDatabaseMapper = paymentDatabaseMapper;
    }

    @Override
    public Payment save(Payment payment) {
        return paymentDatabaseMapper.paymentEntityToPayment(
                paymentJpaRepository.save(paymentDatabaseMapper.paymentToPaymentEntity(payment))
        );
    }

    @Override
    public Optional<Payment> findByOrderId(UUID orderId) {
        return paymentJpaRepository.findByOrderId(orderId)
                .map(paymentDatabaseMapper::paymentEntityToPayment);
    }
}
