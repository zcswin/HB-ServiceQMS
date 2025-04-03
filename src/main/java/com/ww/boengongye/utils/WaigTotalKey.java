package com.ww.boengongye.utils;

import java.sql.Timestamp;
import java.util.Objects;

public class WaigTotalKey {
    private String date;
    private String fType;
    private String fFac;
    private String fStage;
    private String shift;
    private String fBigpro;
    private String fColor;
    private String fSeq;
    private String fLine;
    private String fTestType;
    private String fTestCategory;
    private String fMac;
    private String fTestMan;
    private Timestamp fTime;
    private int sort;

    public WaigTotalKey(String date, String fType, String fFac, String fStage, String shift, String fBigpro,
                        String fColor, String fSeq, String fLine, String fTestType, String fTestCategory,
                        String fMac, String fTestMan, Timestamp fTime, int sort) {
        this.date = date;
        this.fType = fType;
        this.fFac = fFac;
        this.fStage = fStage;
        this.shift = shift;
        this.fBigpro = fBigpro;
        this.fColor = fColor;
        this.fSeq = fSeq;
        this.fLine = fLine;
        this.fTestType = fTestType;
        this.fTestCategory = fTestCategory;
        this.fMac = fMac;
        this.fTestMan = fTestMan;
        this.fTime = fTime;
        this.sort = sort;
    }

    // 生成 getter 方法

    public String getDate() {
        return date;
    }
    public String getfType() {
        return fType;
    }
    public String getfFac() {
        return fFac;
    }
    public String getfStage() {
        return fStage;
    }
    public String getShift() {
        return shift;
    }
    public String getfBigpro() {
        return fBigpro;
    }
    public String getfColor() {
        return fColor;
    }
    public String getfSeq() {
        return fSeq;
    }
    public String getfLine() {
        return fLine;
    }
    public String getfTestType() {
        return fTestType;
    }
    public String getfTestCategory() {
        return fTestCategory;
    }
    public String getfMac() {
        return fMac;
    }
    public String getfTestMan() {
        return fTestMan;
    }
    public Timestamp getfTime() {
        return fTime;
    }
    public int getSort() {
        return sort;
    }

    // 重写 equals 和 hashCode，保证分组正确

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WaigTotalKey)) return false;
        WaigTotalKey that = (WaigTotalKey) o;
        return Objects.equals(date, that.date) &&
                Objects.equals(fType, that.fType) &&
                Objects.equals(fFac, that.fFac) &&
                Objects.equals(fStage, that.fStage) &&
                Objects.equals(shift, that.shift) &&
                Objects.equals(fBigpro, that.fBigpro) &&
                Objects.equals(fColor, that.fColor) &&
                Objects.equals(fSeq, that.fSeq) &&
                Objects.equals(fLine, that.fLine) &&
                Objects.equals(fTestType, that.fTestType) &&
                Objects.equals(fTestCategory, that.fTestCategory) &&
                Objects.equals(fMac, that.fMac) &&
                Objects.equals(fTestMan, that.fTestMan) &&
                Objects.equals(fTime, that.fTime) &&
                Objects.equals(sort, that.sort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, fType, fFac, fStage, shift, fBigpro, fColor, fSeq, fLine, fTestType, fTestCategory, fMac, fTestMan, fTime, sort);
    }
}