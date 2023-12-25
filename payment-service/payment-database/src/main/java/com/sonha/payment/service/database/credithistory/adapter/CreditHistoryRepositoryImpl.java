package com.sonha.payment.service.database.credithistory.adapter;

import com.sonha.domain.valueobject.CustomerId;
import com.sonha.payment.service.database.credithistory.entity.CreditHistoryEntity;
import com.sonha.payment.service.database.credithistory.mapper.CreditHistoryDatabaseMapper;
import com.sonha.payment.service.database.credithistory.repository.CreditHistoryJpaRepository;
import com.sonha.payment.service.domain.entity.CreditHistory;
import com.sonha.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CreditHistoryRepositoryImpl implements CreditHistoryRepository {

    private final CreditHistoryJpaRepository creditHistoryJpaRepository;
    private final CreditHistoryDatabaseMapper creditHistoryDatabaseMapper;

    public CreditHistoryRepositoryImpl(CreditHistoryJpaRepository creditHistoryJpaRepository,
                                       CreditHistoryDatabaseMapper creditHistoryDatabaseMapper) {
        this.creditHistoryJpaRepository = creditHistoryJpaRepository;
        this.creditHistoryDatabaseMapper = creditHistoryDatabaseMapper;
    }

    @Override
    public CreditHistory save(CreditHistory creditHistory) {
        return creditHistoryDatabaseMapper.creditHistoryEntityToCreditHistory(
                creditHistoryJpaRepository.save(
                        creditHistoryDatabaseMapper.creditHistoryToCreditHistoryEntity(creditHistory)
                )
        );
    }

    @Override
    public Optional<List<CreditHistory>> findByCustomerId(CustomerId customerId) {
        Optional<List<CreditHistoryEntity>> creditHistoryList = creditHistoryJpaRepository.findByCustomerId(
                customerId.getValue()
        );

        return creditHistoryList.map(creditHistoryEntities ->
                creditHistoryEntities.stream()
                        .map(creditHistoryDatabaseMapper::creditHistoryEntityToCreditHistory)
                        .collect(Collectors.toList()));
    }
}
