package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 移印AOI人工复查
 * </p>
 *
 * @author guangyao
 * @since 2023-09-13
 */
@Data
@ApiModel("移印AOI人工复查")
public class DfPrintAoiRecheck extends Model<DfPrintAoiRecheck> {

    private static final long serialVersionUID = 1L;

    /**
     * 移印AOI人工复查id
     */
    @ApiModelProperty("移印AOI人工复查id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 工厂
     */
    @ApiModelProperty("工厂")
    private String factory;

    /**
     * 项目
     */
    @ApiModelProperty("项目")
    private String project;

    /**
     * 线体
     */
    @ApiModelProperty("线体")
    private String lineBody;

    /**
     * 颜色
     */
    @ApiModelProperty("颜色")
    private String colour;

    /**
     * 日期
     */
    @ApiModelProperty("日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp checkTime;

    /**
     * 类型1
     */
    @ApiModelProperty("类型1")
    private String type1;

    /**
     * 类型2
     */
    @ApiModelProperty("类型2")
    private String type2;

    /**
     * 投入数据量
     */
    @ApiModelProperty("投入数据量")
    private Integer inputNumber;

    /**
     * 人工复判OK数量
     */
    @ApiModelProperty("人工复判OK数量")
    @TableField("recheck_OK_number")
    private Integer recheckOkNumber;

    /**
     * 人工复判NG数量
     */
    @ApiModelProperty("人工复判NG数量")
    @TableField("recheck_NG_number")
    private Integer recheckNgNumber;

    /**
     * 过杀率或者漏检率
     */
    @ApiModelProperty("过杀率或者漏检率")
    private String overkillOrEscape;

    /**
     * 一次良率
     */
    @ApiModelProperty("一次良率")
    private String onePassPoint;

    /**
     * 最终良率
     */
    @ApiModelProperty("最终良率")
    private String finalPassPoint;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @Override
    public String toString() {
        return "DfPrintAoiRecheck{" +
            "id=" + id +
            ", factory=" + factory +
            ", project=" + project +
            ", lineBody=" + lineBody +
            ", colour=" + colour +
            ", checkTime=" + checkTime +
            ", type1=" + type1 +
            ", type2=" + type2 +
            ", inputNumber=" + inputNumber +
            ", recheckOkNumber=" + recheckOkNumber +
            ", recheckNgNumber=" + recheckNgNumber +
            ", overkillOrEscape=" + overkillOrEscape +
            ", onePassPoint=" + onePassPoint +
            ", finalPassPoint=" + finalPassPoint +
            ", createTime=" + createTime +
        "}";
    }
}
