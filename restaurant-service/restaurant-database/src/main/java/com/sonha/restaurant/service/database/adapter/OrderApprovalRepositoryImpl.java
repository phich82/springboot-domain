package com.sonha.restaurant.service.database.adapter;

import com.sonha.restaurant.service.database.mapper.RestaurantMapper;
import com.sonha.restaurant.service.database.repository.OrderApprovalJpaRepository;
import com.sonha.restaurant.service.domain.entity.OrderApproval;
import com.sonha.restaurant.service.domain.ports.output.repository.OrderApprovalRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderApprovalRepositoryImpl implements OrderApprovalRepository {

    private final OrderApprovalJpaRepository orderApprovalJpaRepository;
    private final RestaurantMapper restaurantMapper;

    public OrderApprovalRepositoryImpl(OrderApprovalJpaRepository orderApprovalJpaRepository, RestaurantMapper restaurantMapper) {
        this.orderApprovalJpaRepository = orderApprovalJpaRepository;
        this.restaurantMapper = restaurantMapper;
    }

    @Override
    public OrderApproval save(OrderApproval orderApproval) {
        return restaurantMapper.orderApprovalEntityToOrderApproval(
                orderApprovalJpaRepository.save(restaurantMapper.orderApprovalToOrderApprovalEntity(orderApproval))
        );
    }
}
