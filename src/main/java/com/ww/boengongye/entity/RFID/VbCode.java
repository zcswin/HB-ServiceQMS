package com.ww.boengongye.entity.RFID;

import lombok.Data;

import java.util.List;
@Data
public class VbCode {
    public String rfid;
    public List<String> vbCodeList;
    public Integer ngCount;
    public Integer nowCount;
}
