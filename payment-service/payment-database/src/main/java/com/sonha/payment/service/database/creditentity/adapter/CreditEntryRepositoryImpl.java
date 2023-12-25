package com.sonha.payment.service.database.creditentity.adapter;

import com.sonha.domain.valueobject.CustomerId;
import com.sonha.payment.service.database.creditentity.mapper.CreditEntryDatabaseMapper;
import com.sonha.payment.service.database.creditentity.repository.CreditEntryJpaRepository;
import com.sonha.payment.service.domain.entity.CreditEntry;
import com.sonha.payment.service.domain.ports.output.repository.CreditEntryRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CreditEntryRepositoryImpl implements CreditEntryRepository {

    private final CreditEntryJpaRepository creditEntryJpaRepository;
    private final CreditEntryDatabaseMapper creditEntryDatabaseMapper;

    public CreditEntryRepositoryImpl(CreditEntryJpaRepository creditEntryJpaRepository,
                                     CreditEntryDatabaseMapper creditEntryDatabaseMapper) {
        this.creditEntryJpaRepository = creditEntryJpaRepository;
        this.creditEntryDatabaseMapper = creditEntryDatabaseMapper;
    }

    @Override
    public CreditEntry save(CreditEntry creditEntry) {
        return creditEntryDatabaseMapper.creditEntryEntityToCreditEntry(
                creditEntryJpaRepository.save(creditEntryDatabaseMapper.creditEntryToCreditEntryEntity(creditEntry))
        );
    }

    @Override
    public Optional<CreditEntry> findByCustomerId(CustomerId customerId) {
        return creditEntryJpaRepository.findByCustomerId(customerId.getValue())
                .map(creditEntryDatabaseMapper::creditEntryEntityToCreditEntry);
    }
}
