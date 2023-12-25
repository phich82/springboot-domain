package com.sonha.order.service.domain.mapper;

import com.sonha.domain.valueobject.CustomerId;
import com.sonha.domain.valueobject.Money;
import com.sonha.domain.valueobject.ProductId;
import com.sonha.domain.valueobject.RestaurantId;
import com.sonha.order.service.domain.dto.create.CreateOrderCommand;
import com.sonha.order.service.domain.dto.create.CreateOrderResponseDto;
import com.sonha.order.service.domain.dto.create.OrderAddressDto;
import com.sonha.order.service.domain.dto.create.OrderItemDto;
import com.sonha.order.service.domain.dto.track.TrackOrderResponseDto;
import com.sonha.order.service.domain.entity.Order;
import com.sonha.order.service.domain.entity.OrderItem;
import com.sonha.order.service.domain.entity.Product;
import com.sonha.order.service.domain.entity.Restaurant;
import com.sonha.order.service.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {
    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
        return Restaurant.Builder.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(
                        createOrderCommand.getItems().stream().map(
                                orderItem -> new Product(new ProductId(orderItem.getProductId()))
                        ).collect(Collectors.toList())
                )
                .build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
        return Order.Builder.builder()
                .customerId(new CustomerId(createOrderCommand.getCustomerId()))
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.getAddress()))
                .price(new Money(createOrderCommand.getPrice()))
                .items(orderItemsToOrderItemEntities(createOrderCommand.getItems()))
                .build();
    }

    public CreateOrderResponseDto orderToCreateOrderResponse(Order order, String message) {
        return CreateOrderResponseDto.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .message(message)
                .build();
    }

    public TrackOrderResponseDto orderToTrackOrderResponse(Order order) {
        return TrackOrderResponseDto.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages())
                .build();
    }

    private StreetAddress orderAddressToStreetAddress(OrderAddressDto orderAddressDto) {
        return new StreetAddress(
                UUID.randomUUID(),
                orderAddressDto.getStreet(),
                orderAddressDto.getPostalCode(),
                orderAddressDto.getCity()
        );
    }

    private List<OrderItem> orderItemsToOrderItemEntities(List<OrderItemDto> orderItems) {
        return orderItems.stream().map(
                orderItemDto -> OrderItem.Builder.builder()
                        .product(new Product(new ProductId(orderItemDto.getProductId())))
                        .price(new Money(orderItemDto.getPrice()))
                        .quanlity(orderItemDto.getQuantity())
                        .subTotal(new Money(orderItemDto.getSubTotal()))
                        .build()
        ).collect(Collectors.toList());
    }

}
