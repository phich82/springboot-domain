package com.sonha.restaurant.service.domain.ports.output.repository;

import com.sonha.restaurant.service.domain.entity.OrderApproval;

public interface OrderApprovalRepository {
    OrderApproval save(OrderApproval orderApproval);
}
