package com.ww.boengongye.entity.exportExcelUpdate;

import lombok.Data;

import java.util.List;

@Data
public class InfoData {
    private String name;
    private List<DayData> days;
}
