package com.sonha.restaurant.service.domain.ports.output.repository;

import com.sonha.restaurant.service.domain.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {
    Optional<Restaurant> findRestaurant(Restaurant restaurant);
}
