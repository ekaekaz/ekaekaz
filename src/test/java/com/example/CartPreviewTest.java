package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CartPreviewTest {

    @Test
    @DisplayName("stores all fields")
    void storesAllFields() {
        List<CartLineItem> lineItems = List.of(new CartLineItem("ABC", 1, 100, 100));
        CartPreview preview = new CartPreview(lineItems, 100, "SAVE10", 10, 10, 90);

        assertThat(preview.getLineItems()).isEqualTo(lineItems);
        assertThat(preview.getSubtotal()).isEqualTo(100);
        assertThat(preview.getAppliedPromoCode()).isEqualTo("SAVE10");
        assertThat(preview.getDiscountPercent()).isEqualTo(10);
        assertThat(preview.getDiscountAmount()).isEqualTo(10);
        assertThat(preview.getTotal()).isEqualTo(90);
    }
}
