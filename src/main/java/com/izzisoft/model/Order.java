package com.izzisoft.model;

public class Order {

    private final Long id;

    private final Product product;

    private final Integer quantity;

    private final OrderType orderType;

    public Order(Builder builder) {
        this.id = builder.id;
        this.product = builder.product;
        this.quantity = builder.quantity;
        this.orderType = builder.orderType;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public static class Builder {

        private Long id;

        private Product product;

        private Integer quantity;

        private OrderType orderType = OrderType.NORMAL;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder product(Product product) {
            this.product = product;
            return this;
        }

        public Builder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder orderType(OrderType orderType) {
            this.orderType = orderType;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
