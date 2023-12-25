package com.sonha.order.service.database.order.repository;

import com.sonha.order.service.database.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {
    Optional<OrderEntity> findByTrackingId(UUID trackingId);
}
