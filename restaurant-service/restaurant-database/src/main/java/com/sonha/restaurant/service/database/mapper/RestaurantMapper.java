package com.sonha.restaurant.service.database.mapper;

import com.sonha.database.restaurant.entity.RestaurantEntity;
import com.sonha.database.restaurant.exception.RestaurantException;
import com.sonha.domain.valueobject.Money;
import com.sonha.domain.valueobject.OrderId;
import com.sonha.domain.valueobject.ProductId;
import com.sonha.domain.valueobject.RestaurantId;
import com.sonha.restaurant.service.database.entity.OrderApprovalEntity;
import com.sonha.restaurant.service.domain.entity.OrderApproval;
import com.sonha.restaurant.service.domain.entity.OrderDetail;
import com.sonha.restaurant.service.domain.entity.Product;
import com.sonha.restaurant.service.domain.entity.Restaurant;
import com.sonha.restaurant.service.domain.valueobject.OrderApprovalId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantMapper {

    public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
        return restaurant.getOrderDetail().getProducts().stream()
                .map(product -> product.getId().getValue()).collect(Collectors.toList());
    }

    public List<UUID> restaurantToRestaurantProductIds(Restaurant restaurant) {
        return restaurant.getOrderDetail().getProducts().stream()
                .map(product -> product.getId().getValue()).collect(Collectors.toList());
    }

    public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntityList) {
        RestaurantEntity restaurantEntity = restaurantEntityList.stream().findFirst()
                .orElseThrow(() -> new RestaurantException("No restaurants found!"));
        List<Product> restaurantProducts = restaurantEntityList.stream()
                .map(entity -> Product.builder()
                        .productId(new ProductId(entity.getProductId()))
                        .name(entity.getProductName())
                        .price(new Money(entity.getProductPrice()))
                        .available(entity.getProductAvailable())
                        .build())
                .collect(Collectors.toList());
        return Restaurant.builder()
                .restaurantId(new RestaurantId(restaurantEntity.getRestaurantId()))
                .orderDetail(OrderDetail.builder().products(restaurantProducts).build())
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }

    public OrderApprovalEntity orderApprovalToOrderApprovalEntity(OrderApproval orderApproval) {
        return OrderApprovalEntity.builder()
                .id(orderApproval.getOrderId().getValue())
                .restaurantId(orderApproval.getRestaurantId().getValue())
                .orderId(orderApproval.getOrderId().getValue())
                .status(orderApproval.getApprovalStatus())
                .build();
    }

    public OrderApproval orderApprovalEntityToOrderApproval(OrderApprovalEntity orderApprovalEntity) {
        return OrderApproval.builder()
                .orderApprovalId(new OrderApprovalId(orderApprovalEntity.getOrderId()))
                .restaurantId(new RestaurantId(orderApprovalEntity.getRestaurantId()))
                .orderId(new OrderId(orderApprovalEntity.getOrderId()))
                .approvalStatus(orderApprovalEntity.getStatus())
                .build();
    }

}
