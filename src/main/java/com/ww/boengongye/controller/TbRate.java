package com.ww.boengongye.controller;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TbRate {
    private String name;
    private String date;
    private String banbie;
    private double passTarget;
    private String dayOrNight;
    private double rate;
    private Integer ngNum;
    private Integer allNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getPassTarget() {
        return passTarget;
    }

    public void setPassTarget(double passTarget) {
        this.passTarget = passTarget;
    }

    public String getDayOrNight() {
        return dayOrNight;
    }

    public void setDayOrNight(String dayOrNight) {
        this.dayOrNight = dayOrNight;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Integer getNgNum() {
        return ngNum;
    }

    public void setNgNum(Integer ngNum) {
        this.ngNum = ngNum;
    }

    public Integer getAllNum() {
        return allNum;
    }

    public void setAllNum(Integer allNum) {
        this.allNum = allNum;
    }
}
