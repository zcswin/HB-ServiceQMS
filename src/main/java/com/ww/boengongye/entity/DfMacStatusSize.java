package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *  尺寸机台状态
 * </p>
 *
 * @author zhao
 * @since 2023-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("尺寸机台状态")
public class DfMacStatusSize extends Model<DfMacStatusSize> {

    public static final long serialVersionUID = 1L;

    @ApiModelProperty("尺寸机台状态id")
    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    @ApiModelProperty("工序")
    private String process;

    @ApiModelProperty("机台号")
    @TableField("MachineCode")
    public String MachineCode;

    @ApiModelProperty("机台状态")
    @TableField("StatusID_Cur")
    public Integer statusidCur;

    @ApiModelProperty("推送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp pubTime;

    @ApiModelProperty("状态改变时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp changeTime;

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
    public String project;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public DfMacStatusSize() {
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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
}
