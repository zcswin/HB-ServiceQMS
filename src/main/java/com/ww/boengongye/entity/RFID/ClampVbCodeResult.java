package com.ww.boengongye.entity.RFID;

import lombok.Data;

import java.util.List;
@Data
public class ClampVbCodeResult {
    public Integer code;
    public String message;

    public ClampVbCode data;

    public ClampVbCodeResult(Integer code, String message, ClampVbCode data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
