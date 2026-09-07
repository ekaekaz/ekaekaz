package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartItemTest {

    @Test
    @DisplayName("stores sku and quantity")
    void storesSkuAndQuantity() {
        CartItem item = new CartItem("ABC", 3);

        assertThat(item.getSku()).isEqualTo("ABC");
        assertThat(item.getQuantity()).isEqualTo(3);
    }

    @Test
    @DisplayName("throws on null sku")
    void nullSkuThrows() {
        assertThatThrownBy(() -> new CartItem(null, 1))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("sku must not be null");
    }

    @Test
    @DisplayName("throws on zero quantity")
    void zeroQuantityThrows() {
        assertThatThrownBy(() -> new CartItem("ABC", 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("quantity must be > 0");
    }

    @Test
    @DisplayName("throws on negative quantity")
    void negativeQuantityThrows() {
        assertThatThrownBy(() -> new CartItem("ABC", -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("quantity must be > 0");
    }
}
