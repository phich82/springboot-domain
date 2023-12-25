package com.sonha.payment.service.domain.entity;

import com.sonha.domain.entity.AggregateRoot;
import com.sonha.domain.valueobject.CustomerId;
import com.sonha.domain.valueobject.Money;
import com.sonha.payment.service.domain.valueobject.CreditEntryId;

public class CreditEntry extends AggregateRoot<CreditEntryId> {

    private final CustomerId customerId;
    private Money totalCreditAmount;

    public void addCreditAmount(Money money) {
        totalCreditAmount = totalCreditAmount.add(money);
    }

    public void subtractCreditAmount(Money money) {
        totalCreditAmount = totalCreditAmount.subtract(money);
    }

    private CreditEntry(Builder builder) {
        setId(builder.creditEntryId);
        customerId = builder.customerId;
        totalCreditAmount = builder.totalCreditAmount;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getTotalCreditAmount() {
        return totalCreditAmount;
    }

    public static Builder builder() {
        return Builder.builder();
    }

    public static final class Builder {
        private CreditEntryId creditEntryId;
        private CustomerId customerId;
        private Money totalCreditAmount;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder creditEntryId(CreditEntryId val) {
            creditEntryId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder totalCreditAmount(Money val) {
            totalCreditAmount = val;
            return this;
        }

        public CreditEntry build() {
            return new CreditEntry(this);
        }
    }
}
