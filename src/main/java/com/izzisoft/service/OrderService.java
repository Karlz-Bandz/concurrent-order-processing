package com.izzisoft.service;

import com.izzisoft.model.Order;

import java.util.concurrent.BlockingQueue;

public class OrderService {

    private final BlockingQueue<Order> orders;

    public OrderService(BlockingQueue<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) throws InterruptedException {
        orders.put(order);
    }

    public Order takeOrder() throws InterruptedException {
        return orders.take();
    }
}
