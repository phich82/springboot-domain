package com.sonha.restaurant.service.database.adapter;

import com.sonha.database.restaurant.entity.RestaurantEntity;
import com.sonha.database.restaurant.repository.RestaurantJpaRepository;
import com.sonha.restaurant.service.database.mapper.RestaurantMapper;
import com.sonha.restaurant.service.domain.entity.Restaurant;
import com.sonha.restaurant.service.domain.ports.output.repository.RestaurantRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantMapper restaurantMapper;

    public RestaurantRepositoryImpl(RestaurantJpaRepository restaurantJpaRepository, RestaurantMapper restaurantMapper) {
        this.restaurantJpaRepository = restaurantJpaRepository;
        this.restaurantMapper = restaurantMapper;
    }

    @Override
    public Optional<Restaurant> findRestaurant(Restaurant restaurant) {
        List<UUID> restaurantProductIds = restaurantMapper.restaurantToRestaurantProductIds(restaurant);
        Optional<List<RestaurantEntity>> restaurantEntityList = restaurantJpaRepository.findByRestaurantIdAndProductIdIn(
                restaurant.getId().getValue(),
                restaurantProductIds
        );
        return restaurantEntityList.map(restaurantMapper::restaurantEntityToRestaurant);
    }
}
