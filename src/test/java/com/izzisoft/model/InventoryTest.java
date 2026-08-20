package com.izzisoft.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InventoryTest {

    @Test
    public void shouldHandleConcurrentReservations() throws InterruptedException {

        Map<Long, Integer> testStock = new HashMap<>();
        testStock.put(1L, 100);

        Inventory inventory = new Inventory(testStock);

        AtomicInteger successfulReservations = new AtomicInteger();

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                boolean result = inventory.reserve(1L, 20);

                if (result) {
                    successfulReservations.incrementAndGet();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        assertEquals(5, successfulReservations.get());
        assertEquals(0, inventory.getStock().get(1L));
    }

    @Test
    public void shouldReserveProductWhenStockIsAvailable() {

        Map<Long, Integer> testStock = new HashMap<>();

        testStock.put(1L, 23);
        testStock.put(2L, 10);

        Inventory inventory = new Inventory(testStock);

        boolean result = inventory.reserve(1L, 10);

        assertTrue(result);
        assertEquals(13, testStock.get(1L));
    }

    @Test
    public void shouldReturnFalseWhenStockIsTooLow() {

        Map<Long, Integer> testStock = new HashMap<>();

        testStock.put(1L, 23);
        testStock.put(2L, 10);

        Inventory inventory = new Inventory(testStock);

        boolean result = inventory.reserve(2L, 11);

        assertFalse(result);
        assertEquals(10, testStock.get(2L));
    }

    @Test
    public void shouldReturnFalseWhenProductNotExists() {

        Map<Long, Integer> testStock = new HashMap<>();

        testStock.put(1L, 23);
        testStock.put(2L, 10);

        Inventory inventory = new Inventory(testStock);

        boolean result = inventory.reserve(7L, 11);

        assertFalse(result);
    }
}
