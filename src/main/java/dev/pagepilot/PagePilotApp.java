package dev.pagepilot;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

public final class PagePilotApp {
    private PagePilotApp() {
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("PagePilot — your reading route to the finish line\n");
            int totalPages = readInt(scanner, "Total pages: ");
            int currentPage = readInt(scanner, "Current page (0 if not started): ");
            LocalDate deadline = readDate(scanner, "Deadline (YYYY-MM-DD): ");
            Set<DayOfWeek> daysOff = readDaysOff(scanner);

            var plan = new ReadingPlanner().createPlan(
                    totalPages, currentPage, LocalDate.now(), deadline, daysOff);

            if (plan.isEmpty()) {
                System.out.println("\nYou have already finished the book. Nice work!");
                return;
            }

            System.out.printf("%n%-12s %-15s %s%n", "DATE", "PAGES", "DAILY LOAD");
            System.out.println("------------------------------------------");
            plan.forEach(day -> System.out.printf(
                    "%-12s %4d–%-9d %d pages%n",
                    day.date(), day.fromPage(), day.toPage(), day.pageCount()));
            System.out.printf("%n%d reading days · %d pages remaining%n",
                    plan.size(), totalPages - currentPage);
        } catch (IllegalArgumentException | DateTimeParseException exception) {
            System.err.println("Could not create a plan: " + exception.getMessage());
            System.exit(1);
        }
    }

    private static int readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String value = scanner.nextLine().trim();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Expected a whole number: " + value);
        }
    }

    private static LocalDate readDate(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return LocalDate.parse(scanner.nextLine().trim());
    }

    private static Set<DayOfWeek> readDaysOff(Scanner scanner) {
        System.out.print("Days off, comma-separated (e.g. SATURDAY,SUNDAY), or Enter: ");
        String raw = scanner.nextLine().trim();
        Set<DayOfWeek> result = EnumSet.noneOf(DayOfWeek.class);
        if (raw.isEmpty()) {
            return result;
        }
        for (String token : raw.split(",")) {
            result.add(DayOfWeek.valueOf(token.trim().toUpperCase(Locale.ROOT)));
        }
        return result;
    }
}

