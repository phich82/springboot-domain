package com.sonha.order.service.messaging.mapper;

import com.sonha.domain.valueobject.OrderApprovalStatus;
import com.sonha.domain.valueobject.PaymentStatus;
import com.sonha.kafka.order.avro.model.PaymentOrderStatus;
import com.sonha.kafka.order.avro.model.PaymentRequestAvroModel;
import com.sonha.kafka.order.avro.model.PaymentResponseAvroModel;
import com.sonha.kafka.order.avro.model.Product;
import com.sonha.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.sonha.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.sonha.kafka.order.avro.model.RestaurantOrderStatus;
import com.sonha.order.service.domain.dto.message.PaymentResponseDto;
import com.sonha.order.service.domain.dto.message.RestaurantApprovalResponseDto;
import com.sonha.order.service.domain.entity.Order;
import com.sonha.order.service.domain.event.OrderCancelledEvent;
import com.sonha.order.service.domain.event.OrderCreatedEvent;
import com.sonha.order.service.domain.event.OrderPaidEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderMessagingMapper {
    public PaymentRequestAvroModel orderCreatedEventToPaymentRequestAvroModel(OrderCreatedEvent orderCreatedEvent) {
        Order order = orderCreatedEvent.getOrder();
        System.out.println("OrderMessagingMapper:orderCreatedEventToPaymentRequestAvroModel => start");
        String sagaId = UUID.randomUUID().toString();
        PaymentRequestAvroModel paymentRequestAvroModel = PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.fromString(sagaId))
                .setCustomerId(order.getCustomerId().getValue())
                .setOrderId(order.getId().getValue())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCreatedEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();
        System.out.println("OrderMessagingMapper:orderCreatedEventToPaymentRequestAvroModel => end");
        return paymentRequestAvroModel;
    }

    public PaymentRequestAvroModel orderCancelledEventToPaymentRequestAvroModel(OrderCancelledEvent orderCancelledEvent) {
        Order order = orderCancelledEvent.getOrder();
        System.out.println("OrderMessagingMapper:orderCancelledEventToPaymentRequestAvroModel => start");
        String sagaId = UUID.randomUUID().toString();
        PaymentRequestAvroModel paymentRequestAvroModel = PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.fromString(sagaId))
                .setCustomerId(order.getCustomerId().getValue())
                .setOrderId(order.getId().getValue())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCancelledEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.CANCELLED)
                .build();
        System.out.println("OrderMessagingMapper:orderCancelledEventToPaymentRequestAvroModel => end");
        return paymentRequestAvroModel;
    }

    public RestaurantApprovalRequestAvroModel orderPaidEventToRestaurantApprovalRequestAvroModel(OrderPaidEvent orderPaidEvent) {
        Order order = orderPaidEvent.getOrder();
        System.out.println("OrderMessagingMapper:orderPaidEventToRestaurantApprovalRequestAvroModel => start");
        String sagaId = UUID.randomUUID().toString();
        RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel = RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.fromString(sagaId))
                .setOrderId(order.getId().getValue())
                .setRestaurantId(order.getRestaurantId().getValue())
                .setRestaurantOrderStatus(RestaurantOrderStatus.valueOf(order.getOrderStatus().name()))
                .setProducts(order.getItems().stream().map(orderItem -> Product.newBuilder()
                        .setId(orderItem.getProduct().getId().getValue().toString())
                        .setQuantity(orderItem.getQuanlity())
                        .build())
                        .toList())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderPaidEvent.getCreatedAt().toInstant())
                .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
                .build();
        System.out.println("OrderMessagingMapper:orderPaidEventToRestaurantApprovalRequestAvroModel => end");
        return restaurantApprovalRequestAvroModel;
    }

    public PaymentResponseDto paymentResponseAvroModelToPaymentResponseDto(PaymentResponseAvroModel paymentResponseAvroModel) {
        System.out.println("OrderMessagingMapper:paymentResponseAvroModelToPaymentResponseDto => start");
        PaymentResponseDto paymentResponseDto = PaymentResponseDto.builder()
                .id(paymentResponseAvroModel.getId().toString())
                .sagaId(paymentResponseAvroModel.getSagaId().toString())
                .paymentId(paymentResponseAvroModel.getPaymentId().toString())
                .customerId(paymentResponseAvroModel.getCustomerId().toString())
                .orderId(paymentResponseAvroModel.getOrderId().toString())
                .price(paymentResponseAvroModel.getPrice())
                .createdAt(paymentResponseAvroModel.getCreatedAt())
                .paymentStatus(PaymentStatus.valueOf(paymentResponseAvroModel.getPaymentStatus().name()))
                .failureMessages(paymentResponseAvroModel.getFailureMessages())
                .build();
        System.out.println("OrderMessagingMapper:paymentResponseAvroModelToPaymentResponseDto => end");
        return paymentResponseDto;
    }

    public RestaurantApprovalResponseDto approvalResponseAvroModelToApprovalResponseDto(RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel) {
        System.out.println("OrderMessagingMapper:approvalResponseAvroModelToApprovalResponseDto => start");
        RestaurantApprovalResponseDto restaurantApprovalResponseDto = RestaurantApprovalResponseDto.builder()
                .id(restaurantApprovalResponseAvroModel.getId().toString())
                .sagaId(restaurantApprovalResponseAvroModel.getSagaId().toString())
                .orderId(restaurantApprovalResponseAvroModel.getOrderId().toString())
                .restaurantId(restaurantApprovalResponseAvroModel.getRestaurantId().toString())
                .createdAt(restaurantApprovalResponseAvroModel.getCreatedAt())
                .orderApprovalStatus(OrderApprovalStatus.valueOf(restaurantApprovalResponseAvroModel.getOrderApprovalStatus().name()))
                .failureMessages(restaurantApprovalResponseAvroModel.getFailureMessages())
                .build();
        System.out.println("OrderMessagingMapper:approvalResponseAvroModelToApprovalResponseDto => end");
        return restaurantApprovalResponseDto;
    }

}
