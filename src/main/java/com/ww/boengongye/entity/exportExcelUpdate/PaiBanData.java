package com.ww.boengongye.entity.exportExcelUpdate;

import lombok.Data;

import java.util.List;

@Data
public class PaiBanData {
    private String name;
    private List<InfoData> infoDataList;
}
