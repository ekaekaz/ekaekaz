package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

class TemperatureConverterTest {

    private TemperatureConverter.RoundingStrategy rounding;
    private TemperatureConverter converter;

    @BeforeEach
    void setUp() {
        rounding = mock(TemperatureConverter.RoundingStrategy.class);
        // identity rounding by default
        when(rounding.round(anyDouble())).thenAnswer(inv -> inv.getArgument(0));
        converter = new TemperatureConverter(rounding);
    }

    @Test
    @DisplayName("0C = 32F")
    void celsiusToFahrenheitFreezing() {
        assertThat(converter.celsiusToFahrenheit(0)).isCloseTo(32.0, within(0.01));
        verify(rounding).round(32.0);
    }

    @Test
    @DisplayName("100C = 212F")
    void celsiusToFahrenheitBoiling() {
        assertThat(converter.celsiusToFahrenheit(100)).isCloseTo(212.0, within(0.01));
    }

    @Test
    @DisplayName("-40C = -40F")
    void celsiusToFahrenheitNeg40() {
        assertThat(converter.celsiusToFahrenheit(-40)).isCloseTo(-40.0, within(0.01));
    }

    @Test
    @DisplayName("32F = 0C")
    void fahrenheitToCelsiusFreezing() {
        assertThat(converter.fahrenheitToCelsius(32)).isCloseTo(0.0, within(0.01));
        verify(rounding).round(0.0);
    }

    @Test
    @DisplayName("212F = 100C")
    void fahrenheitToCelsiusBoiling() {
        assertThat(converter.fahrenheitToCelsius(212)).isCloseTo(100.0, within(0.01));
    }

    @Test
    @DisplayName("0C = 273.15K")
    void celsiusToKelvinFreezing() {
        assertThat(converter.celsiusToKelvin(0)).isCloseTo(273.15, within(0.01));
    }

    @Test
    @DisplayName("-273.15C = 0K")
    void celsiusToKelvinAbsoluteZero() {
        assertThat(converter.celsiusToKelvin(-273.15)).isCloseTo(0.0, within(0.01));
    }

    @Test
    @DisplayName("below absolute zero throws")
    void celsiusToKelvinBelowAbsoluteZero() {
        assertThatThrownBy(() -> converter.celsiusToKelvin(-300))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Below absolute zero");
    }

    @Test
    @DisplayName("0K = -273.15C")
    void kelvinToCelsiusZero() {
        assertThat(converter.kelvinToCelsius(0)).isCloseTo(-273.15, within(0.01));
    }

    @Test
    @DisplayName("273.15K = 0C")
    void kelvinToCelsiusFreezing() {
        assertThat(converter.kelvinToCelsius(273.15)).isCloseTo(0.0, within(0.01));
    }

    @Test
    @DisplayName("negative kelvin throws")
    void kelvinToCelsiusNegative() {
        assertThatThrownBy(() -> converter.kelvinToCelsius(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Kelvin cannot be negative");
    }
}
