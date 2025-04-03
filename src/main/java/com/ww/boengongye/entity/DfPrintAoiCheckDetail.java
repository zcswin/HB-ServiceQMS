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
 * 移印AOI检测明细
 * </p>
 *
 * @author guangyao
 * @since 2023-09-13
 */
@Data
@ApiModel("移印AOI检测明细")
public class DfPrintAoiCheckDetail extends Model<DfPrintAoiCheckDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * 移印AOI检测明细id
     */
    @ApiModelProperty("移印AOI检测明细id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 检测父类id
     */
    @ApiModelProperty("检测父类id")
    private Integer checkId;

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
     * 检测结果
     */
    @ApiModelProperty("检测结果")
    private String checkResult;

    /**
     * 检测名称（原）
     */
    @ApiModelProperty("检测名称（原）")
    private String checkName;

    /**
     * 检测名称
     */
    @ApiModelProperty("检测名称")
    private String itemName;

    /**
     * 下限
     */
    @ApiModelProperty("下限")
    private Double lsl;

    /**
     * 上限
     */
    @ApiModelProperty("上限")
    private Double usl;

    /**
     * 类型（1：外观 2：尺寸）
     */
    @ApiModelProperty("类型（1：外观 2：尺寸）")
    private Integer checkType;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @Override
    public String toString() {
        return "DfPrintAoiCheckDetail{" +
                "id=" + id +
                ", checkId=" + checkId +
                ", checkTime=" + checkTime +
                ", checkValue=" + checkValue +
                ", checkResult='" + checkResult + '\'' +
                ", checkName='" + checkName + '\'' +
                ", itemName='" + itemName + '\'' +
                ", lsl=" + lsl +
                ", usl=" + usl +
                ", checkType=" + checkType +
                ", createTime=" + createTime +
                '}';
    }
}
