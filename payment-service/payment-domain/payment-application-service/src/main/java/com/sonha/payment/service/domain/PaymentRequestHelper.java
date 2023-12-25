package com.sonha.payment.service.domain;

import com.sonha.domain.valueobject.CustomerId;
import com.sonha.payment.service.domain.dto.PaymentRequest;
import com.sonha.payment.service.domain.entity.CreditEntry;
import com.sonha.payment.service.domain.entity.CreditHistory;
import com.sonha.payment.service.domain.entity.Payment;
import com.sonha.payment.service.domain.event.PaymentEvent;
import com.sonha.payment.service.domain.exception.PaymentApplicationServiceException;
import com.sonha.payment.service.domain.mapper.PaymentMapper;
import com.sonha.payment.service.domain.ports.output.message.publisher.PaymentCancelledMessagePublisher;
import com.sonha.payment.service.domain.ports.output.message.publisher.PaymentCompletedMessagePublisher;
import com.sonha.payment.service.domain.ports.output.message.publisher.PaymentFailedMessagePublisher;
import com.sonha.payment.service.domain.ports.output.repository.CreditEntryRepository;
import com.sonha.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import com.sonha.payment.service.domain.ports.output.repository.PaymentRepository;
import com.sonha.payment.service.domain.service.PaymentDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class PaymentRequestHelper {

    private final PaymentDomainService paymentDomainService;
    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final PaymentCompletedMessagePublisher paymentCompletedMessagePublisher;
    private final PaymentCancelledMessagePublisher paymentCancelledEventDomainEventPublisher;
    private final PaymentFailedMessagePublisher paymentFailedEventDomainEventPublisher;

    public PaymentRequestHelper(PaymentDomainService paymentDomainService,
                                PaymentMapper paymentMapper,
                                PaymentRepository paymentRepository,
                                CreditEntryRepository creditEntryRepository,
                                CreditHistoryRepository creditHistoryRepository,
                                PaymentCompletedMessagePublisher paymentCompletedMessagePublisher,
                                PaymentCancelledMessagePublisher paymentCancelledEventDomainEventPublisher,
                                PaymentFailedMessagePublisher paymentFailedEventDomainEventPublisher) {
        this.paymentDomainService = paymentDomainService;
        this.paymentMapper = paymentMapper;
        this.paymentRepository = paymentRepository;
        this.creditEntryRepository = creditEntryRepository;
        this.creditHistoryRepository = creditHistoryRepository;
        this.paymentCompletedMessagePublisher = paymentCompletedMessagePublisher;
        this.paymentCancelledEventDomainEventPublisher = paymentCancelledEventDomainEventPublisher;
        this.paymentFailedEventDomainEventPublisher = paymentFailedEventDomainEventPublisher;
    }

    @Transactional
    public PaymentEvent persistPayment(PaymentRequest paymentRequest) {
        log.info("Received payment complete event for order id: {}", paymentRequest.getOrderId());
        Payment payment = paymentMapper.paymentRequestModelToPayment(paymentRequest);
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistoryList = getCreditHistoryList(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent = paymentDomainService.validateAndInitiatePayment(payment, creditEntry, creditHistoryList, failureMessages, paymentCompletedMessagePublisher, paymentFailedEventDomainEventPublisher);
        persistDbObjects(payment, creditEntry, creditHistoryList, failureMessages);
        return paymentEvent;
    }

    @Transactional
    public PaymentEvent persistCancelPayment(PaymentRequest paymentRequest) {
        log.info("Received payment rollback event for order id: {}", paymentRequest.getOrderId());
        Optional<Payment> paymentResponse = paymentRepository.findByOrderId(UUID.fromString(paymentRequest.getOrderId()));
        if (paymentResponse.isEmpty()) {
            log.error("Payment with order id: {} could not found", paymentRequest.getOrderId());
            throw new PaymentApplicationServiceException("Payment with order id: " + paymentRequest.getOrderId() + " could not found");
        }
        Payment payment = paymentResponse.get();
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistoryList = getCreditHistoryList(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent = paymentDomainService.validateAndCancelPayment(payment, creditEntry, creditHistoryList, failureMessages, paymentCancelledEventDomainEventPublisher, paymentFailedEventDomainEventPublisher);
        persistDbObjects(payment, creditEntry, creditHistoryList, failureMessages);
        return paymentEvent;
    }

    private CreditEntry getCreditEntry(CustomerId customerId) {
        Optional<CreditEntry> creditEntry = creditEntryRepository.findByCustomerId(customerId);
        if (creditEntry.isEmpty()) {
            log.error("Could not find credit entry for customer: {}", customerId.getValue());
            throw new PaymentApplicationServiceException("Could not find credit entry for customer: " + customerId.getValue());
        }
        return creditEntry.get();
    }

    private List<CreditHistory> getCreditHistoryList(CustomerId customerId) {
        Optional<List<CreditHistory>> creditHistoryList = creditHistoryRepository.findByCustomerId(customerId);
        if (creditHistoryList.isEmpty()) {
            log.error("Could not find credit history for customer: {}", customerId.getValue());
            throw new PaymentApplicationServiceException("Could not find credit history for customer: " + customerId.getValue());
        }
        return creditHistoryList.get();

    }

    private void persistDbObjects(Payment payment, CreditEntry creditEntry, List<CreditHistory> creditHistoryList, List<String> failureMessages) {
        paymentRepository.save(payment);
        if (failureMessages.isEmpty()) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistoryList.get(creditHistoryList.size() - 1));
        }
    }
}
