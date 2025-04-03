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
 * @since 2022-08-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfMacModelPosition extends Model<DfMacModelPosition> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    @TableField("MachineCode")
    public String MachineCode;

    public Double positionX;

    public Double positionY;

    public Double positionZ;

    public LocalDateTime createTime;

    public String area;

    public String positionCode;
    public String macType;

    @TableField(exist = false)
    public Integer status;
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp statusTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public DfMacModelPosition() {
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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }


    public String getMacType() {
        return macType;
    }

    public void setMacType(String macType) {
        this.macType = macType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getStatusTime() {
        return statusTime;
    }

    public void setStatusTime(Timestamp statusTime) {
        this.statusTime = statusTime;
    }
}
