package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CartPreviewServiceTest {

    @Test
    @DisplayName("prices multiple items and applies a valid promo code")
    void appliesValidPromoCode() {
        PriceService.PriceClient priceClient = mock(PriceService.PriceClient.class);
        when(priceClient.fetchPrice("ABC")).thenReturn(100);
        when(priceClient.fetchPrice("XYZ")).thenReturn(50);

        CartPreviewService.PromoCodeClient promoCodeClient = mock(CartPreviewService.PromoCodeClient.class);
        when(promoCodeClient.fetchDiscountPercent("SAVE10")).thenReturn(Optional.of(10));

        CartPreviewService service = new CartPreviewService(priceClient, promoCodeClient);
        CartPreview preview = service.preview(
                List.of(new CartItem("ABC", 2), new CartItem("XYZ", 1)), "SAVE10");

        assertThat(preview.getLineItems()).hasSize(2);
        assertThat(preview.getLineItems().get(0).getSku()).isEqualTo("ABC");
        assertThat(preview.getLineItems().get(0).getQuantity()).isEqualTo(2);
        assertThat(preview.getLineItems().get(0).getUnitPrice()).isEqualTo(100);
        assertThat(preview.getLineItems().get(0).getLineTotal()).isEqualTo(200);
        assertThat(preview.getLineItems().get(1).getLineTotal()).isEqualTo(50);

        assertThat(preview.getSubtotal()).isEqualTo(250);
        assertThat(preview.getAppliedPromoCode()).isEqualTo("SAVE10");
        assertThat(preview.getDiscountPercent()).isEqualTo(10);
        assertThat(preview.getDiscountAmount()).isEqualTo(25);
        assertThat(preview.getTotal()).isEqualTo(225);
    }

    @Test
    @DisplayName("ignores an unknown promo code and charges full subtotal")
    void ignoresInvalidPromoCode() {
        PriceService.PriceClient priceClient = mock(PriceService.PriceClient.class);
        when(priceClient.fetchPrice("ABC")).thenReturn(100);

        CartPreviewService.PromoCodeClient promoCodeClient = mock(CartPreviewService.PromoCodeClient.class);
        when(promoCodeClient.fetchDiscountPercent("BOGUS")).thenReturn(Optional.empty());

        CartPreviewService service = new CartPreviewService(priceClient, promoCodeClient);
        CartPreview preview = service.preview(List.of(new CartItem("ABC", 1)), "BOGUS");

        assertThat(preview.getSubtotal()).isEqualTo(100);
        assertThat(preview.getAppliedPromoCode()).isNull();
        assertThat(preview.getDiscountPercent()).isZero();
        assertThat(preview.getDiscountAmount()).isZero();
        assertThat(preview.getTotal()).isEqualTo(100);
    }

    @Test
    @DisplayName("null promo code skips discount lookup entirely")
    void nullPromoCodeSkipsLookup() {
        PriceService.PriceClient priceClient = mock(PriceService.PriceClient.class);
        when(priceClient.fetchPrice("ABC")).thenReturn(100);

        CartPreviewService.PromoCodeClient promoCodeClient = mock(CartPreviewService.PromoCodeClient.class);

        CartPreviewService service = new CartPreviewService(priceClient, promoCodeClient);
        CartPreview preview = service.preview(List.of(new CartItem("ABC", 1)), null);

        assertThat(preview.getAppliedPromoCode()).isNull();
        assertThat(preview.getTotal()).isEqualTo(100);
        verifyNoPromoLookup(promoCodeClient);
    }

    @Test
    @DisplayName("blank promo code skips discount lookup entirely")
    void blankPromoCodeSkipsLookup() {
        PriceService.PriceClient priceClient = mock(PriceService.PriceClient.class);
        when(priceClient.fetchPrice("ABC")).thenReturn(100);

        CartPreviewService.PromoCodeClient promoCodeClient = mock(CartPreviewService.PromoCodeClient.class);

        CartPreviewService service = new CartPreviewService(priceClient, promoCodeClient);
        CartPreview preview = service.preview(List.of(new CartItem("ABC", 1)), "   ");

        assertThat(preview.getAppliedPromoCode()).isNull();
        assertThat(preview.getTotal()).isEqualTo(100);
        verifyNoPromoLookup(promoCodeClient);
    }

    @Test
    @DisplayName("100% discount promo code zeroes out the total")
    void fullDiscountPromoCode() {
        PriceService.PriceClient priceClient = mock(PriceService.PriceClient.class);
        when(priceClient.fetchPrice("ABC")).thenReturn(100);

        CartPreviewService.PromoCodeClient promoCodeClient = mock(CartPreviewService.PromoCodeClient.class);
        when(promoCodeClient.fetchDiscountPercent("FREE")).thenReturn(Optional.of(100));

        CartPreviewService service = new CartPreviewService(priceClient, promoCodeClient);
        CartPreview preview = service.preview(List.of(new CartItem("ABC", 1)), "FREE");

        assertThat(preview.getDiscountAmount()).isEqualTo(100);
        assertThat(preview.getTotal()).isZero();
    }

    @Test
    @DisplayName("throws on empty item list")
    void emptyItemsThrows() {
        CartPreviewService service = new CartPreviewService(
                mock(PriceService.PriceClient.class), mock(CartPreviewService.PromoCodeClient.class));

        assertThatThrownBy(() -> service.preview(List.of(), "ANY"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("items must not be empty");
    }

    @Test
    @DisplayName("throws on null item list")
    void nullItemsThrows() {
        CartPreviewService service = new CartPreviewService(
                mock(PriceService.PriceClient.class), mock(CartPreviewService.PromoCodeClient.class));

        assertThatThrownBy(() -> service.preview(null, "ANY"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("items must not be null");
    }

    @Test
    @DisplayName("throws on null price client")
    void nullPriceClientThrows() {
        assertThatThrownBy(() -> new CartPreviewService(null, mock(CartPreviewService.PromoCodeClient.class)))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("priceClient must not be null");
    }

    @Test
    @DisplayName("throws on null promo code client")
    void nullPromoCodeClientThrows() {
        assertThatThrownBy(() -> new CartPreviewService(mock(PriceService.PriceClient.class), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("promoCodeClient must not be null");
    }

    private static void verifyNoPromoLookup(CartPreviewService.PromoCodeClient promoCodeClient) {
        org.mockito.Mockito.verifyNoInteractions(promoCodeClient);
    }
}
