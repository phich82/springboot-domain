package com.sonha.restaurant.service.domain.mapper;

import com.sonha.domain.valueobject.Money;
import com.sonha.domain.valueobject.OrderId;
import com.sonha.domain.valueobject.OrderStatus;
import com.sonha.domain.valueobject.RestaurantId;
import com.sonha.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.sonha.restaurant.service.domain.entity.OrderDetail;
import com.sonha.restaurant.service.domain.entity.Product;
import com.sonha.restaurant.service.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantMapper {
    public Restaurant restaurantApprovalRequestToRestaurant(RestaurantApprovalRequest restaurantApprovalRequest) {
        return Restaurant.builder()
                .restaurantId(new RestaurantId(UUID.fromString(restaurantApprovalRequest.getRestaurantId())))
                .orderDetail(OrderDetail.builder()
                        .orderId(new OrderId(UUID.fromString(restaurantApprovalRequest.getOrderId())))
                        .products(restaurantApprovalRequest.getProducts().stream()
                                .map(product -> Product.builder()
                                        .productId(product.getId())
                                        .quantity(product.getQuantity()).build())
                                .collect(Collectors.toList()))
                        .totalAmount(new Money(restaurantApprovalRequest.getPrice()))
                        .orderStatus(OrderStatus.valueOf(restaurantApprovalRequest.getRestaurantOrderStatus().name()))
                        .build()
                )
                .build();
    }
}
