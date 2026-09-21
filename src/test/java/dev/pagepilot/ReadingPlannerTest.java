package dev.pagepilot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumSet;
import org.junit.jupiter.api.Test;

class ReadingPlannerTest {
    private final ReadingPlanner planner = new ReadingPlanner();

    @Test
    void distributesEveryRemainingPageWithoutGaps() {
        var plan = planner.createPlan(
                110, 10,
                LocalDate.of(2026, 9, 21),
                LocalDate.of(2026, 9, 24),
                EnumSet.noneOf(DayOfWeek.class));

        assertEquals(4, plan.size());
        assertEquals(11, plan.get(0).fromPage());
        assertEquals(110, plan.get(plan.size() - 1).toPage());
        assertEquals(100, plan.stream().mapToInt(ReadingDay::pageCount).sum());
    }

    @Test
    void skipsSelectedDaysOff() {
        var plan = planner.createPlan(
                40, 0,
                LocalDate.of(2026, 9, 21),
                LocalDate.of(2026, 9, 27),
                EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));

        assertEquals(5, plan.size());
        assertEquals(8, plan.get(0).pageCount());
        assertEquals(DayOfWeek.FRIDAY, plan.get(plan.size() - 1).date().getDayOfWeek());
    }

    @Test
    void rejectsADeadlineBeforeTheStartDate() {
        assertThrows(IllegalArgumentException.class, () -> planner.createPlan(
                100, 0,
                LocalDate.of(2026, 9, 22),
                LocalDate.of(2026, 9, 21),
                EnumSet.noneOf(DayOfWeek.class)));
    }
}
