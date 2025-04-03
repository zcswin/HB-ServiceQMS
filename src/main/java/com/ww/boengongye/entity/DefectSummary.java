package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

public class DefectSummary {

    @TableId(value = "summary_Id", type = IdType.AUTO)
    private Integer summaryId;    // 缺陷名称
    private Integer processSummaryId;      // 缺陷数量
    private double defectRate;    // 缺陷占比
    private String defectName;
    private Integer defectCount;

    // 构造函数
    public DefectSummary(String name, int count, double rate) {
        this.defectName = name;
        this.defectCount = count;
        this.defectRate = rate;
    }

    public DefectSummary() {

    }

    // 更新缺陷的数量和占比
    public void update(int count, double rate) {
        this.defectCount = count;
        this.defectRate = rate;
    }

    public Integer getSummaryId() {
        return summaryId;
    }

    public void setSummaryId(Integer summaryId) {
        this.summaryId = summaryId;
    }

    public Integer getProcessSummaryId() {
        return processSummaryId;
    }

    public void setProcessSummaryId(Integer processSummaryId) {
        this.processSummaryId = processSummaryId;
    }

    public double getDefectRate() {
        return defectRate;
    }

    public void setDefectRate(double defectRate) {
        this.defectRate = defectRate;
    }

    public String getDefectName() {
        return defectName;
    }

    public void setDefectName(String defectName) {
        this.defectName = defectName;
    }

    public Integer getDefectCount() {
        return defectCount;
    }

    public void setDefectCount(Integer defectCount) {
        this.defectCount = defectCount;
    }
}
