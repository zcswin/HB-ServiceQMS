package com.ww.boengongye.utils;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MonthRange {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> weekNumbers; // 新增字段，存储该月包含的周编号
    // 构造方法、Getter/Setter
    public MonthRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}