package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 产品信息
 * </p>
 *
 * @author zhao
 * @since 2022-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfProDetail extends Model<DfProDetail> {

    public static final long serialVersionUID = 1L;

    @TableId("ID")
    public String id;

    /**
     * 产品编号
     */
    @TableField("ProductID")
    public String ProductID;

    /**
     * 位置
     */
    @TableField("Position")
    public String Position;

    /**
     * 标准值
     */
    @TableField("StandardValue")
    public Float StandardValue;

    /**
     * 刀库刀具编号
     */
    @TableField("ToolLibraryTypeID")
    public String ToolLibraryTypeID;

    /**
     * 图纸上公差
     */
    @TableField("DrawingUpperLimit")
    public Float DrawingUpperLimit;

    /**
     * 图纸下公差
     */
    @TableField("DrawingLowerLimit")
    public Float DrawingLowerLimit;

    /**
     * 对应工序
     */
    @TableField("CorresProcess")
    public String CorresProcess;

    /**
     * 工艺对应
     */
    @TableField("ProcessCorres")
    public String ProcessCorres;

    /**
     * 程序值
     */
    @TableField("ProgramValue")
    public String ProgramValue;

    /**
     * 补正方式
     */
    @TableField("CorrectionMethod")
    public Integer CorrectionMethod;

    /**
     * 补正方向
     */
    @TableField("CorrectionDirection")
    public Integer CorrectionDirection;

    /**
     * 公差范围上限
     */
    @TableField("ToleranceUpperLimit")
    public Float ToleranceUpperLimit;

    /**
     * 公差范围下限
     */
    @TableField("ToleranceLowerLimit")
    public Float ToleranceLowerLimit;

    /**
     * 内外圆 1：内圆 2：外圆
     */
    @TableField("CircleAttr")
    public Integer CircleAttr;

    @TableField("Remark")
    public String Remark;

    /**
     * 排序号
     */
    @TableField("SortNum")
    public Integer SortNum;

    /**
     * 创建时间
     */
    @TableField("CreateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp CreateTime;

    /**
     * 创建人
     */
    @TableField("CreateUser")
    public String CreateUser;

    @TableField("UpdateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp UpdateTime;
  

    /**
     * 修改人
     */
    @TableField("UpdateUser")
    public String UpdateUser;

    /**
     * 是否删除
     */
    @TableField("IsDelete")
    public Boolean IsDelete;

    /**
     * 是否刀补 1刀补 0不刀补
     */
    @TableField("IsRepair")
    public Integer IsRepair;

    /**
     * H补偿类型 0：正常h刀补 1代表：弧面刀补
     */
    @TableField("HType")
    public Integer HType;

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
     * 程序值
     */
    @TableField("ProgramValueRe")
    public String ProgramValueRe;

    /**
     * 刀补范围上限
     */
    @TableField("ControlUpperLimit")
    public Float ControlUpperLimit;

    /**
     * 刀补范围下限
     */
    @TableField("ControlLowerLimit")
    public Float ControlLowerLimit;

    /**
     * 是否重要尺寸 1重要 0非重要
     */
    @TableField("FocusType")
    public Integer FocusType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfProDetail() {
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

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public Float getStandardValue() {
        return StandardValue;
    }

    public void setStandardValue(Float standardValue) {
        StandardValue = standardValue;
    }

    public String getToolLibraryTypeID() {
        return ToolLibraryTypeID;
    }

    public void setToolLibraryTypeID(String toolLibraryTypeID) {
        ToolLibraryTypeID = toolLibraryTypeID;
    }

    public Float getDrawingUpperLimit() {
        return DrawingUpperLimit;
    }

    public void setDrawingUpperLimit(Float drawingUpperLimit) {
        DrawingUpperLimit = drawingUpperLimit;
    }

    public Float getDrawingLowerLimit() {
        return DrawingLowerLimit;
    }

    public void setDrawingLowerLimit(Float drawingLowerLimit) {
        DrawingLowerLimit = drawingLowerLimit;
    }

    public String getCorresProcess() {
        return CorresProcess;
    }

    public void setCorresProcess(String corresProcess) {
        CorresProcess = corresProcess;
    }

    public String getProcessCorres() {
        return ProcessCorres;
    }

    public void setProcessCorres(String processCorres) {
        ProcessCorres = processCorres;
    }

    public String getProgramValue() {
        return ProgramValue;
    }

    public void setProgramValue(String programValue) {
        ProgramValue = programValue;
    }

    public Integer getCorrectionMethod() {
        return CorrectionMethod;
    }

    public void setCorrectionMethod(Integer correctionMethod) {
        CorrectionMethod = correctionMethod;
    }

    public Integer getCorrectionDirection() {
        return CorrectionDirection;
    }

    public void setCorrectionDirection(Integer correctionDirection) {
        CorrectionDirection = correctionDirection;
    }

    public Float getToleranceUpperLimit() {
        return ToleranceUpperLimit;
    }

    public void setToleranceUpperLimit(Float toleranceUpperLimit) {
        ToleranceUpperLimit = toleranceUpperLimit;
    }

    public Float getToleranceLowerLimit() {
        return ToleranceLowerLimit;
    }

    public void setToleranceLowerLimit(Float toleranceLowerLimit) {
        ToleranceLowerLimit = toleranceLowerLimit;
    }

    public Integer getCircleAttr() {
        return CircleAttr;
    }

    public void setCircleAttr(Integer circleAttr) {
        CircleAttr = circleAttr;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public Integer getSortNum() {
        return SortNum;
    }

    public void setSortNum(Integer sortNum) {
        SortNum = sortNum;
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

    public Boolean getDelete() {
        return IsDelete;
    }

    public void setDelete(Boolean delete) {
        IsDelete = delete;
    }

    public Integer getIsRepair() {
        return IsRepair;
    }

    public void setIsRepair(Integer isRepair) {
        IsRepair = isRepair;
    }

    public Integer getHType() {
        return HType;
    }

    public void setHType(Integer HType) {
        this.HType = HType;
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

    public String getProgramValueRe() {
        return ProgramValueRe;
    }

    public void setProgramValueRe(String programValueRe) {
        ProgramValueRe = programValueRe;
    }

    public Float getControlUpperLimit() {
        return ControlUpperLimit;
    }

    public void setControlUpperLimit(Float controlUpperLimit) {
        ControlUpperLimit = controlUpperLimit;
    }

    public Float getControlLowerLimit() {
        return ControlLowerLimit;
    }

    public void setControlLowerLimit(Float controlLowerLimit) {
        ControlLowerLimit = controlLowerLimit;
    }

    public Integer getFocusType() {
        return FocusType;
    }

    public void setFocusType(Integer focusType) {
        FocusType = focusType;
    }
}
