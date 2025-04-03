package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 小组两小时时间段良率
 * </p>
 *
 * @author zhao
 * @since 2023-08-27
 */
public class DfGroupOkRate extends Model<DfGroupOkRate> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 小组id
     */
    private Integer groupId;

    /**
     * 小组检测数
     */
    private Integer allCheckNum;

    /**
     * 小组检测ok数
     */
    private Integer okCheckNum;

    /**
     * 良率
     */
    private Double okRate;

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
     * 类型：1-尺寸  2-外观
     */
    private Integer testType;

    /**
     * 班别
     */
    private String dayOrNight;

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
    public Integer getAllCheckNum() {
        return allCheckNum;
    }

    public void setAllCheckNum(Integer allCheckNum) {
        this.allCheckNum = allCheckNum;
    }
    public Integer getOkCheckNum() {
        return okCheckNum;
    }

    public void setOkCheckNum(Integer okCheckNum) {
        this.okCheckNum = okCheckNum;
    }
    public Double getOkRate() {
        return okRate;
    }

    public void setOkRate(Double okRate) {
        this.okRate = okRate;
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
    public Integer getTestType() {
        return testType;
    }

    public void setTestType(Integer testType) {
        this.testType = testType;
    }
    public String getDayOrNight() {
        return dayOrNight;
    }

    public void setDayOrNight(String dayOrNight) {
        this.dayOrNight = dayOrNight;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfGroupOkRate{" +
            "id=" + id +
            ", groupId=" + groupId +
            ", allCheckNum=" + allCheckNum +
            ", okCheckNum=" + okCheckNum +
            ", okRate=" + okRate +
            ", intheTime=" + intheTime +
            ", testTime=" + testTime +
            ", createTime=" + createTime +
            ", factory=" + factory +
            ", process=" + process +
            ", project=" + project +
            ", linebody=" + linebody +
            ", testType=" + testType +
            ", dayOrNight=" + dayOrNight +
        "}";
    }
}
