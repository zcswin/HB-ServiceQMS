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
 * 流转意见表
 * </p>
 *
 * @author zhao
 * @since 2022-10-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfFlowOpinion extends Model<DfFlowOpinion> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 下发人
     */
    public String sender;

    /**
     * 接收人
     */
    public String recipient;

    /**
     * 意见
     */
    public String opinion;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    /**
     * 绑定的流程数据id
     */
    public Integer flowDataId;


    /**
     * 下发人id
     */
    public String senderId;

    /**
     * 接收人id
     */
    public String recipientId;

    /**
     * 发送人部门
     */
    public String senderDepartment;


    /**
     * 备注
     */
    public String remark;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public DfFlowOpinion() {
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getFlowDataId() {
        return flowDataId;
    }

    public void setFlowDataId(Integer flowDataId) {
        this.flowDataId = flowDataId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getSenderDepartment() {
        return senderDepartment;
    }

    public void setSenderDepartment(String senderDepartment) {
        this.senderDepartment = senderDepartment;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
