package com.example;

import java.util.List;

/**
 * A read-only preview of a shopping cart: priced line items, the subtotal,
 * and the effect of an optional promo code on the final total.
 */
public final class CartPreview {

    private final List<CartLineItem> lineItems;
    private final int subtotal;
    private final String appliedPromoCode;
    private final int discountPercent;
    private final int discountAmount;
    private final int total;

    public CartPreview(
            List<CartLineItem> lineItems,
            int subtotal,
            String appliedPromoCode,
            int discountPercent,
            int discountAmount,
            int total) {
        this.lineItems = lineItems;
        this.subtotal = subtotal;
        this.appliedPromoCode = appliedPromoCode;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.total = total;
    }

    public List<CartLineItem> getLineItems() {
        return lineItems;
    }

    public int getSubtotal() {
        return subtotal;
    }

    /**
     * The promo code that was actually applied, or {@code null} if none was
     * supplied or the supplied code was invalid.
     */
    public String getAppliedPromoCode() {
        return appliedPromoCode;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public int getTotal() {
        return total;
    }
}
