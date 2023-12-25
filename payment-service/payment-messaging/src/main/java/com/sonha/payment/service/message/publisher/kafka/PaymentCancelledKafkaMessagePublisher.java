package com.sonha.payment.service.message.publisher.kafka;

import com.sonha.kafka.order.avro.model.PaymentResponseAvroModel;
import com.sonha.kafka.producer.KafkaMessageHelper;
import com.sonha.kafka.producer.service.KafkaProducer;
import com.sonha.payment.service.domain.config.PaymentServiceConfigData;
import com.sonha.payment.service.domain.event.PaymentCancelledEvent;
import com.sonha.payment.service.domain.ports.output.message.publisher.PaymentCancelledMessagePublisher;
import com.sonha.payment.service.message.mapper.PaymentMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentCancelledKafkaMessagePublisher implements PaymentCancelledMessagePublisher {

    private final PaymentMessageMapper paymentMessageMapper;
    private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;
    private final PaymentServiceConfigData paymentServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    public PaymentCancelledKafkaMessagePublisher(PaymentMessageMapper paymentMessageMapper,
                                                 KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer,
                                                 PaymentServiceConfigData paymentServiceConfigData,
                                                 KafkaMessageHelper kafkaMessageHelper) {
        this.paymentMessageMapper = paymentMessageMapper;
        this.kafkaProducer = kafkaProducer;
        this.paymentServiceConfigData = paymentServiceConfigData;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(PaymentCancelledEvent domainEvent) {
        String orderId = domainEvent.getPayment().getOrderId().getValue().toString();
        log.info("Received PaymentCancelledEvent for order id: {}", orderId);
        try {
            PaymentResponseAvroModel paymentResponseAvroModel = paymentMessageMapper.paymentCancelledEventToPaymentResponseAvroModel(domainEvent);
            kafkaProducer.send(
                    paymentServiceConfigData.getPaymentRequestTopicName(),
                    orderId,
                    paymentResponseAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            paymentServiceConfigData.getPaymentRequestTopicName(),
                            paymentResponseAvroModel,
                            orderId,
                            "PaymentResponseAvroModel"
                    )
            );
            log.info("PaymentResponseAvroModel sent to kafka for order id: {}", orderId);
        } catch (Exception e) {
            log.error(
                    "Error while sending PaymentResponseAvroModel message to kafka with order id: {}, error: {}",
                    orderId,
                    e.getMessage()
            );
        }
    }
}
