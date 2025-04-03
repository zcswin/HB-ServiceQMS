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
 * 设备故障记录
 * </p>
 *
 * @author zhao
 * @since 2022-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfMalfunctionRecord extends Model<DfMalfunctionRecord> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 机器编号
     */
    public String machineCode;

    /**
     * 故障类型
     */
    public String malfunctionType;

    /**
     * 故障原因
     */
    public String malfunctionCause;

    /**
     * 解决措施
     */
    public String countermeasure;

    /**
     * 故障时间
     */
    public String  malfunctionTime;

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
     * 工厂编号
     */
    public String factoryCode;

    /**
     * 工厂名称
     */
    @TableField(exist = false)
    public String factoryName;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfMalfunctionRecord() {
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

    public String getMalfunctionType() {
        return malfunctionType;
    }

    public void setMalfunctionType(String malfunctionType) {
        this.malfunctionType = malfunctionType;
    }

    public String getMalfunctionCause() {
        return malfunctionCause;
    }

    public void setMalfunctionCause(String malfunctionCause) {
        this.malfunctionCause = malfunctionCause;
    }

    public String getCountermeasure() {
        return countermeasure;
    }

    public void setCountermeasure(String countermeasure) {
        this.countermeasure = countermeasure;
    }

    public String getMalfunctionTime() {
        return malfunctionTime;
    }

    public void setMalfunctionTime(String malfunctionTime) {
        this.malfunctionTime = malfunctionTime;
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
}
