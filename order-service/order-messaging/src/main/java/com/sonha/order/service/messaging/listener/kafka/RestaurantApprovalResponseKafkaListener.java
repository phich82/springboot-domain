package com.sonha.order.service.messaging.listener.kafka;

import com.sonha.kafka.consumer.service.KafkaConsumer;
import com.sonha.kafka.order.avro.model.OrderApprovalStatus;
import com.sonha.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.sonha.order.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalResponseMessageListener;
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
public class RestaurantApprovalResponseKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {

    //@Autowired
    private final RestaurantApprovalResponseMessageListener restaurantApprovalResponseKafkaListener;
    //@Autowired
    private final OrderMessagingMapper orderMessagingMapper;

    public RestaurantApprovalResponseKafkaListener(
            RestaurantApprovalResponseMessageListener restaurantApprovalResponseKafkaListener,
            OrderMessagingMapper orderMessagingMapper) {
        this.restaurantApprovalResponseKafkaListener = restaurantApprovalResponseKafkaListener;
        this.orderMessagingMapper = orderMessagingMapper;
    }

    @Override
    @KafkaListener(
            id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${order-service.restaurant-approval-response-topic-name}"
    )
    public void receive(@Payload List<RestaurantApprovalResponseAvroModel> messages,
                        //@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        //@Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of restaurant approval responses received with keys: {}, partitions: {} and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());
        messages.forEach(restaurantApprovalResponseAvroModel -> {
            if (restaurantApprovalResponseAvroModel.getOrderApprovalStatus() == OrderApprovalStatus.APPROVED) {
                log.info("Processing approved order for order id: {}", restaurantApprovalResponseAvroModel.getOrderId());
                restaurantApprovalResponseKafkaListener.orderApproved(
                        orderMessagingMapper.approvalResponseAvroModelToApprovalResponseDto(restaurantApprovalResponseAvroModel)
                );
            } else if (restaurantApprovalResponseAvroModel.getOrderApprovalStatus() == OrderApprovalStatus.REJECTED) {
                log.info("Processing rejected order for order id: {}, with failure messages: {}",
                        restaurantApprovalResponseAvroModel.getOrderId(),
                        String.join(FAILURE_MESSAGE_DELIMITER, restaurantApprovalResponseAvroModel.getFailureMessages()));
                restaurantApprovalResponseKafkaListener.orderRejected(
                        orderMessagingMapper.approvalResponseAvroModelToApprovalResponseDto(restaurantApprovalResponseAvroModel)
                );
            }
        });
    }
}
