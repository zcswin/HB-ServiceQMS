package com.ww.boengongye.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2022-09-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfCuttingTime extends Model<DfCuttingTime> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    @TableField("Type_Data")
    public String typeData;


    @JsonProperty("machineCode")
    @JSONField(name="MachineCode")
    @TableField("MachineCode")
    public String MachineCode;

    @TableField("nNumTool")
    public String nNumTool;


    @JsonProperty("secDuration")
    @JSONField(name="SecDuration")
    @TableField("SecDuration")
    public String SecDuration;

    public String pubTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfCuttingTime() {
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

    public String getTypeData() {
        return typeData;
    }

    public void setTypeData(String typeData) {
        this.typeData = typeData;
    }

    public String getMachineCode() {
        return MachineCode;
    }

    public void setMachineCode(String machineCode) {
        MachineCode = machineCode;
    }

    public String getnNumTool() {
        return nNumTool;
    }

    public void setnNumTool(String nNumTool) {
        this.nNumTool = nNumTool;
    }

    public String getSecDuration() {
        return SecDuration;
    }

    public void setSecDuration(String secDuration) {
        SecDuration = secDuration;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
