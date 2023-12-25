package com.sonha.order.service.database.order.adapter;

import com.sonha.order.service.database.order.entity.OrderEntity;
import com.sonha.order.service.database.order.mapper.OrderMapper;
import com.sonha.order.service.database.order.repository.OrderJpaRepository;
import com.sonha.order.service.domain.entity.Order;
import com.sonha.order.service.domain.ports.output.repository.OrderRepository;
import com.sonha.order.service.domain.valueobject.TrackingId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class OrderRepositoryImpl implements OrderRepository {

    //@Autowired
    private final OrderJpaRepository orderJpaRepository;
    //@Autowired
    private final OrderMapper orderMapper;

    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository, OrderMapper orderMapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public Order save(Order order) {
        log.info("OrderStatus => " + order.getOrderStatus());
        return orderMapper.orderEntityToOrder(
                orderJpaRepository.save(orderMapper.orderToOrderEntity(order))
        );
    }

    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        return orderJpaRepository.findByTrackingId(trackingId.getValue())
                .map(orderMapper::orderEntityToOrder);
    }
}
