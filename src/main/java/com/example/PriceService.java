package com.example;

import java.util.Objects;

/**
 * Calculates discounted prices by delegating base-price lookups to a
 * {@link PriceClient} (external boundary).
 */
public class PriceService {

    private static final int MIN_DISCOUNT_PERCENT = 0;
    private static final int MAX_DISCOUNT_PERCENT = 100;
    private static final float PERCENT_SCALE = 100f;

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
        if (discountPercent < MIN_DISCOUNT_PERCENT || discountPercent > MAX_DISCOUNT_PERCENT) {
            throw new IllegalArgumentException(
                    "discountPercent must be 0-100, got " + discountPercent);
        }
        int basePrice = client.fetchPrice(sku);
        int discountAmount = Math.round(basePrice * (discountPercent / PERCENT_SCALE));
        return Math.max(0, basePrice - discountAmount);
    }

    /**
     * Boundary interface for fetching prices from an external source.
     */
    public interface PriceClient {
        int fetchPrice(String sku);
    }
}
