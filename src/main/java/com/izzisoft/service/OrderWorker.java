package com.izzisoft.service;

import com.izzisoft.model.Inventory;
import com.izzisoft.model.Order;
import com.izzisoft.model.OrderType;

public class OrderWorker implements Runnable {

    private final OrderService orderService;

    private final Inventory inventory;

    public OrderWorker(OrderService orderService, Inventory inventory) {
        this.orderService = orderService;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Order order = orderService.takeOrder();

                if (OrderType.STOP.equals(order.getOrderType())) {
                    System.out.println(Thread.currentThread().getName() + " received stop!");
                    break;
                }

                boolean result = inventory.reserve(order.getProduct().getId(), order.getQuantity());
                System.out.println(
                        Thread.currentThread().getName()
                                + " | Order: " + order.getId()
                                + " | Product: " + order.getProduct().getId()
                                + " | Quantity: " + order.getQuantity()
                                + " | Result: " + result
                );
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
