package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class PriceServiceTest {

    @Test
    @DisplayName("calculates 25% discount and calls client once")
    void calculatesDiscountAndCallsClientOnce() {
        PriceService.PriceClient client = mock(PriceService.PriceClient.class);
        when(client.fetchPrice("ABC")).thenReturn(100);

        PriceService svc = new PriceService(client);
        int result = svc.discountedPrice("ABC", 25);

        assertThat(result).isEqualTo(75);
        verify(client, times(1)).fetchPrice("ABC");
        verifyNoMoreInteractions(client);
    }

    @Test
    @DisplayName("0% discount returns full price")
    void zeroDiscount() {
        PriceService.PriceClient client = mock(PriceService.PriceClient.class);
        when(client.fetchPrice("X")).thenReturn(200);

        PriceService svc = new PriceService(client);
        assertThat(svc.discountedPrice("X", 0)).isEqualTo(200);
    }

    @Test
    @DisplayName("100% discount returns zero")
    void fullDiscount() {
        PriceService.PriceClient client = mock(PriceService.PriceClient.class);
        when(client.fetchPrice("X")).thenReturn(200);

        PriceService svc = new PriceService(client);
        assertThat(svc.discountedPrice("X", 100)).isEqualTo(0);
    }

    @ParameterizedTest
    @CsvSource({
            "50, 10, 45",
            "99, 50, 49",
            "1, 99, 0"
    })
    @DisplayName("parameterized discount scenarios")
    void parameterized(int basePrice, int discountPct, int expected) {
        PriceService.PriceClient client = mock(PriceService.PriceClient.class);
        when(client.fetchPrice("SKU")).thenReturn(basePrice);

        PriceService svc = new PriceService(client);
        assertThat(svc.discountedPrice("SKU", discountPct)).isEqualTo(expected);
    }

    @Test
    @DisplayName("throws on negative discount percent")
    void negativeDiscountThrows() {
        PriceService.PriceClient client = mock(PriceService.PriceClient.class);
        PriceService svc = new PriceService(client);

        assertThatThrownBy(() -> svc.discountedPrice("X", -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("discountPercent must be 0-100");
    }

    @Test
    @DisplayName("throws on discount percent > 100")
    void overHundredDiscountThrows() {
        PriceService.PriceClient client = mock(PriceService.PriceClient.class);
        PriceService svc = new PriceService(client);

        assertThatThrownBy(() -> svc.discountedPrice("X", 101))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("discountPercent must be 0-100");
    }

    @Test
    @DisplayName("null client throws NullPointerException")
    void nullClientThrows() {
        assertThatThrownBy(() -> new PriceService(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("client must not be null");
    }
}
