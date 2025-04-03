package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 小组机台超时率
 * </p>
 *
 * @author zhao
 * @since 2023-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfGroupMacOvertime extends Model<DfGroupMacOvertime> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 小组id
     */
    private Integer groupId;

    /**
     * 小组总机台数
     */
    private Integer allMacNum;

    /**
     * 超时机台数
     */
    private Integer overtimeMacNum;

    /**
     * 超时率
     */
    private Double overtimeRate;

    /**
     * 超时时间段
     */
    private String intheTime;

    /**
     * 测试时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp testTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    /**
     * 工厂
     */
    private String factory;

    /**
     * 工序
     */
    private String process;

    /**
     * 项目
     */
    private String project;

    /**
     * 线体
     */
    private String linebody;

    /**
     * 班别
     */
    private String dayOrNight;

    /**
     * 类型  1-尺寸  2-外观
     */
    private Integer testType;

    @TableField(exist = false)
    private String respon;  // 责任人


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Integer getTestType() {
        return testType;
    }

    public void setTestType(Integer testType) {
        this.testType = testType;
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

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getAllMacNum() {
        return allMacNum;
    }

    public void setAllMacNum(Integer allMacNum) {
        this.allMacNum = allMacNum;
    }

    public Integer getOvertimeMacNum() {
        return overtimeMacNum;
    }

    public void setOvertimeMacNum(Integer overtimeMacNum) {
        this.overtimeMacNum = overtimeMacNum;
    }

    public Double getOvertimeRate() {
        return overtimeRate;
    }

    public void setOvertimeRate(Double overtimeRate) {
        this.overtimeRate = overtimeRate;
    }

    public String getIntheTime() {
        return intheTime;
    }

    public void setIntheTime(String intheTime) {
        this.intheTime = intheTime;
    }

    public Timestamp getTestTime() {
        return testTime;
    }

    public void setTestTime(Timestamp testTime) {
        this.testTime = testTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
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

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
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

    public String getRespon() {
        return respon;
    }

    public void setRespon(String respon) {
        this.respon = respon;
    }
}
