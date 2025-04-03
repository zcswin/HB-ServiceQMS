package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

/**
 * 缺陷数据类（记录DfAoiDefect类数据）
 */
public class Defect {
    /**
     * 框架ID，如果没有，可与样本ID相同
     */
    private String frameid;

    /**
     * 缺陷ID
     */
    private String defectid;

    /**
     * 缺陷编号
     */
    private String classid;

    /**
     * 缺陷特征
     */
    private String featurevalues;

    /**
     * 缺陷X坐标
     */
    @TableField("AOIxcenter")
    private String AOIxcenter;

    /**
     * 缺陷Y坐标
     */
    @TableField("AOIycenter")
    private String AOIycenter;

    /**
     * 缺陷抓取规则
     */
    private String attribute;

    /**
     * 缺陷严重等级
     */
    private String severityid;

    /**
     * 缺陷判定结果
     */
    private String qualityid;

    public String getFrameid() {
        return frameid;
    }

    public void setFrameid(String frameid) {
        this.frameid = frameid;
    }

    public String getDefectid() {
        return defectid;
    }

    public void setDefectid(String defectid) {
        this.defectid = defectid;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getFeaturevalues() {
        return featurevalues;
    }

    public void setFeaturevalues(String featurevalues) {
        this.featurevalues = featurevalues;
    }

    public String getAOIxcenter() {
        return AOIxcenter;
    }

    public void setAOIxcenter(String AOIxcenter) {
        this.AOIxcenter = AOIxcenter;
    }

    public String getAOIycenter() {
        return AOIycenter;
    }

    public void setAOIycenter(String AOIycenter) {
        this.AOIycenter = AOIycenter;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getSeverityid() {
        return severityid;
    }

    public void setSeverityid(String severityid) {
        this.severityid = severityid;
    }

    public String getQualityid() {
        return qualityid;
    }

    public void setQualityid(String qualityid) {
        this.qualityid = qualityid;
    }

    @Override
    public String toString() {
        return "Defect{" +
                "frameid='" + frameid + '\'' +
                ", defectid='" + defectid + '\'' +
                ", classid='" + classid + '\'' +
                ", featurevalues='" + featurevalues + '\'' +
                ", AOIxcenter='" + AOIxcenter + '\'' +
                ", AOIycenter='" + AOIycenter + '\'' +
                ", attribute='" + attribute + '\'' +
                ", severityid='" + severityid + '\'' +
                ", qualityid='" + qualityid + '\'' +
                '}';
    }
}
