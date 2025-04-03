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
 * 员工调机排名--机台良率
 * </p>
 *
 * @author zhao
 * @since 2023-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfAdMacOkRate extends Model<DfAdMacOkRate> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 机台编号
     */
    private String machineCode;

    /**
     * 检测次数
     */
    private Integer numTest;

    /**
     * OK次数
     */
    private Integer numOk;

    /**
     * OK率
     */
    private Double rateOk;

    /**
     * 工厂
     */
    private String factory;

    /**
     * 项目
     */
    private String project;

    /**
     * 工序
     */
    private String process;

    /**
     * 线体
     */
    private String linebody;

    /**
     * 白夜班
     */
    private String dayOrNight;

    /**
     * 类型  1-尺寸  2-外观
     */
    public Integer testType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp updateTime;


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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public Integer getNumTest() {
        return numTest;
    }

    public void setNumTest(Integer numTest) {
        this.numTest = numTest;
    }

    public Integer getNumOk() {
        return numOk;
    }

    public void setNumOk(Integer numOk) {
        this.numOk = numOk;
    }

    public Double getRateOk() {
        return rateOk;
    }

    public void setRateOk(Double rateOk) {
        this.rateOk = rateOk;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
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

    public String getDayOrNight() {
        return dayOrNight;
    }

    public void setDayOrNight(String dayOrNight) {
        this.dayOrNight = dayOrNight;
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

    public DfAdMacOkRate() {
    }
}
