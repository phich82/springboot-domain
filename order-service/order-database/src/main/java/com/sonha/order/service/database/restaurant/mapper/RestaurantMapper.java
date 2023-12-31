package com.sonha.order.service.database.restaurant.mapper;

import com.sonha.database.restaurant.entity.RestaurantEntity;
import com.sonha.database.restaurant.exception.RestaurantException;
import com.sonha.domain.valueobject.Money;
import com.sonha.domain.valueobject.ProductId;
import com.sonha.domain.valueobject.RestaurantId;
import com.sonha.order.service.domain.entity.Product;
import com.sonha.order.service.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantMapper {

    public List<UUID> restaurantToRestaurantProductIds(Restaurant restaurant) {
        return restaurant.getProducts().stream().map(product -> product.getId().getValue()).collect(Collectors.toList());
    }

    public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {
        RestaurantEntity restaurantEntity = restaurantEntities.stream()
                .findFirst()
                .orElseThrow(() -> new RestaurantException("Restaurant could not be found!"));
        List<Product> restaurantProducts = restaurantEntities.stream()
                .map(entity -> new Product(
                        new ProductId(entity.getProductId()),
                        entity.getProductName(),
                        new Money(entity.getProductPrice())
                ))
                .collect(Collectors.toList());
        return Restaurant.Builder.builder()
                .restaurantId(new RestaurantId(restaurantEntity.getRestaurantId()))
                .products(restaurantProducts)
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }
}
