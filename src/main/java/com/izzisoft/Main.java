package com.izzisoft;

import com.izzisoft.model.Inventory;
import com.izzisoft.model.Order;
import com.izzisoft.model.OrderStatistics;
import com.izzisoft.model.Product;
import com.izzisoft.service.OrderProducer;
import com.izzisoft.service.OrderService;
import com.izzisoft.service.OrderWorker;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
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

        int workersCount = 1;
        OrderStatistics orderStatistics = new OrderStatistics();

        List<Product> products = List.of(product1, product2, product3);

        OrderProducer orderProducer = new OrderProducer(orderService, products, workersCount);

        Map<Long, Integer> stock = new HashMap<>();
        stock.put(product1.getId(), 1272);
        stock.put(product2.getId(), 9934);
        stock.put(product3.getId(), 7913);

        System.out.println("Initial inventory: " + stock);

        Inventory inventory = new Inventory(stock);

        long start = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(workersCount);

        executorService.execute(orderProducer);

        for (int i = 0; i < workersCount; i++) {
            executorService.execute(new OrderWorker(orderService, inventory, orderStatistics));
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        long end = System.currentTimeMillis();

        System.out.println("---LOG---");
        System.out.println("Time: " + (end - start) + " ms");
        System.out.println("Processed orders: " + orderStatistics.getProcessedOrders());
        System.out.println("Successes orders: " + orderStatistics.getSuccessfulReservations());
        System.out.println("Failed orders: " + orderStatistics.getFailedReservations());
        System.out.println("Current inventory: " + inventory.getStock());
    }
}