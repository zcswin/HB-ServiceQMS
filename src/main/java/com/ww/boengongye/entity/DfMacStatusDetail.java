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
public class DfMacStatusDetail extends Model<DfMacStatusDetail> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("MachineCode")
    private String MachineCode;

    @TableField("RunMode_Cur")
    private String runmodeCur;

    @TableField("StatusID_Cur")
    private Integer statusidCur;

    @TableField("StatusID_Pre")
    private Integer statusidPre;

    @TableField("StatusStep")
    private Integer StatusStep;

    @TableField("Type_Data")
    private Integer typeData;

    private String fileProgMain;

    private String numProgMain;

    private Long pubTime;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    //@TableField("create_time")
    public Timestamp createTime;

    @TableField(exist = false)
    private String curStatusDetail; //当前状态详情

    @TableField(exist = false)
    private String curStatus; //当前状态

    @TableField(exist = false)
    private String curStatusColor; //当前状态颜色

    @TableField(exist = false)
    private String preStatusDetail; //之前状态详情

    @TableField(exist = false)
    private String preStatus; //之前状态

    @TableField(exist = false)
    private String statusName;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfMacStatusDetail() {
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

    public String getRunmodeCur() {
        return runmodeCur;
    }

    public void setRunmodeCur(String runmodeCur) {
        this.runmodeCur = runmodeCur;
    }

    public Integer getStatusidCur() {
        return statusidCur;
    }

    public void setStatusidCur(Integer statusidCur) {
        this.statusidCur = statusidCur;
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

    public Integer getTypeData() {
        return typeData;
    }

    public void setTypeData(Integer typeData) {
        this.typeData = typeData;
    }

    public String getFileProgMain() {
        return fileProgMain;
    }

    public void setFileProgMain(String fileProgMain) {
        this.fileProgMain = fileProgMain;
    }

    public String getNumProgMain() {
        return numProgMain;
    }

    public void setNumProgMain(String numProgMain) {
        this.numProgMain = numProgMain;
    }

    public Long getPubTime() {
        return pubTime;
    }

    public void setPubTime(Long pubTime) {
        this.pubTime = pubTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getCurStatusDetail() {
        return curStatusDetail;
    }

    public void setCurStatusDetail(String curStatusDetail) {
        this.curStatusDetail = curStatusDetail;
    }

    public String getCurStatus() {
        return curStatus;
    }

    public void setCurStatus(String curStatus) {
        this.curStatus = curStatus;
    }

    public String getCurStatusColor() {
        return curStatusColor;
    }

    public void setCurStatusColor(String curStatusColor) {
        this.curStatusColor = curStatusColor;
    }

    public String getPreStatusDetail() {
        return preStatusDetail;
    }

    public void setPreStatusDetail(String preStatusDetail) {
        this.preStatusDetail = preStatusDetail;
    }

    public String getPreStatus() {
        return preStatus;
    }

    public void setPreStatus(String preStatus) {
        this.preStatus = preStatus;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
