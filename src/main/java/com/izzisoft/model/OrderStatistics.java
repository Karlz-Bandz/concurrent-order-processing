package com.izzisoft.model;

import java.util.concurrent.atomic.AtomicInteger;

public class OrderStatistics {

    private final AtomicInteger processedOrders = new AtomicInteger();

    private final AtomicInteger successfulReservations = new AtomicInteger();

    private final AtomicInteger failedReservations = new AtomicInteger();

    public void addProcessedOrders() {
        processedOrders.incrementAndGet();
    }

    public void addSuccessReservation() {
        successfulReservations.incrementAndGet();
    }

    public void addFailedReservation() {
        failedReservations.incrementAndGet();
    }

    public AtomicInteger getProcessedOrders() {
        return processedOrders;
    }

    public AtomicInteger getSuccessfulReservations() {
        return successfulReservations;
    }

    public AtomicInteger getFailedReservations() {
        return failedReservations;
    }
}
