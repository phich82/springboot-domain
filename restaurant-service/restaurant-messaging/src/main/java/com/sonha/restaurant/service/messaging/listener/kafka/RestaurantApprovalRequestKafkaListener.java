package com.sonha.restaurant.service.messaging.listener.kafka;

import com.sonha.kafka.consumer.service.KafkaConsumer;
import com.sonha.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.sonha.restaurant.service.domain.ports.input.message.listener.impl.RestaurantApprovalRequestMessageListenerImpl;
import com.sonha.restaurant.service.messaging.mapper.RestaurantMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RestaurantApprovalRequestKafkaListener implements KafkaConsumer<RestaurantApprovalRequestAvroModel> {

    private final RestaurantApprovalRequestMessageListenerImpl restaurantApprovalRequestMessageListener;
    private final RestaurantMessageMapper restaurantMessageMapper;

    public RestaurantApprovalRequestKafkaListener(RestaurantApprovalRequestMessageListenerImpl restaurantApprovalRequestMessageListener,
                                                  RestaurantMessageMapper restaurantMessageMapper) {
        this.restaurantApprovalRequestMessageListener = restaurantApprovalRequestMessageListener;
        this.restaurantMessageMapper = restaurantMessageMapper;
    }

    @Override
    @KafkaListener(
            id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${restaurant-service.restaurant-approval-request-topic-name}"
    )
    public void receive(@Payload List<RestaurantApprovalRequestAvroModel> messages,
                        //@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        //@Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of order approval requests received with keys: {}, partitions: {} and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(restaurantApprovalRequestAvroModel -> {
            log.info("Processing order approval for order id: {}", restaurantApprovalRequestAvroModel.getOrderId());
            restaurantApprovalRequestMessageListener.approveOrder(
                    restaurantMessageMapper.restaurantApprovalRequestAvroModelToRestaurantApproval(restaurantApprovalRequestAvroModel)
            );
        });
    }

}
