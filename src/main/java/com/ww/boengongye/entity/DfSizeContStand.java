package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 尺寸管控标准
 * </p>
 *
 * @author zhao
 * @since 2023-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfSizeContStand extends Model<DfSizeContStand> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 测量项
     */
    private String testItem;

    /**
     * 标准值
     */
    private Double standard;

    /**
     * 上公差
     */
    private Double upperToler;

    /**
     * 下公差
     */
    private Double lowerToler;

    /**
     * 上限
     */
    private Double upperLimit;

    /**
     * 下限
     */
    private Double lowerLimit;

    /**
     * 隔离上限
     */
    private Double isolaUpperLimit;

    /**
     * 隔离下限
     */
    private Double isolaLowerLimit;

    /**
     * 均值
     */
    private Double mean;

    /**
     * cpk
     */
    private Double cpk;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp updateTime;

    /**
     * 工厂
     */
    private String factory;

    /**
     * 线体
     */
    private String line;

    /**
     * 工序
     */
    private String process;

    /**
     * FAI
     */
    private String fai;

    /**
     * 是否重点0否1是
     */
    private String keyPoint;

    public Integer isUse;
    public Integer sort;

    @ApiModelProperty("项目")
    public String project;
    @ApiModelProperty("颜色")
    public String color;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfSizeContStand() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTestItem() {
        return testItem;
    }

    public void setTestItem(String testItem) {
        this.testItem = testItem;
    }

    public Double getStandard() {
        return standard;
    }

    public void setStandard(Double standard) {
        this.standard = standard;
    }

    public Double getUpperToler() {
        return upperToler;
    }

    public void setUpperToler(Double upperToler) {
        this.upperToler = upperToler;
    }

    public Double getLowerToler() {
        return lowerToler;
    }

    public void setLowerToler(Double lowerToler) {
        this.lowerToler = lowerToler;
    }

    public Double getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(Double upperLimit) {
        this.upperLimit = upperLimit;
    }

    public Double getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(Double lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public Double getIsolaUpperLimit() {
        return isolaUpperLimit;
    }

    public void setIsolaUpperLimit(Double isolaUpperLimit) {
        this.isolaUpperLimit = isolaUpperLimit;
    }

    public Double getIsolaLowerLimit() {
        return isolaLowerLimit;
    }

    public void setIsolaLowerLimit(Double isolaLowerLimit) {
        this.isolaLowerLimit = isolaLowerLimit;
    }

    public Double getMean() {
        return mean;
    }

    public void setMean(Double mean) {
        this.mean = mean;
    }

    public Double getCpk() {
        return cpk;
    }

    public void setCpk(Double cpk) {
        this.cpk = cpk;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getFai() {
        return fai;
    }

    public void setFai(String fai) {
        this.fai = fai;
    }

    public String getKeyPoint() {
        return keyPoint;
    }

    public void setKeyPoint(String keyPoint) {
        this.keyPoint = keyPoint;
    }

    public Integer getIsUse() {
        return isUse;
    }

    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
