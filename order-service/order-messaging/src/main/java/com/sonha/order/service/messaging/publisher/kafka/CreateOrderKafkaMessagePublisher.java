package com.sonha.order.service.messaging.publisher.kafka;

import com.sonha.kafka.order.avro.model.PaymentRequestAvroModel;
import com.sonha.kafka.producer.KafkaMessageHelper;
import com.sonha.kafka.producer.service.KafkaProducer;
import com.sonha.order.service.domain.config.OrderServiceConfigData;
import com.sonha.order.service.domain.event.OrderCreatedEvent;
import com.sonha.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import com.sonha.order.service.messaging.mapper.OrderMessagingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreateOrderKafkaMessagePublisher implements OrderCreatedPaymentRequestMessagePublisher {

    //@Autowired
    private final OrderMessagingMapper orderMessagingMapper;
    //@Autowired
    private final OrderServiceConfigData orderServiceConfigData;
    //@Autowired
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;
    //@Autowired
    private final KafkaMessageHelper kafkaMessageHelper;

    public CreateOrderKafkaMessagePublisher(
            OrderMessagingMapper orderMessagingMapper,
            OrderServiceConfigData orderServiceConfigData,
            KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer,
            KafkaMessageHelper kafkaMessageHelper) {
        this.orderMessagingMapper = orderMessagingMapper;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }


    @Override
    public void publish(OrderCreatedEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();
        log.info("Received OrderCreatedEvent for order id: {}", orderId);
        try {
            PaymentRequestAvroModel paymentRequestAvroModel = orderMessagingMapper.orderCreatedEventToPaymentRequestAvroModel(domainEvent);
            kafkaProducer.send(
                    orderServiceConfigData.getPaymentRequestTopicName(),
                    orderId,
                    paymentRequestAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            orderServiceConfigData.getPaymentRequestTopicName(),
                            paymentRequestAvroModel,
                            orderId,
                            "PaymentRequestAvroModel"));
            log.info("PaymentRequestAvroModel sent to kafka for order id: {}", orderId);
        } catch (Exception e) {
            log.error("Error while sending PaymentRequestAvroModel message to kafka with order id: {}, error: {}", orderId, e.getMessage());
        }
    }
}
