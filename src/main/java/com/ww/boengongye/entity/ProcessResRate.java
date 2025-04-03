package com.ww.boengongye.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProcessResRate {
    private String process;
    private Integer allNum;
    private Integer okNum;
    private Integer ngNum;
    private Double okRate;
    private Double alwaysOkRate;

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public Integer getAllNum() {
        return allNum;
    }

    public void setAllNum(Integer allNum) {
        this.allNum = allNum;
    }

    public Integer getOkNum() {
        return okNum;
    }

    public void setOkNum(Integer okNum) {
        this.okNum = okNum;
    }

    public Integer getNgNum() {
        return ngNum;
    }

    public void setNgNum(Integer ngNum) {
        this.ngNum = ngNum;
    }

    public Double getOkRate() {
        return okRate;
    }

    public void setOkRate(Double okRate) {
        this.okRate = okRate;
    }

    public Double getAlwaysOkRate() {
        return alwaysOkRate;
    }

    public void setAlwaysOkRate(Double alwaysOkRate) {
        this.alwaysOkRate = alwaysOkRate;
    }

    public ProcessResRate() {
    }
}
