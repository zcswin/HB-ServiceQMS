package com.ww.boengongye.entity.exportExcelUpdate;

import lombok.Data;

import java.util.List;

@Data
public class Banbie {
    private String name;
    private String lable;
    private List<HzData> JTDatas;
    private List<DayData> dayDatas;
}
