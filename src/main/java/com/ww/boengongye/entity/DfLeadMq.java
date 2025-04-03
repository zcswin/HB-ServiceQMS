package com.ww.boengongye.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DfLeadMq {

    @SerializedName("CheckTime")
    private String checkTime;

    @SerializedName("SN")
    private String sn;

    @SerializedName("MachineCode")
    private String machineCode;

    @SerializedName("DetnCH")
    private String detnCH;

    @SerializedName("CheckDevCode")
    private String checkDevCode;

    @SerializedName("CheckResult")
    private String result;

    @SerializedName("CheckType")
    private String checkType;

    @SerializedName("CheckItemInfos")
    private List<DfLeadCheckItemInfos> checkItemInfos;

}
