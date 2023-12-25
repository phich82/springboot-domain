package com.sonha.restaurant.service.database.entity;

import com.sonha.domain.valueobject.OrderApprovalStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sh_order_approval", schema = "restaurants")
@Entity
public class OrderApprovalEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "restaurant_id")
    private UUID restaurantId;

    @Column(name = "order_id")
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderApprovalStatus status;
}
