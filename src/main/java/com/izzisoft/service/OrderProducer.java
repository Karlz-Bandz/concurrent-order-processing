package com.izzisoft.service;

import com.izzisoft.model.Order;
import com.izzisoft.model.OrderType;
import com.izzisoft.model.Product;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class OrderProducer implements Runnable {

    private final OrderService orderService;

    private final List<Product> products;

    private final int workerNumber;

    public OrderProducer(OrderService orderService, List<Product> products, int workersCount) {
        this.orderService = orderService;
        this.products = products;
        this.workerNumber = workersCount;
    }

    @Override
    public void run() {

        for (int i = 0; i < 10000; i++) {
            Order order = new Order.Builder()
                    .id(ThreadLocalRandom.current().nextLong(200000))
                    .product(products.get(ThreadLocalRandom.current().nextInt(products.size())))
                    .quantity(ThreadLocalRandom.current().nextInt(1, 50))
                    .build();

            try {
                orderService.addOrder(order);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        Order orderStop = new Order.Builder()
                .orderType(OrderType.STOP)
                .build();

        for (int i = 0; i < workerNumber; i++) {
            try {
                orderService.addOrder(orderStop);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
