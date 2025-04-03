package com.ww.boengongye.entity.RFID;

import lombok.Data;

import java.util.List;

@Data
public class ClampVbCode {
    public String rfid;
    public List<String> vbList;
    public String processTime;
}
