package com.sonha.payment.service.domain.ports.output.repository;

import com.sonha.domain.valueobject.CustomerId;
import com.sonha.payment.service.domain.entity.CreditEntry;

import java.util.Optional;

public interface CreditEntryRepository {

    CreditEntry save(CreditEntry creditEntry);
    Optional<CreditEntry> findByCustomerId(CustomerId customerId);
}
