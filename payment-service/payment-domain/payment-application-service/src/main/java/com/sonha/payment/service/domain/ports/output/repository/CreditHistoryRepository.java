package com.sonha.payment.service.domain.ports.output.repository;

import com.sonha.domain.valueobject.CustomerId;
import com.sonha.payment.service.domain.entity.CreditEntry;
import com.sonha.payment.service.domain.entity.CreditHistory;

import java.util.List;
import java.util.Optional;

public interface CreditHistoryRepository {

    CreditHistory save(CreditHistory creditHistory);
    Optional<List<CreditHistory>> findByCustomerId(CustomerId customerId);
}
