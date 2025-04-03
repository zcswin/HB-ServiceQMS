package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 工序需求机台数量表
 * </p>
 *
 * @author zhao
 * @since 2022-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfProcessNeedMac extends Model<DfProcessNeedMac> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 工序编号
     */
    public String processCode;

    /**
     * 需求数量
     */
    public Integer needQuantity;

    /**
     * 实际数量
     */
    public Integer realQuantity;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public java.sql.Timestamp createTime;

    public String createName;

    public String factoryCode;

    /**
     * 备注
     */
    public String remark;

    @TableField(exist = false)
    public List<DfProcessNeedRelationMachine> machines;

    @TableField(exist = false)
    public String factoryName;

    @TableField(exist = false)
    public String processName;
    
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfProcessNeedMac() {
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

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public Integer getNeedQuantity() {
        return needQuantity;
    }

    public void setNeedQuantity(Integer needQuantity) {
        this.needQuantity = needQuantity;
    }

    public Integer getRealQuantity() {
        return realQuantity;
    }

    public void setRealQuantity(Integer realQuantity) {
        this.realQuantity = realQuantity;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<DfProcessNeedRelationMachine> getMachines() {
        return machines;
    }

    public void setMachines(List<DfProcessNeedRelationMachine> machines) {
        this.machines = machines;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }
}
