package com.ww.boengongye.entity.RFID;

import lombok.Data;

@Data
public class VbCodeResult {
    public Integer code;
    public String message;
    public VbCode data;

    public VbCodeResult(Integer code, String message, VbCode data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}


