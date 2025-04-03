package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
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
 * @since 2025-01-09
 */
@ApiModel("LEADCheck明细")
@Data
public class DfLeadCheckItemInfosCheck extends Model<DfLeadCheckItemInfosCheck> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 检测结果
     */
    @SerializedName("CheckResult")
    private String checkResult;

    /**
     * 检测时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp checkTime;

    /**
     * 检测值
     */
    @SerializedName("CheckValue")
    private Double checkValue;

    /**
     * 检测名称
     */
    @SerializedName("ItemName")
    private String itemName;

    /**
     * 下限
     */
    @SerializedName("LSL")
    private Double lsl;

    /**
     * 标准值
     */
    @SerializedName("StandardValue")
    private Double standardValue;

    /**
     * 上限
     */
    @SerializedName("USL")
    private Double usl;

    /**
     * 关联的父id
     */
    private Integer checkId;

    /**
     * 1尺寸  2倒角  3外观
     */
    private Integer checkType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfLeadCheckItemInfos{" +
                "id=" + id +
                ", checkResult=" + checkResult +
                ", checkTime=" + checkTime +
                ", checkValue=" + checkValue +
                ", itemName=" + itemName +
                ", lsl=" + lsl +
                ", standardValue=" + standardValue +
                ", usl=" + usl +
                ", checkId=" + checkId +
                ", checkType=" + checkType +
                ", createTime=" + createTime +
                "}";
    }
}
