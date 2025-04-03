package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.sql.Timestamp;

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
 * @since 2022-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfMacPositionDetectResult extends Model<DfMacPositionDetectResult> {

    public static final long serialVersionUID = 1L;

    @TableId("ID")
    public String id;

    /**
     * SN
     */
    @TableField("SN")
    public String sn;

    /**
     * 产品编号
     */
    @TableField("ProductCode")
    public String ProductCode;

    /**
     * 检测时间
     */
    @TableField("CheckTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp CheckTime;

    /**
     * 检测结果
     */
    @TableField("CheckResult")
    public String CheckResult;

    /**
     * 机床编号
     */
    @TableField("MachineCode")
    public String MachineCode;

    /**
     * 检验设备编号
     */
    @TableField("CheckEquCode")
    public String CheckEquCode;

    @TableField("ProcessNO")
    public String ProcessNO;

    /**
     * 检验人员
     */
    @TableField("Inspector")
    public String Inspector;

    @TableField("CreateTime")
    public Timestamp CreateTime;

    @TableField("Remark")
    public String Remark;

    /**
     * 检测类型 1抽检 2调机
     */
    @TableField("CheckType")
    public Integer CheckType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfMacPositionDetectResult() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public Timestamp getCheckTime() {
        return CheckTime;
    }

    public void setCheckTime(Timestamp checkTime) {
        CheckTime = checkTime;
    }

    public String getCheckResult() {
        return CheckResult;
    }

    public void setCheckResult(String checkResult) {
        CheckResult = checkResult;
    }

    public String getMachineCode() {
        return MachineCode;
    }

    public void setMachineCode(String machineCode) {
        MachineCode = machineCode;
    }

    public String getCheckEquCode() {
        return CheckEquCode;
    }

    public void setCheckEquCode(String checkEquCode) {
        CheckEquCode = checkEquCode;
    }

    public String getProcessNO() {
        return ProcessNO;
    }

    public void setProcessNO(String processNO) {
        ProcessNO = processNO;
    }

    public String getInspector() {
        return Inspector;
    }

    public void setInspector(String inspector) {
        Inspector = inspector;
    }

    public Timestamp getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Timestamp createTime) {
        CreateTime = createTime;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public Integer getCheckType() {
        return CheckType;
    }

    public void setCheckType(Integer checkType) {
        CheckType = checkType;
    }
}
