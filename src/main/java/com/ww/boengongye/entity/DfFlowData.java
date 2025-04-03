package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 流程数据
 * </p>
 *
 * @author zhao
 * @since 2022-10-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfFlowData extends Model<DfFlowData> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    public String name;

    /**
     * 流程类型
     */
    public String flowType;

    /**
     * 当前流程位置
     */
    public Integer flowLevel;

    /**
     * 数据类型
     */
    public String dataType;

    /**
     * 待审核数据id
     */
    public Integer dataId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    /**
     * 创建人
     */
    public String createName;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp updateTime;

    /**
     * 更新人
     */
    public String updateName;

    /**
     * 补充说明
     */
    public String remark;

    /**
     * 流程状态
     */
    public String status;

    /**
     * 流程顺序记录
     */
    public String flowLog;

    /**
     * 当前流程名称
     */
    public String flowLevelName;

    /**
     * 审批有效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp validTime;
    /**
     * 创建人id
     */
    public  String createUserId;


    /**
     *等级1阅读时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp level1ReadTime;

    /**
     *等级2阅读时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp level2ReadTime;

    /**
     *等级3阅读时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp level3ReadTime;

    /**
     *等级4阅读时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp level4ReadTime;

    /**
     *等级5阅读时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp level5ReadTime;

    /**
     *等级1确认时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp level1AffirmTime;

    /**
     *等级2确认时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp level2AffirmTime;


    /**
     *等级3确认时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp level3AffirmTime;

    /**
     *等级4确认时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp level4AffirmTime;


    /**
     *等级5确认时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp level5AffirmTime;


    /**
     *等级1推送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp level1PushTime;

    /**
     *等级2推送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp level2PushTime;

    /**
     *等级3推送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp level3PushTime;

    /**
     *等级4推送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp level4PushTime;

    /**
     *等级5推送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp level5PushTime;

    /**
     * 下一级审批人
     */
    public String nextLevelUser;

    /**
     * 当前审批人
     */
    public String nowLevelUser;
    public String nowLevelUserName;
    /**
     * 等级1超时时间(分钟)
     */
    public Double level1Overtime;
    public Double level2Overtime;
    public Double level3Overtime;

    /**
     *提交时间时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp submitTime;

    /**
     * 发起人确认时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp  initiatorAffirmTime;


    /**
     * 超时状态
     */

    public String  overtimeStatus;

    /**
     * 开始超时时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp   startTimeout;

    public Integer readTimeMax;

    public Integer disposeTimeMax;

    /**
     * 阅读人
     */
    public String readName;

    /**
     * 确认人
     */
    public String affirmName;

    /**
     * 提交人
     */
    public String submitName;

    /**
     * fa/ca阅读时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp  faCaReadTime;

    /**
     * 当前超时节点
     */
    public Integer currentTimeoutLevel;
    /**
     * 显示当前节点处理人
     */
    public String  showApprover;
    @ApiModelProperty("NG阶段")
    public String  ngPhase;

    @TableField(exist = false)
    public Integer nextLevel;

    @TableField(exist = false)
    public String flowOpinion;

    @TableField(exist = false)
    public String operator;

    @TableField(exist = false)
    public String operatorId;



    @TableField(exist = false)
    public Integer startTime;

    @TableField(exist = false)
    public Integer endTime;


    @TableField(exist = false)
    public String process;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfFlowData() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public Integer getFlowLevel() {
        return flowLevel;
    }

    public void setFlowLevel(Integer flowLevel) {
        this.flowLevel = flowLevel;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }



    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFlowLog() {
        return flowLog;
    }

    public void setFlowLog(String flowLog) {
        this.flowLog = flowLog;
    }

    public String getFlowLevelName() {
        return flowLevelName;
    }

    public void setFlowLevelName(String flowLevelName) {
        this.flowLevelName = flowLevelName;
    }

    public Timestamp getValidTime() {
        return validTime;
    }

    public void setValidTime(Timestamp validTime) {
        this.validTime = validTime;
    }

    public Integer getNextLevel() {
        return nextLevel;
    }

    public void setNextLevel(Integer nextLevel) {
        this.nextLevel = nextLevel;
    }

    public String getFlowOpinion() {
        return flowOpinion;
    }

    public void setFlowOpinion(String flowOpinion) {
        this.flowOpinion = flowOpinion;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }


    public Timestamp getLevel1ReadTime() {
        return level1ReadTime;
    }

    public void setLevel1ReadTime(Timestamp level1ReadTime) {
        this.level1ReadTime = level1ReadTime;
    }

    public Timestamp getLevel2ReadTime() {
        return level2ReadTime;
    }

    public void setLevel2ReadTime(Timestamp level2ReadTime) {
        this.level2ReadTime = level2ReadTime;
    }

    public Timestamp getLevel3ReadTime() {
        return level3ReadTime;
    }

    public void setLevel3ReadTime(Timestamp level3ReadTime) {
        this.level3ReadTime = level3ReadTime;
    }

    public Timestamp getLevel1AffirmTime() {
        return level1AffirmTime;
    }

    public void setLevel1AffirmTime(Timestamp level1AffirmTime) {
        this.level1AffirmTime = level1AffirmTime;
    }

    public Timestamp getLevel2AffirmTime() {
        return level2AffirmTime;
    }

    public void setLevel2AffirmTime(Timestamp level2AffirmTime) {
        this.level2AffirmTime = level2AffirmTime;
    }

    public Timestamp getLevel3AffirmTime() {
        return level3AffirmTime;
    }

    public void setLevel3AffirmTime(Timestamp level3AffirmTime) {
        this.level3AffirmTime = level3AffirmTime;
    }

    public Timestamp getLevel1PushTime() {
        return level1PushTime;
    }

    public void setLevel1PushTime(Timestamp level1PushTime) {
        this.level1PushTime = level1PushTime;
    }

    public Timestamp getLevel2PushTime() {
        return level2PushTime;
    }

    public void setLevel2PushTime(Timestamp level2PushTime) {
        this.level2PushTime = level2PushTime;
    }

    public Timestamp getLevel3PushTime() {
        return level3PushTime;
    }

    public void setLevel3PushTime(Timestamp level3PushTime) {
        this.level3PushTime = level3PushTime;
    }

    public String getNextLevelUser() {
        return nextLevelUser;
    }

    public void setNextLevelUser(String nextLevelUser) {
        this.nextLevelUser = nextLevelUser;
    }

    public String getNowLevelUser() {
        return nowLevelUser;
    }

    public void setNowLevelUser(String nowLevelUser) {
        this.nowLevelUser = nowLevelUser;
    }

    public String getNowLevelUserName() {
        return nowLevelUserName;
    }

    public void setNowLevelUserName(String nowLevelUserName) {
        this.nowLevelUserName = nowLevelUserName;
    }

    public Double getLevel1Overtime() {
        return level1Overtime;
    }

    public void setLevel1Overtime(Double level1Overtime) {
        this.level1Overtime = level1Overtime;
    }

    public Double getLevel2Overtime() {
        return level2Overtime;
    }

    public void setLevel2Overtime(Double level2Overtime) {
        this.level2Overtime = level2Overtime;
    }

    public Double getLevel3Overtime() {
        return level3Overtime;
    }

    public void setLevel3Overtime(Double level3Overtime) {
        this.level3Overtime = level3Overtime;
    }

    public Timestamp getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Timestamp submitTime) {
        this.submitTime = submitTime;
    }

    public Timestamp getInitiatorAffirmTime() {
        return initiatorAffirmTime;
    }

    public void setInitiatorAffirmTime(Timestamp initiatorAffirmTime) {
        this.initiatorAffirmTime = initiatorAffirmTime;
    }

    public String getOvertimeStatus() {
        return overtimeStatus;
    }

    public void setOvertimeStatus(String overtimeStatus) {
        this.overtimeStatus = overtimeStatus;
    }

    public Timestamp getStartTimeout() {
        return startTimeout;
    }

    public void setStartTimeout(Timestamp startTimeout) {
        this.startTimeout = startTimeout;
    }

    public Integer getReadTimeMax() {
        return readTimeMax;
    }

    public void setReadTimeMax(Integer readTimeMax) {
        this.readTimeMax = readTimeMax;
    }

    public Integer getDisposeTimeMax() {
        return disposeTimeMax;
    }

    public void setDisposeTimeMax(Integer disposeTimeMax) {
        this.disposeTimeMax = disposeTimeMax;
    }

    public String getReadName() {
        return readName;
    }

    public void setReadName(String readName) {
        this.readName = readName;
    }

    public String getAffirmName() {
        return affirmName;
    }

    public void setAffirmName(String affirmName) {
        this.affirmName = affirmName;
    }

    public String getSubmitName() {
        return submitName;
    }

    public void setSubmitName(String submitName) {
        this.submitName = submitName;
    }

    public Timestamp getFaCaReadTime() {
        return faCaReadTime;
    }

    public void setFaCaReadTime(Timestamp faCaReadTime) {
        this.faCaReadTime = faCaReadTime;
    }

    public Integer getCurrentTimeoutLevel() {
        return currentTimeoutLevel;
    }

    public void setCurrentTimeoutLevel(Integer currentTimeoutLevel) {
        this.currentTimeoutLevel = currentTimeoutLevel;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public String getShowApprover() {
        return showApprover;
    }

    public void setShowApprover(String showApprover) {
        this.showApprover = showApprover;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public Timestamp getLevel4ReadTime() {
        return level4ReadTime;
    }

    public void setLevel4ReadTime(Timestamp level4ReadTime) {
        this.level4ReadTime = level4ReadTime;
    }

    public Timestamp getLevel5ReadTime() {
        return level5ReadTime;
    }

    public void setLevel5ReadTime(Timestamp level5ReadTime) {
        this.level5ReadTime = level5ReadTime;
    }

    public Timestamp getLevel4AffirmTime() {
        return level4AffirmTime;
    }

    public void setLevel4AffirmTime(Timestamp level4AffirmTime) {
        this.level4AffirmTime = level4AffirmTime;
    }

    public Timestamp getLevel5AffirmTime() {
        return level5AffirmTime;
    }

    public void setLevel5AffirmTime(Timestamp level5AffirmTime) {
        this.level5AffirmTime = level5AffirmTime;
    }

    public Timestamp getLevel4PushTime() {
        return level4PushTime;
    }

    public void setLevel4PushTime(Timestamp level4PushTime) {
        this.level4PushTime = level4PushTime;
    }

    public Timestamp getLevel5PushTime() {
        return level5PushTime;
    }

    public void setLevel5PushTime(Timestamp level5PushTime) {
        this.level5PushTime = level5PushTime;
    }

    @Override
    public String toString() {
        return "DfFlowData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", flowType='" + flowType + '\'' +
                ", flowLevel=" + flowLevel +
                ", dataType='" + dataType + '\'' +
                ", dataId=" + dataId +
                ", createTime=" + createTime +
                ", createName='" + createName + '\'' +
                ", updateTime=" + updateTime +
                ", updateName='" + updateName + '\'' +
                ", remark='" + remark + '\'' +
                ", status='" + status + '\'' +
                ", flowLog='" + flowLog + '\'' +
                ", flowLevelName='" + flowLevelName + '\'' +
                ", validTime=" + validTime +
                ", createUserId='" + createUserId + '\'' +
                ", level1ReadTime=" + level1ReadTime +
                ", level2ReadTime=" + level2ReadTime +
                ", level3ReadTime=" + level3ReadTime +
                ", level1AffirmTime=" + level1AffirmTime +
                ", level2AffirmTime=" + level2AffirmTime +
                ", level3AffirmTime=" + level3AffirmTime +
                ", level1PushTime=" + level1PushTime +
                ", level2PushTime=" + level2PushTime +
                ", level3PushTime=" + level3PushTime +
                ", nextLevelUser='" + nextLevelUser + '\'' +
                ", nowLevelUser='" + nowLevelUser + '\'' +
                ", nowLevelUserName='" + nowLevelUserName + '\'' +
                ", level1Overtime=" + level1Overtime +
                ", level2Overtime=" + level2Overtime +
                ", level3Overtime=" + level3Overtime +
                ", submitTime=" + submitTime +
                ", initiatorAffirmTime=" + initiatorAffirmTime +
                ", overtimeStatus='" + overtimeStatus + '\'' +
                ", startTimeout=" + startTimeout +
                ", readTimeMax=" + readTimeMax +
                ", disposeTimeMax=" + disposeTimeMax +
                ", currentTimeoutLevel=" + currentTimeoutLevel +
                ", nextLevel=" + nextLevel +
                ", flowOpinion='" + flowOpinion + '\'' +
                ", operator='" + operator + '\'' +
                ", operatorId='" + operatorId + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
