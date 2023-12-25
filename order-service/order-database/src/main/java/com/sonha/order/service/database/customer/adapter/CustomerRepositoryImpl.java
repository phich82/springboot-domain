package com.sonha.order.service.database.customer.adapter;

import com.sonha.order.service.database.customer.mapper.CustomerMapper;
import com.sonha.order.service.database.customer.repository.CustomerJpaRepository;
import com.sonha.order.service.domain.entity.Customer;
import com.sonha.order.service.domain.ports.output.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {

    //@Autowired
    private final CustomerJpaRepository customerJpaRepository;
    //@Autowired
    private final CustomerMapper customerMapper;

    public CustomerRepositoryImpl(CustomerJpaRepository customerJpaRepository, CustomerMapper customerMapper) {
        this.customerJpaRepository = customerJpaRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public Optional<Customer> find(UUID customerId) {
        return customerJpaRepository.findById(customerId).map(customerMapper::customerEntityToCustomer);
    }
}
