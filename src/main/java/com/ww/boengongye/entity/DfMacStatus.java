package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2022-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfMacStatus extends Model<DfMacStatus> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    @TableField("MachineCode")
    public String MachineCode;

    @TableField("StatusID_Cur")
    public Integer statusidCur;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp pubTime;

    /**
     * 设备上次状态
     */
    @TableField("StatusID_Pre")
    private Integer statusidPre;

    /**
     * 上次状态持续时间
     */
    @TableField("StatusStep")
    private Integer StatusStep;

    /**
     * 告警信息
     */
    private String warningMes;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp updateTime;



    @TableField(exist = false)
    public String statusName;
    @TableField(exist = false)
    public String statusColor;

    @TableField(exist = false)
    public Double positionX;
    @TableField(exist = false)
    public Double positionY;

    @TableField(exist = false)
    public Double positionZ;

    @TableField(exist = false)
    private Integer count;



    @TableField(exist = false)
    public String processName;


    @TableField(exist = false)
    public String macType;


    @TableField(exist = false)
    public String personName;

    @TableField(exist = false)
    public String devIp;

    @TableField(exist = false)
    public String unitType;

    @TableField(exist = false)
    public String statusPreName;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public DfMacStatus() {
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
        return MachineCode;
    }

    public void setMachineCode(String machineCode) {
        MachineCode = machineCode;
    }

    public Integer getStatusidCur() {
        return statusidCur;
    }

    public void setStatusidCur(Integer statusidCur) {
        this.statusidCur = statusidCur;
    }

    public Timestamp getPubTime() {
        return pubTime;
    }

    public void setPubTime(Timestamp pubTime) {
        this.pubTime = pubTime;
    }


    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }

    public Double getPositionX() {
        return positionX;
    }

    public void setPositionX(Double positionX) {
        this.positionX = positionX;
    }

    public Double getPositionY() {
        return positionY;
    }

    public void setPositionY(Double positionY) {
        this.positionY = positionY;
    }

    public Double getPositionZ() {
        return positionZ;
    }

    public void setPositionZ(Double positionZ) {
        this.positionZ = positionZ;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getMacType() {
        return macType;
    }

    public void setMacType(String macType) {
        this.macType = macType;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getDevIp() {
        return devIp;
    }

    public void setDevIp(String devIp) {
        this.devIp = devIp;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public Integer getStatusidPre() {
        return statusidPre;
    }

    public void setStatusidPre(Integer statusidPre) {
        this.statusidPre = statusidPre;
    }

    public Integer getStatusStep() {
        return StatusStep;
    }

    public void setStatusStep(Integer statusStep) {
        StatusStep = statusStep;
    }

    public String getWarningMes() {
        return warningMes;
    }

    public void setWarningMes(String warningMes) {
        this.warningMes = warningMes;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatusPreName() {
        return statusPreName;
    }

    public void setStatusPreName(String statusPreName) {
        this.statusPreName = statusPreName;
    }
}
