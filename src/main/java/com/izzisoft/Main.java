package com.izzisoft;

import com.izzisoft.model.Inventory;
import com.izzisoft.model.Order;
import com.izzisoft.model.OrderType;
import com.izzisoft.model.Product;
import com.izzisoft.service.OrderService;
import com.izzisoft.service.OrderWorker;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        BlockingQueue<Order> orders = new LinkedBlockingQueue<>();
        OrderService orderService = new OrderService(orders);


        Product product1 = new Product.Builder()
                .id(1L)
                .name("Produkt1")
                .price(BigDecimal.valueOf(12.34))
                .build();
        Product product2 = new Product.Builder()
                .id(2L)
                .name("Produkt2")
                .price(BigDecimal.valueOf(12.34))
                .build();
        Product product3 = new Product.Builder()
                .id(3L)
                .name("Produkt3")
                .price(BigDecimal.valueOf(12.34))
                .build();

        Order order1 = new Order.Builder()
                .id(1L)
                .product(product1)
                .quantity(3)
                .build();

        Order order2 = new Order.Builder()
                .id(2L)
                .product(product1)
                .quantity(3)
                .build();

        Order order3 = new Order.Builder()
                .id(3L)
                .product(product3)
                .quantity(12)
                .build();

        Order order4 = new Order.Builder()
                .id(2L)
                .product(product3)
                .quantity(2)
                .build();

        Order orderStop = new Order.Builder()
                .orderType(OrderType.STOP)
                .build();

        orderService.addOrder(order1);
        orderService.addOrder(order2);
        orderService.addOrder(order3);
        orderService.addOrder(order4);

        int workersCount = 4;

        for (int i = 0; i < workersCount; i++) {
            orderService.addOrder(orderStop);
        }

        Map<Long, Integer> stock = new HashMap<>();
        stock.put(product1.getId(), 10);
        stock.put(product2.getId(), 5);
        stock.put(product3.getId(), 2);

        System.out.println("Initial stock: " + stock);

        Inventory inventory = new Inventory(stock);

        long start = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(workersCount);

        for (int i = 0; i < workersCount; i++) {
            executorService.execute(new OrderWorker(orderService, inventory));
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        long end = System.currentTimeMillis();

        System.out.println("Time: " + (end - start) + " ms");

        System.out.println(inventory.getStock());
    }
}