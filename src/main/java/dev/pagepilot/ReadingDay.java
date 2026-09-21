package dev.pagepilot;

import java.time.LocalDate;

public record ReadingDay(LocalDate date, int fromPage, int toPage) {
    public int pageCount() {
        return toPage - fromPage + 1;
    }
}

