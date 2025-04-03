package com.ww.boengongye.utils;

import lombok.Data;

import java.time.LocalDateTime;

// 时间周期对象
@Data
class TimePeriod {
    private LocalDateTime start;
    private LocalDateTime end;
    private String label; // 如 "2023-WK1" 或 "2023-01"
    private boolean isSummary; // 是否为汇总行

    public TimePeriod(LocalDateTime start, LocalDateTime end, String label, boolean isSummary) {
        this.start = start;
        this.end = end;
        this.label = label;
        this.isSummary = isSummary;
    }
    // Getters
}
