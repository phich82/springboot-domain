package com.sonha.payment.service.message.listener.kafka;

import com.sonha.kafka.consumer.service.KafkaConsumer;
import com.sonha.kafka.order.avro.model.PaymentOrderStatus;
import com.sonha.kafka.order.avro.model.PaymentRequestAvroModel;
import com.sonha.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import com.sonha.payment.service.message.mapper.PaymentMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PaymentRequestKafkaListener implements KafkaConsumer<PaymentRequestAvroModel> {

    private final PaymentRequestMessageListener paymentRequestMessageListener;
    private final PaymentMessageMapper paymentMessageMapper;

    public PaymentRequestKafkaListener(PaymentRequestMessageListener paymentRequestMessageListener,
                                       PaymentMessageMapper paymentMessageMapper) {
        this.paymentRequestMessageListener = paymentRequestMessageListener;
        this.paymentMessageMapper = paymentMessageMapper;
    }

    @Override
    @KafkaListener(
            id = "${kafka-consumer-config.payment-consumer-group-id}",
            topics = "${payment-service.payment-request-topic-name}"
    )
    public void receive(@Payload List<PaymentRequestAvroModel> messages,
                        //@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        //@Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of payment requests received with keys: {}, partitions: {} and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(paymentRequestAvroModel -> {
            if (paymentRequestAvroModel.getPaymentOrderStatus() == PaymentOrderStatus.PENDING) {
                log.info("Processing payment for order id: {}", paymentRequestAvroModel.getOrderId());
                paymentRequestMessageListener.completePayment(
                        paymentMessageMapper.paymentRequestAvroModelToPaymentRequest(paymentRequestAvroModel)
                );
            } else if (paymentRequestAvroModel.getPaymentOrderStatus() == PaymentOrderStatus.CANCELLED) {
                log.info("Cancelling payment for order id: {}", paymentRequestAvroModel.getOrderId());
                paymentRequestMessageListener.cancelPayment(
                        paymentMessageMapper.paymentRequestAvroModelToPaymentRequest(paymentRequestAvroModel)
                );
            }
        });

    }
}
