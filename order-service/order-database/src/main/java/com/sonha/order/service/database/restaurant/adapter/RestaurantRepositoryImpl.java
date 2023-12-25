package com.sonha.order.service.database.restaurant.adapter;

import com.sonha.database.restaurant.entity.RestaurantEntity;
import com.sonha.database.restaurant.repository.RestaurantJpaRepository;
import com.sonha.order.service.database.restaurant.mapper.RestaurantMapper;
import com.sonha.order.service.domain.entity.Order;
import com.sonha.order.service.domain.entity.Restaurant;
import com.sonha.order.service.domain.ports.output.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantRepositoryImpl implements RestaurantRepository {

    //@Autowired
    private final RestaurantJpaRepository restaurantJpaRepository;
    //@Autowired
    private final RestaurantMapper restaurantMapper;

    public RestaurantRepositoryImpl(RestaurantJpaRepository restaurantJpaRepository, RestaurantMapper restaurantMapper) {
        this.restaurantJpaRepository = restaurantJpaRepository;
        this.restaurantMapper = restaurantMapper;
    }

    @Override
    public Order save(Order order) {
        return null;
    }

    @Override
    public Optional<Restaurant> findByRestaurant(Restaurant restaurant) {
        List<UUID> restaurantProductIds = restaurantMapper.restaurantToRestaurantProductIds(restaurant);
        Optional<List<RestaurantEntity>> restaurantEntities = restaurantJpaRepository.findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(), restaurantProductIds);
        //Optional<List<RestaurantEntity>> restaurantEntities = restaurantJpaRepository.findByRestaurantIdAndProductId(restaurant.getId().getValue(), restaurantProductIds.get(0));
        return restaurantEntities.map(restaurantMapper::restaurantEntityToRestaurant);
    }
}
