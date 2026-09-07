package com.example;

import java.util.Objects;

/**
 * A single line in a shopping cart: a product SKU and the quantity requested.
 */
public final class CartItem {

    private final String sku;
    private final int quantity;

    public CartItem(String sku, int quantity) {
        this.sku = Objects.requireNonNull(sku, "sku must not be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be > 0, got " + quantity);
        }
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }
}
