package com.example;

import java.util.Objects;

/**
 * Calculates discounted prices by delegating base-price lookups to a
 * {@link PriceClient} (external boundary).
 */
public class PriceService {

    private final PriceClient client;

    public PriceService(PriceClient client) {
        this.client = Objects.requireNonNull(client, "client must not be null");
    }

    /**
     * Returns the price after applying a percentage discount.
     *
     * @param sku the product SKU
     * @param discountPercent discount percentage (0-100)
     * @return discounted price, never negative
     * @throws IllegalArgumentException if discountPercent is out of range
     */
    public int discountedPrice(String sku, int discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException(
                    "discountPercent must be 0-100, got " + discountPercent);
        }
        int base = client.fetchPrice(sku);
        int discount = (int) Math.round(base * (discountPercent / 100.0));
        return Math.max(0, base - discount);
    }

    /**
     * Boundary interface for fetching prices from an external source.
     */
    public interface PriceClient {
        int fetchPrice(String sku);
    }
}
