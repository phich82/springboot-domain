package com.sonha.restaurant.service.messaging.publisher.kafka;

import com.sonha.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.sonha.kafka.producer.KafkaMessageHelper;
import com.sonha.kafka.producer.service.KafkaProducer;
import com.sonha.restaurant.service.domain.config.RestaurantServiceConfigData;
import com.sonha.restaurant.service.domain.event.OrderRejectedEvent;
import com.sonha.restaurant.service.domain.ports.output.message.publisher.OrderRejectedMessagePublisher;
import com.sonha.restaurant.service.messaging.mapper.RestaurantMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderRejectedKafkaMessagePublisher implements OrderRejectedMessagePublisher {

    private final RestaurantMessageMapper restaurantMessageMapper;
    private final KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer;
    private final RestaurantServiceConfigData restaurantServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    public OrderRejectedKafkaMessagePublisher(RestaurantMessageMapper restaurantMessageMapper,
                                              KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer,
                                              RestaurantServiceConfigData restaurantServiceConfigData,
                                              KafkaMessageHelper kafkaMessageHelper) {
        this.restaurantMessageMapper = restaurantMessageMapper;
        this.kafkaProducer = kafkaProducer;
        this.restaurantServiceConfigData = restaurantServiceConfigData;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }


    @Override
    public void publish(OrderRejectedEvent domainEvent) {
        String orderId = domainEvent.getOrderApproval().getOrderId().getValue().toString();
        log.info("Received OrderRejectedEvent for order id: {}", orderId);
        try {
            RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel = restaurantMessageMapper.orderRejectedEventToRestaurantApprovalResponseAvroModel(domainEvent);
            kafkaProducer.send(
                    restaurantServiceConfigData.getRestaurantApprovalResponseTopicName(),
                    orderId,
                    restaurantApprovalResponseAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            restaurantServiceConfigData.getRestaurantApprovalResponseTopicName(),
                            restaurantApprovalResponseAvroModel,
                            orderId,
                            "RestaurantApprovalResponseAvroModel"
                    )
            );
            log.info("RestaurantApprovalResponseAvroModel sent to kafka for order id: {}", orderId);
        } catch (Exception e) {
            log.error(
                    "Error while sending RestaurantApprovalResponseAvroModel message to kafka with order id: {}, error: {}",
                    orderId,
                    e.getMessage()
            );
        }
    }
}
