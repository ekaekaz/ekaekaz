package com.example;

/**
 * A priced line in a {@link CartPreview}: the SKU, quantity, unit price, and
 * the resulting line total (unit price times quantity).
 */
public final class CartLineItem {

    private final String sku;
    private final int quantity;
    private final int unitPrice;
    private final int lineTotal;

    public CartLineItem(String sku, int quantity, int unitPrice, int lineTotal) {
        this.sku = sku;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal;
    }

    public String getSku() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public int getLineTotal() {
        return lineTotal;
    }
}
