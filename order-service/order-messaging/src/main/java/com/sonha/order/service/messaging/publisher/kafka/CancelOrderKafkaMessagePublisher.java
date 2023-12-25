package com.sonha.order.service.messaging.publisher.kafka;

import com.sonha.kafka.order.avro.model.PaymentRequestAvroModel;
import com.sonha.kafka.producer.KafkaMessageHelper;
import com.sonha.kafka.producer.service.KafkaProducer;
import com.sonha.order.service.domain.config.OrderServiceConfigData;
import com.sonha.order.service.domain.event.OrderCancelledEvent;
import com.sonha.order.service.domain.ports.output.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisher;
import com.sonha.order.service.messaging.mapper.OrderMessagingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CancelOrderKafkaMessagePublisher implements OrderCancelledPaymentRequestMessagePublisher {

    //@Autowired
    private final OrderMessagingMapper orderMessagingMapper;
    //@Autowired
    private final OrderServiceConfigData orderServiceConfigData;
    //@Autowired
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;
    //@Autowired
    private final KafkaMessageHelper kafkaMessageHelper;

    public CancelOrderKafkaMessagePublisher(
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
    public void publish(OrderCancelledEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();
        log.info("Received OrderCancelledEvent for order id: {}", orderId);
        try {
            PaymentRequestAvroModel paymentRequestAvroModel = orderMessagingMapper.orderCancelledEventToPaymentRequestAvroModel(domainEvent);
            kafkaProducer.send(
                    orderServiceConfigData.getPaymentRequestTopicName(),
                    orderId,
                    paymentRequestAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            orderServiceConfigData.getPaymentRequestTopicName(),
                            paymentRequestAvroModel,
                            orderId,
                            "PaymentRequestAvroModel"
                    ));
            log.info("PaymentRequestAvroModel sent to kafka for order id: {}", orderId);
        } catch (Exception e) {
            log.error("Error while sending PaymentRequestAvroModel message to kafka with order id: {}, error: {}", orderId, e.getMessage());
        }
    }
}
