package com.ww.boengongye.utils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class DateUtils {
    // 拆分时间段为完整周
    public static List<LocalDate[]> splitWeeks(LocalDate start, LocalDate end) {
        List<LocalDate[]> weeks = new ArrayList<>();
        LocalDate current = start;

        while (!current.isAfter(end)) {
            LocalDate weekEnd = current.plusDays(6);
            if (weekEnd.isAfter(end)) {
                weekEnd = end;
            }
            weeks.add(new LocalDate[]{current, weekEnd});
            current = weekEnd.plusDays(1);
        }
        return weeks;
    }

    // 拆分时间段为完整月
    public static List<YearMonth> splitMonths(LocalDate start, LocalDate end) {
        List<YearMonth> months = new ArrayList<>();
        YearMonth current = YearMonth.from(start);
        YearMonth endMonth = YearMonth.from(end);

        while (!current.isAfter(endMonth)) {
            months.add(current);
            current = current.plusMonths(1);
        }
        return months;
    }
}
