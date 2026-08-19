package com.izzisoft.service;

import com.izzisoft.model.Inventory;
import com.izzisoft.model.Order;
import com.izzisoft.model.OrderStatistics;
import com.izzisoft.model.OrderType;

public class OrderWorker implements Runnable {

    private final OrderService orderService;

    private final Inventory inventory;

    private final OrderStatistics orderStatistics;

    public OrderWorker(OrderService orderService, Inventory inventory, OrderStatistics orderStatistics) {
        this.orderService = orderService;
        this.inventory = inventory;
        this.orderStatistics = orderStatistics;
    }

    @Override
    public void run() {

        try {
            while (true) {
                Order order = orderService.takeOrder();

                if (OrderType.STOP.equals(order.getOrderType())) {
                    break;
                }

                boolean result = inventory.reserve(order.getProduct().getId(), order.getQuantity());

                orderStatistics.addProcessedOrders();

                if (!result) {
                    orderStatistics.addFailedReservation();
                } else {
                    orderStatistics.addSuccessReservation();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
