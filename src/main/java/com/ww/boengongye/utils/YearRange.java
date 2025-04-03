package com.ww.boengongye.utils;

import lombok.Data;

import java.time.LocalDate;

@Data
public class YearRange {
    private LocalDate startDate;
    private LocalDate endDate;

    // 带参构造方法
    public YearRange(LocalDate yearStart, LocalDate yearEnd) {
        this.startDate = yearStart;
        this.endDate = yearEnd;
    }
}
