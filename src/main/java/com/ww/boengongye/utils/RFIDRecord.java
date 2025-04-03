package com.ww.boengongye.utils;

import lombok.Data;

@Data
public class RFIDRecord {
    public String vbCode;
    public String rfid;
    public String machineCode;
    public String procedureName;
    public String procedureStepName;
    public String operateTime;
}