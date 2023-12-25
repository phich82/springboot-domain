package com.sonha.order.service.domain.ports.output.repository;

import com.sonha.order.service.domain.entity.Order;
import com.sonha.order.service.domain.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {
    Order save(Order order);
    Optional<Restaurant> findByRestaurant(Restaurant restaurant);
}
