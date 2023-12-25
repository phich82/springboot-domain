package com.sonha.order.service.domain.entity;

import com.sonha.domain.entity.BaseEntity;
import com.sonha.domain.valueobject.Money;
import com.sonha.domain.valueobject.OrderId;
import com.sonha.order.service.domain.valueobject.OrderItemId;


public class OrderItem extends BaseEntity<OrderItemId> {
    private OrderItemId orderItemId;
    private OrderId orderId;
    private final Product product;
    private final int quanlity;
    private final Money price;
    private final Money subTotal;

    public void initializeOrderItem(OrderId orderId, OrderItemId orderItemId) {
        this.orderId = orderId;
        super.setId(orderItemId);
    }

    boolean isPriceValid() {
        return price.isGreaterThanZero()
                && price.equals(product.getPrice())
                && price.multiply(quanlity).equals(subTotal);
    }

    private OrderItem(Builder builder) {
        super.setId(builder.orderItemId);
        product = builder.product;
        quanlity = builder.quanlity;
        price = builder.price;
        subTotal = builder.subTotal;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuanlity() {
        return quanlity;
    }

    public Money getPrice() {
        return price;
    }

    public Money getSubTotal() {
        return subTotal;
    }

    public static final class Builder {
        private OrderItemId orderItemId;
        private Product product;
        private int quanlity;
        private Money price;
        private Money subTotal;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder orderItemId(OrderItemId val) {
            orderItemId = val;
            return this;
        }

        public Builder product(Product val) {
            product = val;
            return this;
        }

        public Builder quanlity(int val) {
            quanlity = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder subTotal(Money val) {
            subTotal = val;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }
}
