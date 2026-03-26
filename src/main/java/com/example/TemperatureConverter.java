package com.example;

/**
 * Converts temperatures between Celsius, Fahrenheit, and Kelvin.
 * Uses constructor-injected {@link RoundingStrategy} for output precision.
 */
public class TemperatureConverter {

    private final RoundingStrategy rounding;

    public TemperatureConverter(RoundingStrategy rounding) {
        this.rounding = rounding;
    }

    public double celsiusToFahrenheit(double celsius) {
        return rounding.round(celsius * 9.0 / 5.0 + 32);
    }

    public double fahrenheitToCelsius(double fahrenheit) {
        return rounding.round((fahrenheit - 32) * 5.0 / 9.0);
    }

    public double celsiusToKelvin(double celsius) {
        if (celsius < -273.15) {
            throw new IllegalArgumentException("Below absolute zero");
        }
        return rounding.round(celsius + 273.15);
    }

    public double kelvinToCelsius(double kelvin) {
        if (kelvin < 0) {
            throw new IllegalArgumentException("Kelvin cannot be negative");
        }
        return rounding.round(kelvin - 273.15);
    }

    /**
     * Strategy interface for rounding output values (boundary for mocking).
     */
    public interface RoundingStrategy {
        double round(double value);
    }
}
