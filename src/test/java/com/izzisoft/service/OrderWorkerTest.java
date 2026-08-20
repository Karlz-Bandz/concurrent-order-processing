package com.izzisoft.service;

import com.izzisoft.model.Inventory;
import com.izzisoft.model.Order;
import com.izzisoft.model.OrderStatistics;
import com.izzisoft.model.OrderType;
import com.izzisoft.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderWorkerTest {

    @Test
    public void shouldSuccessfullyProcessOrder() throws InterruptedException {

        BlockingQueue<Order> orders = new LinkedBlockingQueue<>();
        OrderService orderService = new OrderService(orders);

        Product product1 = new Product.Builder()
                .id(1L)
                .name("Test1")
                .price(BigDecimal.valueOf(11))
                .build();

        Product product2 = new Product.Builder()
                .id(2L)
                .name("Test2")
                .price(BigDecimal.valueOf(11))
                .build();

        Order order1 = new Order.Builder()
                .id(1L)
                .product(product1)
                .quantity(200)
                .build();

        Order order2 = new Order.Builder()
                .id(2L)
                .product(product2)
                .quantity(130)
                .build();

        Order stopOrder = new Order.Builder()
                .orderType(OrderType.STOP)
                .build();


        orderService.addOrder(order1);
        orderService.addOrder(order2);
        orderService.addOrder(stopOrder);

        Map<Long, Integer> stock = new HashMap<>();
        stock.put(1L, 234);
        stock.put(2L, 123);

        Inventory inventory = new Inventory(stock);

        OrderStatistics orderStatistics = new OrderStatistics();

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.execute(new OrderWorker(orderService, inventory, orderStatistics));
        executorService.execute(new OrderWorker(orderService, inventory, orderStatistics));

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        assertEquals(2, orderStatistics.getProcessedOrders().get());
        assertEquals(1, orderStatistics.getSuccessfulReservations().get());
        assertEquals(1, orderStatistics.getFailedReservations().get());

        assertEquals(34, inventory.getStock().get(1L));
        assertEquals(123, inventory.getStock().get(2L));
    }
}
