package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.NumberFormat;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2023-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfSizeCheckItemInfos extends Model<DfSizeCheckItemInfos> {

    public static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;
    @SerializedName("CheckResult")
    public String checkResult;
    @SerializedName("CheckTime")
    public String checkTime;
    @SerializedName("CheckValue")
    public Double checkValue;
    @SerializedName("ComDirection")
    public String comDirection;
    @SerializedName("CompRatio")
    public Double compRatio;
    @SerializedName("CompValue")
    public Double compValue;
    @SerializedName("ControlLowerLimit")
    public Double controlLowerLimit;
    @SerializedName("ControlUpperLimit")
    public Double controlUpperLimit;
    @SerializedName("DCode")
    public String dCode;
    @SerializedName("FloatValue")
    public Double floatValue;
    @SerializedName("FocusType")
    public Integer focusType;
    @SerializedName("ItemName")
    public String itemName;
    @SerializedName("LSL")
    public Double lsl;
    @SerializedName("Remark")
    public String remark;
    @SerializedName("RepairType")
    public String repairType;
    @SerializedName("SN")
    public String sn;
    @SerializedName("StandardValue")
    public Double standardValue;
    @SerializedName("ToolCode")
    public String toolCode;
    @SerializedName("ToolFlag")
    public String toolFlag;
    @SerializedName("TrendValue")
    public Double trendValue;
    @SerializedName("USL")
    public Double usl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    @TableField(exist = false)
    public String process;

    @TableField(exist = false)
    public String project;

    @TableField(exist = false)
    public String color;

    public String keyPoint;

    @ApiModelProperty("检验类型,FAI/CPK/风险")
    public String checkType;


    @TableField(exist = false)
    public Double ngRate;
    @TableField(exist = false)
    public Double okRate;
    @TableField(exist = false)
    public Double result;
    @TableField(exist = false)
    public String machineCode;
    @TableField(exist = false)
    public String ngNum;
    @TableField(exist = false)
    public String sizeTotal;
    @TableField(exist = false)
    public String waigTotal;
    @TableField(exist = false)
    public String monthDay;
    @TableField(exist = false)
    public String sort;

//样本均值
    @TableField(exist = false)
    public String meanValue;
//样本方差
    @TableField(exist = false)
    public String variance;

    //样本标准差
    @TableField(exist = false)
    public String stddev;

    @NumberFormat(pattern = "#.##")
    @TableField(exist = false)
    public Double cpk;
    @NumberFormat(pattern = "#.##")
    @TableField(exist = false)
    public Double ca;




    public Double getOkRate() {
        return okRate;
    }

    public void setOkRate(Double okRate) {
        this.okRate = okRate;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getNgRate() {
        return ngRate;
    }

    public void setNgRate(Double ngRate) {
        this.ngRate = ngRate;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    /**
     * 不良情况
     */
    public String badCondition;

    /**
     * 关联的父id
     */
    @SerializedName("CheckId")
    public String checkId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public DfSizeCheckItemInfos() {
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

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public Double getCheckValue() {
        return checkValue;
    }

    public void setCheckValue(Double checkValue) {
        this.checkValue = checkValue;
    }

    public String getComDirection() {
        return comDirection;
    }

    public void setComDirection(String comDirection) {
        this.comDirection = comDirection;
    }

    public Double getCompRatio() {
        return compRatio;
    }

    public void setCompRatio(Double compRatio) {
        this.compRatio = compRatio;
    }

    public Double getCompValue() {
        return compValue;
    }

    public void setCompValue(Double compValue) {
        this.compValue = compValue;
    }

    public Double getControlLowerLimit() {
        return controlLowerLimit;
    }

    public void setControlLowerLimit(Double controlLowerLimit) {
        this.controlLowerLimit = controlLowerLimit;
    }

    public Double getControlUpperLimit() {
        return controlUpperLimit;
    }

    public void setControlUpperLimit(Double controlUpperLimit) {
        this.controlUpperLimit = controlUpperLimit;
    }

    public String getdCode() {
        return dCode;
    }

    public void setdCode(String dCode) {
        this.dCode = dCode;
    }

    public Double getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(Double floatValue) {
        this.floatValue = floatValue;
    }

    public Integer getFocusType() {
        return focusType;
    }

    public void setFocusType(Integer focusType) {
        this.focusType = focusType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getLsl() {
        return lsl;
    }

    public void setLsl(Double lsl) {
        this.lsl = lsl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRepairType() {
        return repairType;
    }

    public void setRepairType(String repairType) {
        this.repairType = repairType;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Double getStandardValue() {
        return standardValue;
    }

    public void setStandardValue(Double standardValue) {
        this.standardValue = standardValue;
    }

    public String getToolCode() {
        return toolCode;
    }

    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    public String getToolFlag() {
        return toolFlag;
    }

    public void setToolFlag(String toolFlag) {
        this.toolFlag = toolFlag;
    }

    public Double getTrendValue() {
        return trendValue;
    }

    public void setTrendValue(Double trendValue) {
        this.trendValue = trendValue;
    }

    public Double getUsl() {
        return usl;
    }

    public void setUsl(Double usl) {
        this.usl = usl;
    }

    public String getBadCondition() {
        return badCondition;
    }

    public void setBadCondition(String badCondition) {
        this.badCondition = badCondition;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getKeyPoint() {
        return keyPoint;
    }

    public void setKeyPoint(String keyPoint) {
        this.keyPoint = keyPoint;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }
}
