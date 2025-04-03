package com.ww.boengongye.entity.exportExcelUpdate;

public class HzData {

    private String fFmac;

    private String fSeq;
    private String fSort;
    private String fDate;
    private Integer spotCheckCount;

    private Integer ngNum ;
    private double ngRate;

    public String getfDate() {
        return fDate;
    }

    public void setfDate(String fDate) {
        this.fDate = fDate;
    }

    public String getfSort() {
        return fSort;
    }

    public void setfSort(String fSort) {
        this.fSort = fSort;
    }

    public String getfSeq() {
        return fSeq;
    }

    public void setfSeq(String fSeq) {
        this.fSeq = fSeq;
    }

    public String getfFmac() {
        return fFmac;
    }

    public void setfFmac(String fFmac) {
        this.fFmac = fFmac;
    }

    public Integer getSpotCheckCount() {
        return spotCheckCount;
    }

    public void setSpotCheckCount(Integer spotCheckCount) {
        this.spotCheckCount = spotCheckCount;
    }

    public Integer getNgNum() {
        return ngNum;
    }

    public void setNgNum(Integer ngNum) {
        this.ngNum = ngNum;
    }

    public double getNgRate() {
        return ngRate;
    }

    public void setNgRate(double ngRate) {
        this.ngRate = ngRate;
    }
}
