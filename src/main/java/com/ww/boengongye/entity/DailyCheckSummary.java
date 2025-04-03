package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DailyCheckSummary {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private LocalDate date;
    private LocalDateTime fTime;
    private Integer sort;
    private String fBigpro;
    private String fSeq;
    private String fFac;
    private String fColor;
    private String fTestCategory;
    private String fTestMan;
    private String fSort;
    private String fStage;
    private String fTestType;
    private String fMac;
    private String fType;
    private String fLine;
    private String num;
    private Integer spotCheckCount;
    private Integer okNum;
    private double okRate;
    private double ngRate;
    private Integer ngNum;
    private String shift;
    private String statisticType;
    private LocalDate statisticDate;
    private LocalDate endDate;
    private String dataType ;
    private String typeNum;

    @TableField(exist = false)
    private List<DefectSummary> defect;

    // 完整的构造函数
    public DailyCheckSummary(String fStage, String fBigpro, String fColor, String fFac, String fType, String shift, String fSeq, String fTestCategory, String fLine, String fMac, String fTestType, String fTestMan) {
        this.fStage = fStage;
        this.fBigpro = fBigpro;
        this.fColor = fColor;
        this.fFac = fFac;
        this.fType = fType;
        this.shift = shift;
        this.fSeq = fSeq;
        this.fTestCategory = fTestCategory;
        this.fLine = fLine;
        this.fMac = fMac;
        this.fTestType = fTestType;
        this.fTestMan = fTestMan;
    }

    // 完整的构造函数
    public DailyCheckSummary(String fStage, String fBigpro, String fColor, String fFac, String fType, String shift, String fTestCategory, String fLine, String fMac, String fTestType, String fTestMan) {
        this.fStage = fStage;
        this.fBigpro = fBigpro;
        this.fColor = fColor;
        this.fFac = fFac;
        this.fType = fType;
        this.shift = shift;
        this.fTestCategory = fTestCategory;
        this.fLine = fLine;
        this.fMac = fMac;
        this.fTestType = fTestType;
        this.fTestMan = fTestMan;
    }


    public String getTypeNum() {
        return typeNum;
    }

    public void setTypeNum(String typeNum) {
        this.typeNum = typeNum;
    }

    public List<DefectSummary> getDefect() {
        return defect;
    }

    public void setDefect(List<DefectSummary> defect) {
        this.defect = defect;
    }

    public DailyCheckSummary() {
    }

    // 生成 Getter 和 Setter 方法



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getFTime() {
        return fTime;
    }

    public void setFTime(LocalDateTime fTime) {
        this.fTime = fTime;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getFBigpro() {
        return fBigpro;
    }

    public void setFBigpro(String fBigpro) {
        this.fBigpro = fBigpro;
    }

    public String getFSeq() {
        return fSeq;
    }

    public void setFSeq(String fSeq) {
        this.fSeq = fSeq;
    }

    public String getFFac() {
        return fFac;
    }

    public void setFFac(String fFac) {
        this.fFac = fFac;
    }

    public String getFColor() {
        return fColor;
    }

    public void setFColor(String fColor) {
        this.fColor = fColor;
    }

    public String getFTestCategory() {
        return fTestCategory;
    }

    public void setFTestCategory(String fTestCategory) {
        this.fTestCategory = fTestCategory;
    }

    public String getFTestMan() {
        return fTestMan;
    }

    public void setFTestMan(String fTestMan) {
        this.fTestMan = fTestMan;
    }

    public String getFSort() {
        return fSort;
    }

    public void setFSort(String fSort) {
        this.fSort = fSort;
    }

    public String getFStage() {
        return fStage;
    }

    public void setFStage(String fStage) {
        this.fStage = fStage;
    }

    public String getFTestType() {
        return fTestType;
    }

    public void setFTestType(String fTestType) {
        this.fTestType = fTestType;
    }

    public String getFMac() {
        return fMac;
    }

    public void setFMac(String fMac) {
        this.fMac = fMac;
    }

    public String getFType() {
        return fType;
    }

    public void setFType(String fType) {
        this.fType = fType;
    }

    public String getFLine() {
        return fLine;
    }

    public void setFLine(String fLine) {
        this.fLine = fLine;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }


    public Integer getSpotCheckCount() {
        return spotCheckCount;
    }

    public void setSpotCheckCount(Integer spotCheckCount) {
        this.spotCheckCount = spotCheckCount;
    }

    public Integer getOkNum() {
        return okNum;
    }

    public void setOkNum(Integer okNum) {
        this.okNum = okNum;
    }

    public double getOkRate() {
        return okRate;
    }

    public void setOkRate(double okRate) {
        this.okRate = okRate;
    }


    public double getNgRate() {
        return ngRate;
    }

    public void setNgRate(double ngRate) {
        this.ngRate = ngRate;
    }

    public Integer getNgNum() {
        return ngNum;
    }

    public void setNgNum(Integer ngNum) {
        this.ngNum = ngNum;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getStatisticType() {
        return statisticType;
    }

    public void setStatisticType(String statisticType) {
        this.statisticType = statisticType;
    }


    public LocalDate getStatisticDate() {
        return statisticDate;
    }

    public void setStatisticDate(LocalDate statisticDate) {
        this.statisticDate = statisticDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }


    @Override
    public String toString() {
        return "DailySummary{" +
                "date=" + date +
                ", fTime=" + fTime +
                ", sort=" + sort +
                ", fBigpro='" + fBigpro + '\'' +
                ", fSeq='" + fSeq + '\'' +
                ", fFac='" + fFac + '\'' +
                ", fColor='" + fColor + '\'' +
                ", fTestCategory='" + fTestCategory + '\'' +
                ", fTestMan='" + fTestMan + '\'' +
                ", fSort='" + fSort + '\'' +
                ", fStage='" + fStage + '\'' +
                ", fTestType='" + fTestType + '\'' +
                ", fMac='" + fMac + '\'' +
                ", fType='" + fType + '\'' +
                ", fLine='" + fLine + '\'' +
                ", num='" + num + '\'' +
                ", spotCheckCount=" + spotCheckCount +
                ", okNum=" + okNum +
                ", okRate=" + okRate +
                ", ngRate=" + ngRate +
                ", ngNum=" + ngNum +
                ", shift='" + shift + '\'' +
                ", statisticType='" + statisticType + '\'' +
                ", statisticDate=" + statisticDate +
                ", endDate=" + endDate +
                '}';
    }
}
