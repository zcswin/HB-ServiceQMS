package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2025-01-12
 */
@Data
@ApiModel("天准诊断-尺寸明细")
public class DfSizeCheckItemInfosCheck extends Model<DfSizeCheckItemInfosCheck> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String checkResult;

    private String checkTime;

    private Double checkValue;

    private String comDirection;

    private Double compRatio;

    private Double compValue;

    private Double controlLowerLimit;

    private Double controlUpperLimit;

    private String dCode;

    private Double floatValue;

    private Integer focusType;

    private String itemName;

    private Double lsl;

    private String remark;

    private String repairType;

    private String sn;

    private Double standardValue;

    private String toolCode;

    private String toolFlag;

    private Double trendValue;

    private Double usl;

    /**
     * 不良情况
     */
    private String badCondition;

    /**
     * 关联的父id
     */
    private Integer checkId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    /**
     * 是否重点0否1是
     */
    private String keyPoint;





    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfSizeCheckItemInfosCheck{" +
            "id=" + id +
            ", checkResult=" + checkResult +
            ", checkTime=" + checkTime +
            ", checkValue=" + checkValue +
            ", comDirection=" + comDirection +
            ", compRatio=" + compRatio +
            ", compValue=" + compValue +
            ", controlLowerLimit=" + controlLowerLimit +
            ", controlUpperLimit=" + controlUpperLimit +
            ", dCode=" + dCode +
            ", floatValue=" + floatValue +
            ", focusType=" + focusType +
            ", itemName=" + itemName +
            ", lsl=" + lsl +
            ", remark=" + remark +
            ", repairType=" + repairType +
            ", sn=" + sn +
            ", standardValue=" + standardValue +
            ", toolCode=" + toolCode +
            ", toolFlag=" + toolFlag +
            ", trendValue=" + trendValue +
            ", usl=" + usl +
            ", badCondition=" + badCondition +
            ", checkId=" + checkId +
            ", createTime=" + createTime +
            ", keyPoint=" + keyPoint +
        "}";
    }
}
