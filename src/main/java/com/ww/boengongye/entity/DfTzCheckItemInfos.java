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
 * TZ明细表
 * </p>
 *
 * @author guangyao
 * @since 2023-09-11
 */
@Data
@ApiModel("TZ测量明细表")
public class DfTzCheckItemInfos extends Model<DfTzCheckItemInfos> {

    private static final long serialVersionUID = 1L;

    /**
     * TZ明细id
     */
    @ApiModelProperty("TZ测量明细id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 检测结果
     */
    @ApiModelProperty("检测结果")
    private String checkResult;

    /**
     * 检测时间
     */
    @ApiModelProperty("检测时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp checkTime;

    /**
     * 检测值
     */
    @ApiModelProperty("检测值")
    private Double checkValue;

    /**
     * 检测名称(原)
     */
    @ApiModelProperty("检测名称(原)")
    private String checkName;


    /**
     * 检测名称
     */
    @ApiModelProperty("检测名称")
    private String itemName;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String describes;

    /**
     * 下限
     */
    @ApiModelProperty("下限")
    private Double lsl;

    /**
     * 标准值
     */
    @ApiModelProperty("标准值")
    private Double standardValue;

    /**
     * 上限
     */
    @ApiModelProperty("上限")
    private Double usl;

    /**
     * 关联的父id
     */
    @ApiModelProperty("关联的父id")
    private Integer checkId;

    /**
     * 类型（1：尺寸；2：倒角；3：外观）
     */
    @ApiModelProperty("类型（1：尺寸；2：倒角；3：外观）")
    private Integer checkType;

    /**
     * 上公差
     */
    @ApiModelProperty("上公差")
    private Double upperTolerance;

    /**
     * 下公差
     */
    @ApiModelProperty("下公差")
    private Double lowerTolerance;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @Override
    public String toString() {
        return "DfTzCheckItemInfos{" +
            "id=" + id +
            ", checkResult=" + checkResult +
            ", checkTime=" + checkTime +
            ", checkValue=" + checkValue +
            ", itemName=" + itemName +
            ", describes=" + describes +
            ", lsl=" + lsl +
            ", standardValue=" + standardValue +
            ", usl=" + usl +
            ", checkId=" + checkId +
            ", checkType=" + checkType +
            ", upperTolerance=" + upperTolerance +
            ", lowerTolerance=" + lowerTolerance +
            ", createTime=" + createTime +
        "}";
    }
}
