package com.ww.boengongye.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Rate {
    private String name;
    private String date;
    private Double passTarget;
    private String dayOrNight;
    private String fmac;
    private Double rate;
    private String fseq;
    private Integer ngNum;
    private Integer allNum;
    private double unqualifiedRate;


    public String getFmac() {
        return fmac;
    }

    public void setFmac(String fmac) {
        this.fmac = fmac;
    }

    public String getFseq() {
        return fseq;
    }

    public void setFseq(String fseq) {
        this.fseq = fseq;
    }

    public double getUnqualifiedRate() {
        return unqualifiedRate;
    }

    public void setUnqualifiedRate(double unqualifiedRate) {
        this.unqualifiedRate = unqualifiedRate;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRate() {
        return rate;
    }



    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getDayOrNight() {
        return dayOrNight;
    }

    public void setDayOrNight(String dayOrNight) {
        this.dayOrNight = dayOrNight;
    }

    public Rate() {
    }
    // 构造函数
    public Rate(String name, String date, Double passTarget, String dayOrNight, Double rate, Integer ngNum, Integer allNum) {
        this.name = name;
        this.date = date;
        this.passTarget = passTarget;
        this.dayOrNight = dayOrNight;
        this.rate = rate;
        this.ngNum = ngNum;
        this.allNum = allNum;
    }

}
