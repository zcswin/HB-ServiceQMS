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
public class DfGroupClose extends Model<DfGroupClose> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 小组id
     */
    private Integer groupId;

    /**
     * 关闭数
     */
    private Integer closeNum;

    /**
     * 开启数
     */
    private Integer openNum;

    /**
     * 关闭率
     */
    private Double closeRate;

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
    public Integer getCloseNum() {
        return closeNum;
    }

    public void setCloseNum(Integer closeNum) {
        this.closeNum = closeNum;
    }
    public Integer getOpenNum() {
        return openNum;
    }

    public void setOpenNum(Integer openNum) {
        this.openNum = openNum;
    }
    public Double getCloseRate() {
        return closeRate;
    }

    public void setCloseRate(Double closeRate) {
        this.closeRate = closeRate;
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
        return "DfGroupClose{" +
            "id=" + id +
            ", groupId=" + groupId +
            ", closeNum=" + closeNum +
            ", openNum=" + openNum +
            ", closeRate=" + closeRate +
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
