package com.example;

/**
 * Utility for clamping numeric values to a specified range.
 */
public final class Clamp {

    private Clamp() {
        // utility class
    }

    /**
     * Clamps {@code value} to the range [{@code min}, {@code max}].
     *
     * @throws IllegalArgumentException if min &gt; max
     */
    public static int clamp(int value, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min > max");
        }
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Clamps {@code value} to the range [{@code min}, {@code max}] for doubles.
     *
     * @throws IllegalArgumentException if min &gt; max
     */
    public static double clamp(double value, double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("min > max");
        }
        return Math.max(min, Math.min(max, value));
    }
}
