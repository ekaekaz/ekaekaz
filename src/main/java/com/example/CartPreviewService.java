package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Builds a {@link CartPreview} for a set of {@link CartItem}s, pricing each
 * line via an injected {@link PriceService.PriceClient} and optionally
 * applying a promo code resolved via an injected {@link PromoCodeClient}
 * (both external boundaries).
 */
public class CartPreviewService {

    private final PriceService.PriceClient priceClient;
    private final PromoCodeClient promoCodeClient;

    public CartPreviewService(PriceService.PriceClient priceClient, PromoCodeClient promoCodeClient) {
        this.priceClient = Objects.requireNonNull(priceClient, "priceClient must not be null");
        this.promoCodeClient = Objects.requireNonNull(promoCodeClient, "promoCodeClient must not be null");
    }

    /**
     * Prices every item, then applies the promo code (if present and valid)
     * to the subtotal.
     *
     * @param items non-null, non-empty list of cart items
     * @param promoCode a promo code to apply, or {@code null}/blank for none
     * @return a preview with line items, subtotal, discount, and total
     * @throws IllegalArgumentException if items is empty
     */
    public CartPreview preview(List<CartItem> items, String promoCode) {
        Objects.requireNonNull(items, "items must not be null");
        if (items.isEmpty()) {
            throw new IllegalArgumentException("items must not be empty");
        }

        List<CartLineItem> lineItems = new ArrayList<>();
        int subtotal = 0;
        for (CartItem item : items) {
            int unitPrice = priceClient.fetchPrice(item.getSku());
            int lineTotal = unitPrice * item.getQuantity();
            lineItems.add(new CartLineItem(item.getSku(), item.getQuantity(), unitPrice, lineTotal));
            subtotal += lineTotal;
        }

        String appliedPromoCode = null;
        int discountPercent = 0;
        if (promoCode != null && !promoCode.isBlank()) {
            Optional<Integer> resolved = promoCodeClient.fetchDiscountPercent(promoCode);
            if (resolved.isPresent()) {
                discountPercent = resolved.get();
                appliedPromoCode = promoCode;
            }
        }

        int discountAmount = Math.round(subtotal * (discountPercent / 100f));
        int total = Math.max(0, subtotal - discountAmount);

        return new CartPreview(lineItems, subtotal, appliedPromoCode, discountPercent, discountAmount, total);
    }

    /**
     * Boundary interface for resolving a promo code to a discount percentage.
     */
    public interface PromoCodeClient {
        /**
         * @param code the promo code to resolve
         * @return the discount percentage (0-100) if the code is valid, or
         *     an empty Optional if the code is unknown/expired
         */
        Optional<Integer> fetchDiscountPercent(String code);
    }
}
