package com.sonha.database.restaurant.repository;

import com.sonha.database.restaurant.entity.RestaurantEntity;
import com.sonha.database.restaurant.entity.RestaurantEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, RestaurantEntityId> {
    Optional<List<RestaurantEntity>> findByRestaurantIdAndProductIdIn(UUID restaurantId, List<UUID> productIds);
    //Optional<List<RestaurantEntity>> findByRestaurantIdAndProductId(UUID restaurantId, UUID productId);
}
