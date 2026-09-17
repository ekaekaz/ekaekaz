# ekaekaz

Java-проект с юнит-тестами на JUnit 5 и проверкой покрытия кода через JaCoCo.

## Требования

- Java 17+
- Maven 3.6+

## Сборка и тесты

```bash
# Быстрый локальный прогон (только юнит-тесты)
mvn -q -DskipITs test

# Полная сборка с проверкой покрытия (падает, если <95% строк или <85% ветвей)
mvn -q verify

# Открыть отчёт о покрытии
open target/site/jacoco/index.html
```

## Структура проекта

```
src/main/java/com/example/
  Clamp.java                 — утилита ограничения числа в диапазоне
  PriceService.java          — расчёт скидок с внедряемым PriceClient
  StringUtils.java           — утилиты для работы со строками
  TemperatureConverter.java  — конвертация температур с внедряемой RoundingStrategy

src/test/java/com/example/
  ClampTest.java
  PriceServiceTest.java
  StringUtilsTest.java
  TemperatureConverterTest.java
```

## Политика покрытия

- **Покрытие строк ≥ 95%** (проверяется JaCoCo)
- **Покрытие ветвей ≥ 85%** (проверяется JaCoCo)

Сборка падает, если пороги не достигнуты.
