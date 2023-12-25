package com.sonha.payment.service.domain.service;

import com.sonha.domain.event.publisher.DomainEventPublisher;
import com.sonha.payment.service.domain.entity.CreditEntry;
import com.sonha.payment.service.domain.entity.CreditHistory;
import com.sonha.payment.service.domain.entity.Payment;
import com.sonha.payment.service.domain.event.PaymentCancelledEvent;
import com.sonha.payment.service.domain.event.PaymentCompletedEvent;
import com.sonha.payment.service.domain.event.PaymentEvent;
import com.sonha.payment.service.domain.event.PaymentFailedEvent;

import java.util.List;

public interface PaymentDomainService {

    public PaymentEvent validateAndInitiatePayment(Payment payment,
                                                   CreditEntry creditEntry,
                                                   List<CreditHistory> creditHistoryList,
                                                   List<String> failureMessages,
                                                   DomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventDomainEventPublisher,
                                                   DomainEventPublisher<PaymentFailedEvent> paymentFailedEventDomainEventPublisher);

    public PaymentEvent validateAndCancelPayment(Payment payment,
                                                 CreditEntry creditEntry,
                                                 List<CreditHistory> creditHistoryList,
                                                 List<String> failureMessages,
                                                 DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventDomainEventPublisher,
                                                 DomainEventPublisher<PaymentFailedEvent> paymentFailedEventDomainEventPublisher);
}
