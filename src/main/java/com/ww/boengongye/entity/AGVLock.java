package com.ww.boengongye.entity;

import lombok.Data;

@Data
public class AGVLock {
    //机器
    public String machineCode;
    //方向
    public String direction;
    //锁定时间
    public String lockTime;
    //x
    public Double x;
    //y
    public Double y;

    //分类
    public String type;


}
