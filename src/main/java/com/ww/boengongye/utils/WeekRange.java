package com.ww.boengongye.utils;

import java.time.LocalDate;

// WeekRange 类
public class WeekRange {
    private final LocalDate startDate;
    private final LocalDate endDate;

    public WeekRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
}