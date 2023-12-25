package com.sonha.order.service.domain.entity;

import com.sonha.domain.entity.AggregateRoot;
import com.sonha.domain.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {

    public Customer() {

    }

    public Customer(CustomerId customerId) {
        super.setId(customerId);
    }
}
