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
 * 设备状态校准记录
 * </p>
 *
 * @author zhao
 * @since 2022-11-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfDeviceStatusCalibration extends Model<DfDeviceStatusCalibration> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 设备编号
     */
    public String machineCode;

    /**
     * 校准前状态
     */
    public String startStatus;

    /**
     * 校准后状态
     */
    public String endStatus;

    /**
     * 校准时间
     */
    public String calibrationTime;

    /**
     * 消耗物品
     */
    public String consumable;

    /**
     * 测试标准版本
     */
    public String checkVersion;

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

    @TableField(exist = false)
    public String factoryName;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public DfDeviceStatusCalibration() {
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

    public String getStartStatus() {
        return startStatus;
    }

    public void setStartStatus(String startStatus) {
        this.startStatus = startStatus;
    }

    public String getEndStatus() {
        return endStatus;
    }

    public void setEndStatus(String endStatus) {
        this.endStatus = endStatus;
    }

    public String getCalibrationTime() {
        return calibrationTime;
    }

    public void setCalibrationTime(String calibrationTime) {
        this.calibrationTime = calibrationTime;
    }

    public String getConsumable() {
        return consumable;
    }

    public void setConsumable(String consumable) {
        this.consumable = consumable;
    }

    public String getCheckVersion() {
        return checkVersion;
    }

    public void setCheckVersion(String checkVersion) {
        this.checkVersion = checkVersion;
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
