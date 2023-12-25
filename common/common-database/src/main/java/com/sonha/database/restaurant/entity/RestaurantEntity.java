package com.sonha.database.restaurant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RestaurantEntityId.class)
@Table(name = "shc_order_restaurant_m_view", schema = "restaurants")
@Entity
public class RestaurantEntity {

    @Id
    @Column(name = "restaurant_id")
    private UUID restaurantId;

    @Id
    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "restaurant_name")
    private String restaurantName;

    @Column(name = "restaurant_active")
    private Boolean restaurantActive;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private BigDecimal productPrice;

    @Column(name = "product_available")
    private Boolean productAvailable;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantEntity that = (RestaurantEntity) o;
        return Objects.equals(restaurantId, that.restaurantId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantId, productId);
    }
}
