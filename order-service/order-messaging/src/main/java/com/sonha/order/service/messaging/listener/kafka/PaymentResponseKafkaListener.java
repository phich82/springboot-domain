package com.sonha.order.service.messaging.listener.kafka;

import com.sonha.kafka.consumer.service.KafkaConsumer;
import com.sonha.kafka.order.avro.model.PaymentResponseAvroModel;
import com.sonha.kafka.order.avro.model.PaymentStatus;
import com.sonha.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import com.sonha.order.service.messaging.mapper.OrderMessagingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.sonha.order.service.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;

@Slf4j
@Component
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {

    //@Autowired
    private final PaymentResponseMessageListener paymentResponseMessageListener;
    //@Autowired
    private final OrderMessagingMapper orderMessagingMapper;

    public PaymentResponseKafkaListener(
            PaymentResponseMessageListener paymentResponseMessageListener,
            OrderMessagingMapper orderMessagingMapper) {
        this.paymentResponseMessageListener = paymentResponseMessageListener;
        this.orderMessagingMapper = orderMessagingMapper;
    }

    @Override
    @KafkaListener(
            id = "${kafka-consumer-config.payment-consumer-group-id}",
            topics = "${order-service.payment-response-topic-name}"
    )
    public void receive(@Payload List<PaymentResponseAvroModel> messages,
                        //@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        //@Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of payment responses received with keys: {}, partitions: {} and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());
        messages.forEach(paymentResponseAvroModel -> {
            if (paymentResponseAvroModel.getPaymentStatus() == PaymentStatus.COMPLETED) {
                log.info("Processing successfully payment for order id: {}", paymentResponseAvroModel.getOrderId());
                paymentResponseMessageListener.paymentCompleted(
                        orderMessagingMapper.paymentResponseAvroModelToPaymentResponseDto(paymentResponseAvroModel)
                );
            } else if (paymentResponseAvroModel.getPaymentStatus() == PaymentStatus.CANCELLED || paymentResponseAvroModel.getPaymentStatus() == PaymentStatus.FAILED) {
                log.info("Processing unsuccessfully payment for order id: {}, with failure messages: {}",
                        paymentResponseAvroModel.getOrderId(),
                        String.join(FAILURE_MESSAGE_DELIMITER, paymentResponseAvroModel.getFailureMessages()));
                paymentResponseMessageListener.paymentCancelled(
                        orderMessagingMapper.paymentResponseAvroModelToPaymentResponseDto(paymentResponseAvroModel)
                );
            }
        });
    }
}
