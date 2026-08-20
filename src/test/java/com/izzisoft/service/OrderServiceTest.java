package com.izzisoft.service;

import com.izzisoft.model.Order;
import org.junit.jupiter.api.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderServiceTest {

    @Test
    public void shouldTakeOrdersInFifoOrder() throws InterruptedException {

        BlockingQueue<Order> orders = new LinkedBlockingQueue<>();
        OrderService orderService = new OrderService(orders);

        Order order1 = new Order.Builder().id(1L).build();
        Order order2 = new Order.Builder().id(2L).build();
        Order order3 = new Order.Builder().id(3L).build();

        orderService.addOrder(order1);
        orderService.addOrder(order2);
        orderService.addOrder(order3);

        assertEquals(order1, orderService.takeOrder());
        assertEquals(order2, orderService.takeOrder());
        assertEquals(order3, orderService.takeOrder());
    }

    @Test
    public void shouldAddAndTakeOrder() throws InterruptedException {

        BlockingQueue<Order> orders = new LinkedBlockingQueue<>();
        OrderService orderService = new OrderService(orders);

        Order order = new Order.Builder()
                .id(1L)
                .build();

        orderService.addOrder(order);

        Order result = orderService.takeOrder();

        assertEquals(order, result);
    }
}
