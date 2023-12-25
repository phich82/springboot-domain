package com.sonha.order.service.domain.ports.output.repository;

import com.sonha.order.service.domain.entity.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Optional<Customer> find(UUID customerId);
}
