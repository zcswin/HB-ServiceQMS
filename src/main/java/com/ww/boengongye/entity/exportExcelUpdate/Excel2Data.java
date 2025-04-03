package com.ww.boengongye.entity.exportExcelUpdate;

import lombok.Data;

import java.util.List;

@Data
public class Excel2Data {
    private String process;
    private String projectName;
    private List<PaiBanData> bbDataList;
    private List<PaiBanData> ybDataList;
}
