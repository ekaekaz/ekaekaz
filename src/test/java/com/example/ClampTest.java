package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ClampTest {

    // --- int clamp ---

    @Test
    @DisplayName("returns value when within range")
    void intWithinRange() {
        assertThat(Clamp.clamp(5, 0, 10)).isEqualTo(5);
    }

    @Test
    @DisplayName("floors at min when value is below range")
    void intBelowRange() {
        assertThat(Clamp.clamp(-1, 0, 10)).isEqualTo(0);
    }

    @Test
    @DisplayName("caps at max when value is above range")
    void intAboveRange() {
        assertThat(Clamp.clamp(11, 0, 10)).isEqualTo(10);
    }

    @Test
    @DisplayName("returns min when value equals min")
    void intAtMin() {
        assertThat(Clamp.clamp(0, 0, 10)).isEqualTo(0);
    }

    @Test
    @DisplayName("returns max when value equals max")
    void intAtMax() {
        assertThat(Clamp.clamp(10, 0, 10)).isEqualTo(10);
    }

    @Test
    @DisplayName("works when min equals max")
    void intMinEqualsMax() {
        assertThat(Clamp.clamp(5, 3, 3)).isEqualTo(3);
    }

    @ParameterizedTest
    @CsvSource({
            "-100, -50, 50, -50",
            "100, -50, 50, 50",
            "0, -50, 50, 0"
    })
    @DisplayName("parameterized int clamp")
    void intParameterized(int value, int min, int max, int expected) {
        assertThat(Clamp.clamp(value, min, max)).isEqualTo(expected);
    }

    @Test
    @DisplayName("throws when min > max for int")
    void intInvalidRangeThrows() {
        assertThatThrownBy(() -> Clamp.clamp(1, 5, 4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("min > max");
    }

    // --- double clamp ---

    @Test
    @DisplayName("returns double value when within range")
    void doubleWithinRange() {
        assertThat(Clamp.clamp(5.5, 0.0, 10.0)).isEqualTo(5.5);
    }

    @Test
    @DisplayName("floors double at min when below range")
    void doubleBelowRange() {
        assertThat(Clamp.clamp(-1.5, 0.0, 10.0)).isEqualTo(0.0);
    }

    @Test
    @DisplayName("caps double at max when above range")
    void doubleAboveRange() {
        assertThat(Clamp.clamp(11.5, 0.0, 10.0)).isEqualTo(10.0);
    }

    @Test
    @DisplayName("throws when min > max for double")
    void doubleInvalidRangeThrows() {
        assertThatThrownBy(() -> Clamp.clamp(1.0, 5.0, 4.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("min > max");
    }

    // --- long clamp ---

    @Test
    @DisplayName("returns long value when within range")
    void longWithinRange() {
        assertThat(Clamp.clamp(5L, 0L, 10L)).isEqualTo(5L);
    }

    @Test
    @DisplayName("floors long at min when below range")
    void longBelowRange() {
        assertThat(Clamp.clamp(-1L, 0L, 10L)).isEqualTo(0L);
    }

    @Test
    @DisplayName("caps long at max when above range")
    void longAboveRange() {
        assertThat(Clamp.clamp(11L, 0L, 10L)).isEqualTo(10L);
    }

    @Test
    @DisplayName("throws when min > max for long")
    void longInvalidRangeThrows() {
        assertThatThrownBy(() -> Clamp.clamp(1L, 5L, 4L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("min > max");
    }
}
