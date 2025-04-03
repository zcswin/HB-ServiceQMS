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
 * 小组调机能力
 * </p>
 *
 * @author zhao
 * @since 2023-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfGroupAdjustment extends Model<DfGroupAdjustment> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 小组id
     */
    private Integer groupId;

    /**
     * 小组机台总数
     */
    private Integer allMacNum;

    /**
     * 隔离个数
     */
    private Integer quarantineNum;

    /**
     * 调机个数
     */
    private Integer adjustmentNum;

    /**
     * 正常个数
     */
    private Integer normalNum;

    /**
     * 闲置台数
     */
    private Integer unusedNum;

    /**
     * 测试总数 隔离+调机+正常+闲置
     */
    private Integer allTestNum;

    /**
     * 隔离率
     */
    private Double quarantineRate;

    /**
     * 调机率
     */
    private Double adjustmentRate;

    /**
     * 正常率
     */
    private Double normalRate;

    /**
     * 闲置率
     */
    private Double unusedRate;

    /**
     * 班别
     */
    private String dayOrNight;

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
     * 类型  1-尺寸  2-外观
     */
    private Integer testType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    @TableField(exist = false)
    private String groupName;  // 小组名

    @TableField(exist = false)
    private String groupRespon;  // 小组负责人

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

    public Integer getQuarantineNum() {
        return quarantineNum;
    }

    public void setQuarantineNum(Integer quarantineNum) {
        this.quarantineNum = quarantineNum;
    }

    public Integer getAdjustmentNum() {
        return adjustmentNum;
    }

    public void setAdjustmentNum(Integer adjustmentNum) {
        this.adjustmentNum = adjustmentNum;
    }

    public Integer getNormalNum() {
        return normalNum;
    }

    public void setNormalNum(Integer normalNum) {
        this.normalNum = normalNum;
    }

    public Integer getUnusedNum() {
        return unusedNum;
    }

    public void setUnusedNum(Integer unusedNum) {
        this.unusedNum = unusedNum;
    }

    public Integer getAllTestNum() {
        return allTestNum;
    }

    public void setAllTestNum(Integer allTestNum) {
        this.allTestNum = allTestNum;
    }

    public Double getQuarantineRate() {
        return quarantineRate;
    }

    public void setQuarantineRate(Double quarantineRate) {
        this.quarantineRate = quarantineRate;
    }

    public Double getAdjustmentRate() {
        return adjustmentRate;
    }

    public void setAdjustmentRate(Double adjustmentRate) {
        this.adjustmentRate = adjustmentRate;
    }

    public Double getNormalRate() {
        return normalRate;
    }

    public void setNormalRate(Double normalRate) {
        this.normalRate = normalRate;
    }

    public Double getUnusedRate() {
        return unusedRate;
    }

    public void setUnusedRate(Double unusedRate) {
        this.unusedRate = unusedRate;
    }

    public String getDayOrNight() {
        return dayOrNight;
    }

    public void setDayOrNight(String dayOrNight) {
        this.dayOrNight = dayOrNight;
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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupRespon() {
        return groupRespon;
    }

    public void setGroupRespon(String groupRespon) {
        this.groupRespon = groupRespon;
    }

    public DfGroupAdjustment() {
    }
}
