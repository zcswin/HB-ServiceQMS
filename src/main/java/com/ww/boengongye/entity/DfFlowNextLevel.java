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
 * 流程的下一级配置
 * </p>
 *
 * @author zhao
 * @since 2022-10-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfFlowNextLevel extends Model<DfFlowNextLevel> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 绑定的id
     */
    public String parentId;

    /**
     * 下一级
     */
    public String nextLevel;

    /**
     * 操作步骤名称
     */
    public String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    public String createName;

    /**
     * 施加的状态
     */
    public String setStatus;

    /**
     * 流程类型
     */
    public String flowType;

    @TableField(exist = false)
    public  Integer blockId;
    @TableField(exist = false)
    public String blockName;

    /**
     * 绑定的流程的id
     */
    public Integer flowId;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfFlowNextLevel() {
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getNextLevel() {
        return nextLevel;
    }

    public void setNextLevel(String nextLevel) {
        this.nextLevel = nextLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSetStatus() {
        return setStatus;
    }

    public void setSetStatus(String setStatus) {
        this.setStatus = setStatus;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public Integer getBlockId() {
        return blockId;
    }

    public void setBlockId(Integer blockId) {
        this.blockId = blockId;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public Integer getFlowId() {
        return flowId;
    }

    public void setFlowId(Integer flowId) {
        this.flowId = flowId;
    }
}
