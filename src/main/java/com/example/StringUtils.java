package com.example;

/**
 * Common string manipulation utilities.
 */
public final class StringUtils {

    private StringUtils() {
        // utility class
    }

    /**
     * Reverses the given string.
     *
     * @param input the string to reverse (may be null)
     * @return reversed string, or null if input is null
     */
    public static String reverse(String input) {
        if (input == null) {
            return null;
        }
        return new StringBuilder(input).reverse().toString();
    }

    /**
     * Checks whether the given string is a palindrome (case-insensitive).
     *
     * @param input the string to check (may be null)
     * @return true if input is a palindrome, false otherwise
     */
    public static boolean isPalindrome(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        String lower = input.toLowerCase();
        int left = 0;
        int right = lower.length() - 1;
        while (left < right) {
            if (lower.charAt(left) != lower.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    /**
     * Truncates the string to the given max length, appending "..." if truncated.
     *
     * @param input the string to truncate (may be null)
     * @param maxLength maximum length (must be >= 3 if truncation occurs)
     * @return truncated string, or null if input is null
     * @throws IllegalArgumentException if maxLength &lt; 0
     */
    public static String truncate(String input, int maxLength) {
        if (maxLength < 0) {
            throw new IllegalArgumentException("maxLength must be >= 0, got " + maxLength);
        }
        if (input == null) {
            return null;
        }
        if (input.length() <= maxLength) {
            return input;
        }
        if (maxLength < 3) {
            return input.substring(0, maxLength);
        }
        return input.substring(0, maxLength - 3) + "...";
    }

    /**
     * Capitalizes the first character of the given string, leaving the rest unchanged.
     *
     * @param input the string to capitalize (may be null)
     * @return capitalized string, or the input unchanged if it is null or empty
     */
    public static String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }
}
