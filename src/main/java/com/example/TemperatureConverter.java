package com.example;

import java.util.Objects;

/**
 * Converts temperatures between Celsius, Fahrenheit, and Kelvin.
 * Uses constructor-injected {@link RoundingStrategy} for output precision.
 */
public class TemperatureConverter {

    private static final double ABSOLUTE_ZERO_CELSIUS = -273.15;

    private final RoundingStrategy rounding;

    public TemperatureConverter(RoundingStrategy rounding) {
        this.rounding = Objects.requireNonNull(rounding, "rounding must not be null");
    }

    public double celsiusToFahrenheit(double celsius) {
        return rounding.round(celsius * 9.0 / 5.0 + 32);
    }

    public double fahrenheitToCelsius(double fahrenheit) {
        return rounding.round((fahrenheit - 32) * 5.0 / 9.0);
    }

    public double celsiusToKelvin(double celsius) {
        if (celsius < ABSOLUTE_ZERO_CELSIUS) {
            throw new IllegalArgumentException("Below absolute zero");
        }
        return rounding.round(celsius - ABSOLUTE_ZERO_CELSIUS);
    }

    public double kelvinToCelsius(double kelvin) {
        if (kelvin < 0) {
            throw new IllegalArgumentException("Kelvin cannot be negative");
        }
        return rounding.round(kelvin + ABSOLUTE_ZERO_CELSIUS);
    }

    /**
     * Strategy interface for rounding output values (boundary for mocking).
     */
    public interface RoundingStrategy {
        double round(double value);
    }
}
