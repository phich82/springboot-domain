package com.sonha.order.service.database.customer.mapper;

import com.sonha.domain.valueobject.CustomerId;
import com.sonha.order.service.database.customer.entity.CustomerEntity;
import com.sonha.order.service.domain.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer customerEntityToCustomer(CustomerEntity customerEntity) {
        return new Customer(new CustomerId(customerEntity.getId()));
    }
}
