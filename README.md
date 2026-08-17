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

## Interactive Terminal CLI

The project ships an interactive terminal menu (`com.example.Main`) that
lets you try `Clamp`, `StringUtils`, `TemperatureConverter` and
`PriceService` from the command line.

```bash
# Run directly from source
mvn -q compile exec:java

# Or build a runnable jar and run it
mvn -q package -DskipTests
java -jar target/ekaekaz-1.0.0-SNAPSHOT.jar
```

## Project Structure

```
src/main/java/com/example/
  Clamp.java                 — numeric clamping utility
  PriceService.java          — discount calculator with injected PriceClient
  StringUtils.java           — string manipulation utilities
  TemperatureConverter.java  — temperature conversions with injected RoundingStrategy

src/test/java/com/example/
  ClampTest.java
  PriceServiceTest.java
  StringUtilsTest.java
  TemperatureConverterTest.java
```

## Coverage Policy

- **Line coverage ≥ 95%** (enforced by JaCoCo)
- **Branch coverage ≥ 85%** (enforced by JaCoCo)

Build fails if thresholds are not met.
