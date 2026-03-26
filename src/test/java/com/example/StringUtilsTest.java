package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringUtilsTest {

    @Nested
    @DisplayName("reverse()")
    class ReverseTest {

        @Test
        @DisplayName("reverses a normal string")
        void normal() {
            assertThat(StringUtils.reverse("hello")).isEqualTo("olleh");
        }

        @Test
        @DisplayName("returns null for null input")
        void nullInput() {
            assertThat(StringUtils.reverse(null)).isNull();
        }

        @Test
        @DisplayName("returns empty for empty input")
        void emptyInput() {
            assertThat(StringUtils.reverse("")).isEmpty();
        }

        @Test
        @DisplayName("single character unchanged")
        void singleChar() {
            assertThat(StringUtils.reverse("a")).isEqualTo("a");
        }

        @Test
        @DisplayName("handles unicode characters")
        void unicode() {
            assertThat(StringUtils.reverse("\u00e9\u00e0")).isEqualTo("\u00e0\u00e9");
        }
    }

    @Nested
    @DisplayName("isPalindrome()")
    class IsPalindromeTest {

        @ParameterizedTest
        @ValueSource(strings = {"aba", "racecar", "Madam", "A"})
        @DisplayName("returns true for palindromes")
        void palindromes(String input) {
            assertThat(StringUtils.isPalindrome(input)).isTrue();
        }

        @ParameterizedTest
        @ValueSource(strings = {"hello", "ab"})
        @DisplayName("returns false for non-palindromes")
        void nonPalindromes(String input) {
            assertThat(StringUtils.isPalindrome(input)).isFalse();
        }

        @Test
        @DisplayName("returns false for null")
        void nullInput() {
            assertThat(StringUtils.isPalindrome(null)).isFalse();
        }

        @Test
        @DisplayName("returns false for empty string")
        void emptyInput() {
            assertThat(StringUtils.isPalindrome("")).isFalse();
        }

        @Test
        @DisplayName("even-length palindrome")
        void evenLength() {
            assertThat(StringUtils.isPalindrome("abba")).isTrue();
        }
    }

    @Nested
    @DisplayName("truncate()")
    class TruncateTest {

        @Test
        @DisplayName("returns input when shorter than maxLength")
        void noTruncation() {
            assertThat(StringUtils.truncate("hi", 10)).isEqualTo("hi");
        }

        @Test
        @DisplayName("truncates with ellipsis when longer than maxLength >= 3")
        void truncatesWithEllipsis() {
            assertThat(StringUtils.truncate("hello world", 8)).isEqualTo("hello...");
        }

        @Test
        @DisplayName("truncates without ellipsis when maxLength < 3")
        void truncatesShort() {
            assertThat(StringUtils.truncate("hello", 2)).isEqualTo("he");
        }

        @Test
        @DisplayName("returns null for null input")
        void nullInput() {
            assertThat(StringUtils.truncate(null, 5)).isNull();
        }

        @Test
        @DisplayName("throws on negative maxLength")
        void negativeMaxLength() {
            assertThatThrownBy(() -> StringUtils.truncate("x", -1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("maxLength must be >= 0");
        }

        @Test
        @DisplayName("maxLength 0 returns empty")
        void zeroMaxLength() {
            assertThat(StringUtils.truncate("hello", 0)).isEmpty();
        }

        @Test
        @DisplayName("exact length returns unchanged")
        void exactLength() {
            assertThat(StringUtils.truncate("abc", 3)).isEqualTo("abc");
        }

        @Test
        @DisplayName("maxLength 3 with longer string returns ellipsis only")
        void maxLengthThree() {
            assertThat(StringUtils.truncate("hello", 3)).isEqualTo("...");
        }
    }
}
