package com.sonha.order.service.messaging.publisher.kafka;

import com.sonha.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.sonha.kafka.producer.KafkaMessageHelper;
import com.sonha.kafka.producer.service.KafkaProducer;
import com.sonha.order.service.domain.config.OrderServiceConfigData;
import com.sonha.order.service.domain.event.OrderPaidEvent;
import com.sonha.order.service.domain.ports.output.message.publisher.restaurantapproval.OrderPaidPaymentRequestMessagePublisher;
import com.sonha.order.service.messaging.mapper.OrderMessagingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PayOrderKafkaMessagePublisher implements OrderPaidPaymentRequestMessagePublisher {

    //@Autowired
    private final OrderMessagingMapper orderMessagingMapper;
    //@Autowired
    private final OrderServiceConfigData orderServiceConfigData;
    //@Autowired
    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;
    //@Autowired
    private final KafkaMessageHelper kafkaMessageHelper;

    public PayOrderKafkaMessagePublisher(
            OrderMessagingMapper orderMessagingMapper,
            OrderServiceConfigData orderServiceConfigData,
            KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer,
            KafkaMessageHelper kafkaMessageHelper) {
        this.orderMessagingMapper = orderMessagingMapper;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }


    @Override
    public void publish(OrderPaidEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();
        try {
           RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel = orderMessagingMapper.orderPaidEventToRestaurantApprovalRequestAvroModel(domainEvent);
            kafkaProducer.send(
                    orderServiceConfigData.getPaymentRequestTopicName(),
                    orderId,
                    restaurantApprovalRequestAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            orderServiceConfigData.getRestaurantApprovalRequestTopicName(),
                            restaurantApprovalRequestAvroModel,
                            orderId,
                            "RestaurantApprovalRequestAvroModel"
                    )
            );
            log.info("RestaurantApprovalRequestAvroModel sent to kafka for order id: {}", orderId);
        } catch (Exception e) {
           log.error("Error while sending RestaurantApprovalRequestAvroModel message to kafka with order id: {}, error: {}", orderId, e.getMessage());
        }
    }
}
