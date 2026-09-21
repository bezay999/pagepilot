package dev.pagepilot;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public final class ReadingPlanner {
    public List<ReadingDay> createPlan(
            int totalPages,
            int currentPage,
            LocalDate startDate,
            LocalDate deadline,
            Set<DayOfWeek> daysOff) {

        validate(totalPages, currentPage, startDate, deadline);
        Set<DayOfWeek> safeDaysOff = daysOff == null || daysOff.isEmpty()
                ? EnumSet.noneOf(DayOfWeek.class)
                : EnumSet.copyOf(daysOff);

        List<LocalDate> readingDates = startDate.datesUntil(deadline.plusDays(1))
                .filter(date -> !safeDaysOff.contains(date.getDayOfWeek()))
                .toList();

        int pagesLeft = totalPages - currentPage;
        if (pagesLeft == 0) {
            return List.of();
        }
        if (readingDates.isEmpty()) {
            throw new IllegalArgumentException("There are no available reading days before the deadline.");
        }

        int base = pagesLeft / readingDates.size();
        int extra = pagesLeft % readingDates.size();
        int nextPage = currentPage + 1;
        List<ReadingDay> plan = new ArrayList<>();

        for (int i = 0; i < readingDates.size(); i++) {
            int count = base + (i < extra ? 1 : 0);
            if (count == 0) {
                continue;
            }
            plan.add(new ReadingDay(readingDates.get(i), nextPage, nextPage + count - 1));
            nextPage += count;
        }
        return List.copyOf(plan);
    }

    private void validate(int totalPages, int currentPage, LocalDate startDate, LocalDate deadline) {
        if (totalPages < 1) {
            throw new IllegalArgumentException("Total pages must be positive.");
        }
        if (currentPage < 0 || currentPage > totalPages) {
            throw new IllegalArgumentException("Current page must be between 0 and total pages.");
        }
        if (deadline.isBefore(startDate)) {
            throw new IllegalArgumentException("Deadline cannot be before the start date.");
        }
    }
}
