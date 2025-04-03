package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 判定记录表
 * </p>
 *
 * @author zhao
 * @since 2023-08-11
 */
public class DfAoiDecideLog extends Model<DfAoiDecideLog> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 缺陷ID
     */
    private String defectId;

    /**
     * AOI判定结果
     */
    private String aoiResult;

    /**
     * QC判定结果
     */
    private String qcResult;

    /**
     * QC判定时间
     */

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp qcTime;

    /**
     * QC用户编号
     */
    private String qcUserCode;

    /**
     * QC用户名
     */
    private String qcUserName;

    /**
     * AOI机台号
     */
    private String machineCode;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;
    /**
     * 明码
     */
    private String barCode;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getAoiResult() {
        return aoiResult;
    }

    public void setAoiResult(String aoiResult) {
        this.aoiResult = aoiResult;
    }
    public String getQcResult() {
        return qcResult;
    }

    public void setQcResult(String qcResult) {
        this.qcResult = qcResult;
    }
    public Timestamp getQcTime() {
        return qcTime;
    }

    public void setQcTime(Timestamp qcTime) {
        this.qcTime = qcTime;
    }
    public String getQcUserCode() {
        return qcUserCode;
    }

    public void setQcUserCode(String qcUserCode) {
        this.qcUserCode = qcUserCode;
    }
    public String getQcUserName() {
        return qcUserName;
    }

    public void setQcUserName(String qcUserName) {
        this.qcUserName = qcUserName;
    }
    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getDefectId() {
        return defectId;
    }

    public void setDefectId(String defectId) {
        this.defectId = defectId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfAoiDecideLog{" +
                "id=" + id +
                ", defectId='" + defectId + '\'' +
                ", aoiResult='" + aoiResult + '\'' +
                ", qcResult='" + qcResult + '\'' +
                ", qcTime=" + qcTime +
                ", qcUserCode='" + qcUserCode + '\'' +
                ", qcUserName='" + qcUserName + '\'' +
                ", machineCode='" + machineCode + '\'' +
                ", createTime=" + createTime +
                ", barCode='" + barCode + '\'' +
                '}';
    }
}
