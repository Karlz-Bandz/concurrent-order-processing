package com.izzisoft.service;

import com.izzisoft.model.Order;
import com.izzisoft.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderProducerTest {

    @Test
    public void shouldProduce100002rders() {

        BlockingQueue<Order> orders = new LinkedBlockingQueue<>();
        OrderService orderService = new OrderService(orders);

        Product product1 = new Product.Builder()
                .id(1L)
                .name("Test1")
                .price(BigDecimal.TEN)
                .build();

        Product product2 = new Product.Builder()
                .id(2L)
                .name("Test2")
                .price(BigDecimal.TEN)
                .build();

        List<Product> products = List.of(product1, product2);

        OrderProducer orderProducer = new OrderProducer(orderService, products, 2);

        orderProducer.run();

        assertEquals(10002, orders.size());
    }
}
