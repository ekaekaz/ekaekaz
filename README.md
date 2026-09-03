# ekaekaz

Java project with JUnit 5 unit tests and JaCoCo coverage enforcement.

## Prerequisites

- Java 17+
- Maven 3.6+

## Build & Test

```bash
# Fast local test loop (unit tests only)
mvn -q -DskipITs test

# Full build with coverage check (fails if <95% line or <85% branch)
mvn -q verify

# View coverage report
open target/site/jacoco/index.html
```

## Project Structure

```
src/main/java/com/example/
  Clamp.java                 — numeric clamping utility
  PriceService.java          — discount calculator with injected PriceClient
  StringUtils.java           — string manipulation utilities
  TemperatureConverter.java  — temperature conversions with injected RoundingStrategy
  CartItem.java              — a SKU + quantity requested for a cart
  CartLineItem.java          — a priced cart line (unit price, line total)
  CartPreview.java           — line items, subtotal, promo discount, and total
  CartPreviewService.java    — builds a CartPreview, applying an optional promo code

src/test/java/com/example/
  ClampTest.java
  PriceServiceTest.java
  StringUtilsTest.java
  TemperatureConverterTest.java
  CartItemTest.java
  CartLineItemTest.java
  CartPreviewTest.java
  CartPreviewServiceTest.java
```

## Coverage Policy

- **Line coverage ≥ 95%** (enforced by JaCoCo)
- **Branch coverage ≥ 85%** (enforced by JaCoCo)

Build fails if thresholds are not met.
