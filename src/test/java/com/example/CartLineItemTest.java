package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartLineItemTest {

    @Test
    @DisplayName("stores all fields")
    void storesAllFields() {
        CartLineItem line = new CartLineItem("ABC", 2, 100, 200);

        assertThat(line.getSku()).isEqualTo("ABC");
        assertThat(line.getQuantity()).isEqualTo(2);
        assertThat(line.getUnitPrice()).isEqualTo(100);
        assertThat(line.getLineTotal()).isEqualTo(200);
    }
}
