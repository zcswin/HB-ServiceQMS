package com.ww.boengongye.entity.RFID;

import lombok.Data;

import java.util.List;

@Data
public class UnLockClampCode {
    public String status;
    public List<String> vbList;
    public String processTime;
}
