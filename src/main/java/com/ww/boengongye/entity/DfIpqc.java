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
 * IPQC稽核数据
 * </p>
 *
 * @author zhao
 * @since 2022-09-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfIpqc extends Model<DfIpqc> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 工厂id
     */
    private Integer factoryId;

    /**
     * 工序id
     */
    private Integer procedureId;

    /**
     * 型号id
     */
    private Integer modelId;

    /**
     * 问题id
     */
    private Integer problemId;

    /**
     * 问题明细
     */
    private String problemDetial;

    /**
     * 影响机台数量
     */
    private Integer affectMachineNum;

    /**
     * 处理意见
     */
    private String handingOption;

    /**
     * 发起人
     */
    private String initiateMan;

    /**
     * 负责人
     */
    private String liableMan;

    /**
     * 事件等级
     */
    private String eventLevel;

    /**
     * 发生时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp occurrenceTime;

    /**
     * 关闭时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp solutionTime;

    /**
     * 时效时间，发生时间后4小时
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp linesTime;

    /**
     * 被接收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp receivedTime;

    /**
     * 结果：OK || NG
     */
    private String result;

    /**
     * 超时时长
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp timeoutDuration;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    /**
     * 更改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp updateTime;

    /**
     * 是否关闭
     */
    private String close;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfIpqc() {
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

    public Integer getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Integer factoryId) {
        this.factoryId = factoryId;
    }

    public Integer getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(Integer procedureId) {
        this.procedureId = procedureId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Integer getProblemId() {
        return problemId;
    }

    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }

    public String getProblemDetial() {
        return problemDetial;
    }

    public void setProblemDetial(String problemDetial) {
        this.problemDetial = problemDetial;
    }

    public Integer getAffectMachineNum() {
        return affectMachineNum;
    }

    public void setAffectMachineNum(Integer affectMachineNum) {
        this.affectMachineNum = affectMachineNum;
    }

    public String getHandingOption() {
        return handingOption;
    }

    public void setHandingOption(String handingOption) {
        this.handingOption = handingOption;
    }

    public String getInitiateMan() {
        return initiateMan;
    }

    public void setInitiateMan(String initiateMan) {
        this.initiateMan = initiateMan;
    }

    public String getLiableMan() {
        return liableMan;
    }

    public void setLiableMan(String liableMan) {
        this.liableMan = liableMan;
    }

    public String getEventLevel() {
        return eventLevel;
    }

    public void setEventLevel(String eventLevel) {
        this.eventLevel = eventLevel;
    }

    public Timestamp getOccurrenceTime() {
        return occurrenceTime;
    }

    public void setOccurrenceTime(Timestamp occurrenceTime) {
        this.occurrenceTime = occurrenceTime;
    }

    public Timestamp getSolutionTime() {
        return solutionTime;
    }

    public void setSolutionTime(Timestamp solutionTime) {
        this.solutionTime = solutionTime;
    }

    public Timestamp getLinesTime() {
        return linesTime;
    }

    public void setLinesTime(Timestamp linesTime) {
        this.linesTime = linesTime;
    }

    public Timestamp getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Timestamp receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Timestamp getTimeoutDuration() {
        return timeoutDuration;
    }

    public void setTimeoutDuration(Timestamp timeoutDuration) {
        this.timeoutDuration = timeoutDuration;
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

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }
}
