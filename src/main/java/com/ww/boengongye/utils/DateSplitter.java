package com.ww.boengongye.utils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/**
 * 时间分割工具类（根据图片中表格的日期格式优化）
 */
public class DateSplitter {

    // 日期格式器（与图片中的"2024-03-03~2024-03-09（周）"格式匹配）
    private static final DateTimeFormatter DATE_RANGE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter WEEK_FORMATTER = DateTimeFormatter.ofPattern("（W周）");

    public static List<TimePeriod> splitPeriod(LocalDateTime start, LocalDateTime end, String period) {
        List<TimePeriod> periods = new ArrayList<>();
        switch (period.toLowerCase()) {
            case "day":
                handleDailyData(periods, start, end);
                break;
            case "week":
                handleWeeklyData(periods, start, end);
                break;
            case "month":
                handleMonthlyData(periods, start, end);
                break;
            case "year":
                handleYearlyData(periods, start, end);
                break;
        }
        return periods;
    }

    // ------------------------- 日数据处理 -------------------------
    private static void handleDailyData(List<TimePeriod> periods, LocalDateTime start, LocalDateTime end) {
        for (LocalDateTime date = start; !date.isAfter(end); date = date.plusDays(1)) {
            addPeriod(periods, date, date, "DETAIL", date.format(DATE_RANGE_FORMATTER) + "（日）");
        }
    }

    // ------------------------- 周数据处理 -------------------------
    private static void handleWeeklyData(List<TimePeriod> periods, LocalDateTime start, LocalDateTime end) {
        LocalDateTime current = start.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        while (!current.isAfter(end)) {
            LocalDateTime weekStart = current;
            LocalDateTime weekEnd = weekStart.plusDays(6);
            if (weekEnd.isAfter(end)) weekEnd = end;

            // 添加每日明细
            for (LocalDateTime day = weekStart; !day.isAfter(weekEnd); day = day.plusDays(1)) {
                addPeriod(periods, day, day, "DETAIL", day.format(DATE_RANGE_FORMATTER) + "（日）");
            }

            // 添加周汇总（与图片中的周格式匹配）
            String weekLabel = weekStart.format(DATE_RANGE_FORMATTER) + "~" + weekEnd.format(DATE_RANGE_FORMATTER) + "（周）";
            addPeriod(periods, weekStart, weekEnd, "SUMMARY", weekLabel);

            current = weekEnd.plusDays(1);
        }
    }

    // ------------------------- 月数据处理 -------------------------
    private static void handleMonthlyData(List<TimePeriod> periods, LocalDateTime start, LocalDateTime end) {
        LocalDateTime current = start.withDayOfMonth(1);
        while (!current.isAfter(end)) {
            LocalDateTime monthStart = current;
            LocalDateTime monthEnd = monthStart.with(TemporalAdjusters.lastDayOfMonth());
            if (monthEnd.isAfter(end)) monthEnd = end;

            // 添加周级明细
            LocalDateTime weekStart = monthStart;
            while (!weekStart.isAfter(monthEnd)) {
                LocalDateTime weekEnd = weekStart.plusDays(6);
                if (weekEnd.isAfter(monthEnd)) weekEnd = monthEnd;

                // 添加每周汇总
                String weekLabel = weekStart.format(DATE_RANGE_FORMATTER) + "~" + weekEnd.format(DATE_RANGE_FORMATTER) + "（周）";
                addPeriod(periods, weekStart, weekEnd, "DETAIL", weekLabel);

                weekStart = weekEnd.plusDays(1);
            }

            // 添加月汇总（与图片中的月格式匹配）
            String monthLabel = monthStart.format(DateTimeFormatter.ofPattern("yyyy-MM")) + "（月）";
            addPeriod(periods, monthStart, monthEnd, "SUMMARY", monthLabel);

            current = monthEnd.plusDays(1);
        }
    }

    // ------------------------- 年数据处理 -------------------------
    private static void handleYearlyData(List<TimePeriod> periods, LocalDateTime start, LocalDateTime end) {
        LocalDateTime current = start.withDayOfYear(1);
        while (!current.isAfter(end)) {
            LocalDateTime yearStart = current;
            LocalDateTime yearEnd = yearStart.with(TemporalAdjusters.lastDayOfYear());
            if (yearEnd.isAfter(end)) yearEnd = end;

            // 添加月级明细
            LocalDateTime monthStart = yearStart;
            while (!monthStart.isAfter(yearEnd)) {
                LocalDateTime monthEnd = monthStart.with(TemporalAdjusters.lastDayOfMonth());
                if (monthEnd.isAfter(yearEnd)) monthEnd = yearEnd;

                // 添加每月汇总
                String monthLabel = monthStart.format(DateTimeFormatter.ofPattern("yyyy-MM")) + "（月）";
                addPeriod(periods, monthStart, monthEnd, "DETAIL", monthLabel);

                monthStart = monthEnd.plusDays(1);
            }

            // 添加年汇总（与图片中的年格式匹配）
            String yearLabel = yearStart.format(DateTimeFormatter.ofPattern("yyyy")) + "（年）";
            addPeriod(periods, yearStart, yearEnd, "SUMMARY", yearLabel);

            current = yearEnd.plusDays(1);
        }
    }

    // ------------------------- 工具方法 -------------------------
    private static void addPeriod(List<TimePeriod> periods,
                                  LocalDateTime start,
                                  LocalDateTime end,
                                  String type,
                                  String label) {
        periods.add(new TimePeriod(
                start,
                end.withHour(23).withMinute(59).withSecond(59),
                label,
                "SUMMARY".equals(type)
        ));
    }
}