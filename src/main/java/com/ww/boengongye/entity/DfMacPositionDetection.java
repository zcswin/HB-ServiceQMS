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
 * 设备尺寸检测数据表
 * </p>
 *
 * @author zhao
 * @since 2022-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfMacPositionDetection extends Model<DfMacPositionDetection> {

    public static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId("ID")
    public String id;

    @TableField("DetectResultID")
    public String DetectResultID;

    /**
     * 二维码编码
     */
    @TableField("TwoDimensionCode")
    public String TwoDimensionCode;

    /**
     * 加工设备编号
     */
    @TableField("MachineCode")
    public String MachineCode;

    /**
     * 检验设备编号
     */
    @TableField("CheckEquCode")
    public String CheckEquCode;

    /**
     * 检验人员
     */
    @TableField("Inspector")
    public String Inspector;

    /**
     * 产品
     */
    @TableField("ProductCode")
    public String ProductCode;

    /**
     * 工序
     */
    @TableField("Process")
    public String Process;

    /**
     * 位置
     */
    @TableField("Positon")
    public String Positon;

    /**
     * 标准值
     */
    @TableField("StandardValue")
    public Float StandardValue;

    /**
     * 公差上限
     */
    @TableField("UpperLimit")
    public Float UpperLimit;

    /**
     * 公差下限
     */
    @TableField("LowerLimit")
    public Float LowerLimit;

    /**
     * 检测值
     */
    @TableField("DetectionValue")
    public Float DetectionValue;

    /**
     * 补偿值
     */
    @TableField("CompValue")
    public Float CompValue;

    /**
     * 刀具编号
     */
    @TableField("ToolCode")
    public String ToolCode;

    /**
     * D值编号
     */
    @TableField("DCode")
    public String DCode;

    /**
     * 刀具标识
     */
    @TableField("ToolFlag")
    public String ToolFlag;

    @TableField("CreateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp CreateTime;

    /**
     * 创建用户
     */
    @TableField("CreateUser")
    public String CreateUser;

    @TableField("UpdateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp UpdateTime;

    /**
     * 修改用户
     */
    @TableField("UpdateUser")
    public String UpdateUser;

    @TableField("IsDelete")
    public Integer IsDelete;

    /**
     * 补偿系数
     */
    @TableField("CompRatio")
    public Float CompRatio;

    /**
     * 趋势值
     */
    @TableField("TrendValue")
    public Float TrendValue;

    /**
     * 检测结果
     */
    @TableField("CheckResult")
    public String CheckResult;

    /**
     * 检测类型 1抽检 2调机
     */
    @TableField("CheckType")
    public Integer CheckType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfMacPositionDetection() {
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

    public String getDetectResultID() {
        return DetectResultID;
    }

    public void setDetectResultID(String detectResultID) {
        DetectResultID = detectResultID;
    }

    public String getTwoDimensionCode() {
        return TwoDimensionCode;
    }

    public void setTwoDimensionCode(String twoDimensionCode) {
        TwoDimensionCode = twoDimensionCode;
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

    public String getInspector() {
        return Inspector;
    }

    public void setInspector(String inspector) {
        Inspector = inspector;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public String getProcess() {
        return Process;
    }

    public void setProcess(String process) {
        Process = process;
    }

    public String getPositon() {
        return Positon;
    }

    public void setPositon(String positon) {
        Positon = positon;
    }

    public Float getStandardValue() {
        return StandardValue;
    }

    public void setStandardValue(Float standardValue) {
        StandardValue = standardValue;
    }

    public Float getUpperLimit() {
        return UpperLimit;
    }

    public void setUpperLimit(Float upperLimit) {
        UpperLimit = upperLimit;
    }

    public Float getLowerLimit() {
        return LowerLimit;
    }

    public void setLowerLimit(Float lowerLimit) {
        LowerLimit = lowerLimit;
    }

    public Float getDetectionValue() {
        return DetectionValue;
    }

    public void setDetectionValue(Float detectionValue) {
        DetectionValue = detectionValue;
    }

    public Float getCompValue() {
        return CompValue;
    }

    public void setCompValue(Float compValue) {
        CompValue = compValue;
    }

    public String getToolCode() {
        return ToolCode;
    }

    public void setToolCode(String toolCode) {
        ToolCode = toolCode;
    }

    public String getDCode() {
        return DCode;
    }

    public void setDCode(String DCode) {
        this.DCode = DCode;
    }

    public String getToolFlag() {
        return ToolFlag;
    }

    public void setToolFlag(String toolFlag) {
        ToolFlag = toolFlag;
    }

    public Timestamp getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Timestamp createTime) {
        CreateTime = createTime;
    }

    public String getCreateUser() {
        return CreateUser;
    }

    public void setCreateUser(String createUser) {
        CreateUser = createUser;
    }

    public Timestamp getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        UpdateTime = updateTime;
    }

    public String getUpdateUser() {
        return UpdateUser;
    }

    public void setUpdateUser(String updateUser) {
        UpdateUser = updateUser;
    }

    public Integer getIsDelete() {
        return IsDelete;
    }

    public void setIsDelete(Integer isDelete) {
        IsDelete = isDelete;
    }

    public Float getCompRatio() {
        return CompRatio;
    }

    public void setCompRatio(Float compRatio) {
        CompRatio = compRatio;
    }

    public Float getTrendValue() {
        return TrendValue;
    }

    public void setTrendValue(Float trendValue) {
        TrendValue = trendValue;
    }

    public String getCheckResult() {
        return CheckResult;
    }

    public void setCheckResult(String checkResult) {
        CheckResult = checkResult;
    }

    public Integer getCheckType() {
        return CheckType;
    }

    public void setCheckType(Integer checkType) {
        CheckType = checkType;
    }
}
