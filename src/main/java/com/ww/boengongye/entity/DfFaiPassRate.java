package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 首检通过率
 * </p>
 *
 * @author zhao
 * @since 2023-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfFaiPassRate extends Model<DfFaiPassRate> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 工厂
     */
    private String factory;

    /**
     * 工序
     */
    private String process;

    /**
     * 线体
     */
    private String linebody;

    /**
     * 项目
     */
    private String project;

    /**
     * 班别
     */
    private String dayOrNight;

    /**
     * 机台总数
     */
    private Integer allMacNum;

    /**
     * 开机数
     */
    private Integer openMacNum;

    /**
     * 首检开机数
     */
    private Integer faiOpenNum;

    /**
     * 首检通过数
     */
    private Integer faiPassNum;

    /**
     * 开机率
     */
    private Double openMacRate;

    /**
     * 首检通过率
     */
    private Double faiPassRate;

    /**
     * 首检开机率
     */
    private Double faiOpenRate;

    /**
     * 类型  1-尺寸  2-外观
     */
    public Integer testType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    public Integer getTestType() {
        return testType;
    }

    public void setTestType(Integer testType) {
        this.testType = testType;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfFaiPassRate() {
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

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getLinebody() {
        return linebody;
    }

    public void setLinebody(String linebody) {
        this.linebody = linebody;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getDayOrNight() {
        return dayOrNight;
    }

    public void setDayOrNight(String dayOrNight) {
        this.dayOrNight = dayOrNight;
    }

    public Integer getAllMacNum() {
        return allMacNum;
    }

    public void setAllMacNum(Integer allMacNum) {
        this.allMacNum = allMacNum;
    }

    public Integer getOpenMacNum() {
        return openMacNum;
    }

    public void setOpenMacNum(Integer openMacNum) {
        this.openMacNum = openMacNum;
    }

    public Integer getFaiOpenNum() {
        return faiOpenNum;
    }

    public void setFaiOpenNum(Integer faiOpenNum) {
        this.faiOpenNum = faiOpenNum;
    }

    public Integer getFaiPassNum() {
        return faiPassNum;
    }

    public void setFaiPassNum(Integer faiPassNum) {
        this.faiPassNum = faiPassNum;
    }

    public Double getOpenMacRate() {
        return openMacRate;
    }

    public void setOpenMacRate(Double openMacRate) {
        this.openMacRate = openMacRate;
    }

    public Double getFaiPassRate() {
        return faiPassRate;
    }

    public void setFaiPassRate(Double faiPassRate) {
        this.faiPassRate = faiPassRate;
    }

    public Double getFaiOpenRate() {
        return faiOpenRate;
    }

    public void setFaiOpenRate(Double faiOpenRate) {
        this.faiOpenRate = faiOpenRate;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
