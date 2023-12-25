package com.sonha.payment.service.domain.service.impl;

import com.sonha.domain.event.publisher.DomainEventPublisher;
import com.sonha.domain.valueobject.Money;
import com.sonha.domain.valueobject.PaymentStatus;
import com.sonha.payment.service.domain.entity.CreditEntry;
import com.sonha.payment.service.domain.entity.CreditHistory;
import com.sonha.payment.service.domain.entity.Payment;
import com.sonha.payment.service.domain.event.PaymentCancelledEvent;
import com.sonha.payment.service.domain.event.PaymentCompletedEvent;
import com.sonha.payment.service.domain.event.PaymentEvent;
import com.sonha.payment.service.domain.event.PaymentFailedEvent;
import com.sonha.payment.service.domain.service.PaymentDomainService;
import com.sonha.payment.service.domain.valueobject.CreditHistoryId;
import com.sonha.payment.service.domain.valueobject.TransactionType;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static com.sonha.domain.constant.DomainConst.ZONE_ID;

@Slf4j
public class PaymentDomainServiceImpl implements PaymentDomainService {
    @Override
    public PaymentEvent validateAndInitiatePayment(Payment payment,
                                                   CreditEntry creditEntry,
                                                   List<CreditHistory> creditHistoryList,
                                                   List<String> failureMessages,
                                                   DomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventDomainEventPublisher,
                                                   DomainEventPublisher<PaymentFailedEvent> paymentFailedEventDomainEventPublisher) {
        payment.initializePayment();
        payment.validatePayment(failureMessages);
        validateCreditEntry(payment, creditEntry, failureMessages);
        subtractCreditEntry(payment, creditEntry);
        updateCreditHistory(payment, creditHistoryList, TransactionType.DEBIT);
        validateCreditHistory(creditEntry, creditHistoryList, failureMessages);

        if (failureMessages.isEmpty()) {
            log.info("Payment is initiated for order id: {}", payment.getCustomerId().getValue());
            payment.updateStatus(PaymentStatus.COMPLETED);
            return new PaymentCompletedEvent(payment, ZonedDateTime.now(ZoneId.of(ZONE_ID)), paymentCompletedEventDomainEventPublisher);
        }
        log.info("Payment initiation is failed for order id: {}", payment.getCustomerId().getValue());
        payment.updateStatus(PaymentStatus.FAILED);
        return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of(ZONE_ID)), failureMessages, paymentFailedEventDomainEventPublisher);
    }

    @Override
    public PaymentEvent validateAndCancelPayment(Payment payment,
                                                 CreditEntry creditEntry,
                                                 List<CreditHistory> creditHistoryList,
                                                 List<String> failureMessages,
                                                 DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventDomainEventPublisher,
                                                 DomainEventPublisher<PaymentFailedEvent> paymentFailedEventDomainEventPublisher) {
        payment.validatePayment(failureMessages);
        addCreditEntry(payment, creditEntry);
        updateCreditHistory(payment, creditHistoryList, TransactionType.CREDIT);

        if (failureMessages.isEmpty()) {
            log.info("Payment is cancelled for order id: {}", payment.getCustomerId().getValue());
            payment.updateStatus(PaymentStatus.CANCELLED);
            return new PaymentCancelledEvent(payment, ZonedDateTime.now(ZoneId.of(ZONE_ID)), paymentCancelledEventDomainEventPublisher);
        }
        log.info("Payment cancellation is failed for order id: {}", payment.getCustomerId().getValue());
        payment.updateStatus(PaymentStatus.FAILED);
        return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of(ZONE_ID)), failureMessages, paymentFailedEventDomainEventPublisher);
    }

    private void validateCreditEntry(Payment payment, CreditEntry creditEntry, List<String> failureMessages) {
        if (payment.getPrice().isGreaterThan(creditEntry.getTotalCreditAmount())) {
            log.error("Customer with id: {} doesn't enough credit for payment", payment.getCustomerId().getValue());
            failureMessages.add("Customer with id: " + payment.getCustomerId().getValue() + " doesn't enough credit for payment");
        }
    }

    private void subtractCreditEntry(Payment payment, CreditEntry creditEntry) {
        creditEntry.subtractCreditAmount(payment.getPrice());
    }

    private void updateCreditHistory(Payment payment, List<CreditHistory> creditHistoryList, TransactionType transactionType) {
        creditHistoryList.add(CreditHistory.Builder.builder()
                        .creditHistoryId(new CreditHistoryId(UUID.randomUUID()))
                        .customerId(payment.getCustomerId())
                        .amount(payment.getPrice())
                        .transactionType(transactionType)
                .build());
    }

    private void validateCreditHistory(CreditEntry creditEntry, List<CreditHistory> creditHistoryList, List<String> failureMessages) {
        Money totalCreditHistory = getTotalHistoryAmount(creditHistoryList, TransactionType.CREDIT);
        Money totalDebitHistory = getTotalHistoryAmount(creditHistoryList, TransactionType.DEBIT);
        if (totalDebitHistory.isGreaterThan(totalCreditHistory)) {
            log.error("Customer with id: {} doesn't enough credit according to credit history", creditEntry.getCustomerId().getValue());
            failureMessages.add("Customer with id= " + creditEntry.getCustomerId().getValue() + "doesn't enough credit according to credit history");
        }

        if (!creditEntry.getTotalCreditAmount().equals(totalCreditHistory.subtract(totalDebitHistory))) {
            log.error("Credit history total is not equal to current credit for customer id: {}!", creditEntry.getCustomerId().getValue());
            failureMessages.add("Credit history total is not equal to current credit for customer id: " + creditEntry.getCustomerId().getValue() + "!");
        }
    }

    private static Money getTotalHistoryAmount(List<CreditHistory> creditHistoryList, TransactionType transactionType) {
        return creditHistoryList.stream()
                .filter(creditHistory -> creditHistory.getTransactionType() == transactionType)
                .map(CreditHistory::getAmount)
                .reduce(Money.ZERO, Money::add);
    }

    private void addCreditEntry(Payment payment, CreditEntry creditEntry) {
        creditEntry.addCreditAmount(payment.getPrice());
    }

}
