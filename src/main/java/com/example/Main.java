package com.example;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Interactive terminal menu that exercises the utilities in this project.
 */
public final class Main {

    private static final Map<String, Integer> DEMO_CATALOG = new LinkedHashMap<>();

    static {
        DEMO_CATALOG.put("SKU-001", 1000);
        DEMO_CATALOG.put("SKU-002", 2500);
        DEMO_CATALOG.put("SKU-003", 799);
    }

    private Main() {
    }

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            run(scanner);
        }
    }

    private static void run(Scanner scanner) {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    runClamp(scanner);
                    break;
                case "2":
                    runStringUtils(scanner);
                    break;
                case "3":
                    runTemperatureConverter(scanner);
                    break;
                case "4":
                    runPriceService(scanner);
                    break;
                case "0":
                    running = false;
                    System.out.println("Пока!");
                    break;
                default:
                    System.out.println("Неизвестный пункт меню: " + choice);
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("=== ekaekaz CLI ===");
        System.out.println("1. Clamp — ограничить число диапазоном");
        System.out.println("2. StringUtils — reverse / palindrome / truncate");
        System.out.println("3. TemperatureConverter — конвертация температур");
        System.out.println("4. PriceService — расчёт цены со скидкой");
        System.out.println("0. Выход");
        System.out.print("Выберите пункт: ");
    }

    private static void runClamp(Scanner scanner) {
        Double value = readDouble(scanner, "Значение: ");
        Double min = readDouble(scanner, "Минимум: ");
        Double max = readDouble(scanner, "Максимум: ");
        if (value == null || min == null || max == null) {
            return;
        }
        try {
            System.out.println("Результат: " + Clamp.clamp(value, min, max));
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void runStringUtils(Scanner scanner) {
        System.out.print("Строка: ");
        String input = scanner.nextLine();

        System.out.println("Reverse: " + StringUtils.reverse(input));
        System.out.println("Palindrome: " + StringUtils.isPalindrome(input));

        Integer maxLength = readInt(scanner, "Макс. длина для truncate: ");
        if (maxLength == null) {
            return;
        }
        try {
            System.out.println("Truncate: " + StringUtils.truncate(input, maxLength));
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void runTemperatureConverter(Scanner scanner) {
        TemperatureConverter converter = new TemperatureConverter(
                value -> Math.round(value * 100.0) / 100.0);

        System.out.println("1. Цельсий -> Фаренгейт");
        System.out.println("2. Фаренгейт -> Цельсий");
        System.out.println("3. Цельсий -> Кельвин");
        System.out.println("4. Кельвин -> Цельсий");
        System.out.print("Выберите направление: ");
        String direction = scanner.nextLine().trim();

        Double value = readDouble(scanner, "Значение: ");
        if (value == null) {
            return;
        }

        try {
            switch (direction) {
                case "1":
                    System.out.println("Результат: " + converter.celsiusToFahrenheit(value));
                    break;
                case "2":
                    System.out.println("Результат: " + converter.fahrenheitToCelsius(value));
                    break;
                case "3":
                    System.out.println("Результат: " + converter.celsiusToKelvin(value));
                    break;
                case "4":
                    System.out.println("Результат: " + converter.kelvinToCelsius(value));
                    break;
                default:
                    System.out.println("Неизвестное направление: " + direction);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void runPriceService(Scanner scanner) {
        PriceService service = new PriceService(sku -> {
            Integer price = DEMO_CATALOG.get(sku);
            if (price == null) {
                throw new IllegalArgumentException("Неизвестный SKU: " + sku);
            }
            return price;
        });

        System.out.println("Доступные SKU: " + DEMO_CATALOG);
        System.out.print("SKU: ");
        String sku = scanner.nextLine().trim();

        Integer discount = readInt(scanner, "Скидка, % (0-100): ");
        if (discount == null) {
            return;
        }

        try {
            System.out.println("Цена со скидкой: " + service.discountedPrice(sku, discount));
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static Double readDouble(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine().trim();
        try {
            return Double.parseDouble(line.replace(',', '.'));
        } catch (NumberFormatException e) {
            System.out.println("Некорректное число: " + line);
            return null;
        }
    }

    private static Integer readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine().trim();
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("Некорректное число: " + line);
            return null;
        }
    }
}
