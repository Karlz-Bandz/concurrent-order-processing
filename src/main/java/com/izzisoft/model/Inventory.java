package com.izzisoft.model;

import java.util.Map;

public class Inventory {

    private final Map<Long, Integer> stock;

    public Inventory(Map<Long, Integer> stock) {
        this.stock = stock;
    }

    public synchronized boolean reserve(Long productId, Integer quantity) {

        Integer currentStock = stock.get(productId);

        if (currentStock != null && currentStock >= quantity) {
            stock.put(productId, currentStock - quantity);
            return true;
        }

        return false;
    }

    public Map<Long, Integer> getStock() {
        return stock;
    }
}
