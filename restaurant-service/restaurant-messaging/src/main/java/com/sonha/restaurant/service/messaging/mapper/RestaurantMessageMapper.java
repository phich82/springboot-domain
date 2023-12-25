package com.sonha.restaurant.service.messaging.mapper;

import com.sonha.domain.valueobject.ProductId;
import com.sonha.domain.valueobject.RestaurantOrderStatus;
import com.sonha.kafka.order.avro.model.OrderApprovalStatus;
import com.sonha.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.sonha.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.sonha.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.sonha.restaurant.service.domain.entity.Product;
import com.sonha.restaurant.service.domain.event.OrderApprovedEvent;
import com.sonha.restaurant.service.domain.event.OrderRejectedEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantMessageMapper {

    public RestaurantApprovalResponseAvroModel orderApprovedEventToRestaurantApprovalResponseAvroModel(OrderApprovedEvent orderApprovedEvent) {
        return RestaurantApprovalResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.randomUUID())
                .setOrderId(orderApprovedEvent.getOrderApproval().getOrderId().getValue())
                .setRestaurantId(orderApprovedEvent.getRestaurantId().getValue())
                .setCreatedAt(orderApprovedEvent.getCreatedAt().toInstant())
                .setOrderApprovalStatus(OrderApprovalStatus.valueOf(orderApprovedEvent.getOrderApproval().getApprovalStatus().name()))
                .setFailureMessages(orderApprovedEvent.getFailureMessages())
                .build();
    }

    public RestaurantApprovalResponseAvroModel orderRejectedEventToRestaurantApprovalResponseAvroModel(OrderRejectedEvent orderRejectedEvent) {
        return RestaurantApprovalResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.randomUUID())
                .setOrderId(orderRejectedEvent.getOrderApproval().getOrderId().getValue())
                .setRestaurantId(orderRejectedEvent.getRestaurantId().getValue())
                .setCreatedAt(orderRejectedEvent.getCreatedAt().toInstant())
                .setOrderApprovalStatus(OrderApprovalStatus.valueOf(orderRejectedEvent.getOrderApproval().getApprovalStatus().name()))
                .setFailureMessages(orderRejectedEvent.getFailureMessages())
                .build();
    }

    public RestaurantApprovalRequest restaurantApprovalRequestAvroModelToRestaurantApproval(RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel) {
        return RestaurantApprovalRequest.builder()
                .id(restaurantApprovalRequestAvroModel.getId().toString())
                .sagaId(restaurantApprovalRequestAvroModel.getSagaId().toString())
                .restaurantId(restaurantApprovalRequestAvroModel.getRestaurantId().toString())
                .orderId(restaurantApprovalRequestAvroModel.getOrderId().toString())
                .restaurantOrderStatus(RestaurantOrderStatus.valueOf(restaurantApprovalRequestAvroModel.getRestaurantOrderStatus().name()))
                .products(restaurantApprovalRequestAvroModel.getProducts().stream()
                        .map(avroModel -> Product.builder()
                                .productId(new ProductId(UUID.fromString(avroModel.getId())))
                                .quantity(avroModel.getQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .price(restaurantApprovalRequestAvroModel.getPrice())
                .createdAt(restaurantApprovalRequestAvroModel.getCreatedAt())
                .build();
    }

}