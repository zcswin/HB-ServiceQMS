package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 移印AOI检测
 * </p>
 *
 * @author guangyao
 * @since 2023-09-13
 */
@Data
@ApiModel("移印AOI检测")
public class DfPrintAoiCheck extends Model<DfPrintAoiCheck> {

    private static final long serialVersionUID = 1L;

    /**
     * 移印AOI检测id
     */
    @ApiModelProperty("移印AOI检测id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 检测时间
     */
    @ApiModelProperty("检测时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp checkTime;

    /**
     * 项目
     */
    @ApiModelProperty("项目")
    private String project;

    /**
     * 工厂
     */
    @ApiModelProperty("工厂")
    private String factory;

    /**
     * 颜色
     */
    @ApiModelProperty("颜色")
    private String colour;

    /**
     * 线体
     */
    @ApiModelProperty("线体")
    private String lineBody;

    /**
     * VirBarcode
     */
    @ApiModelProperty("VirBarcode")
    private String virBarcode;

    /**
     * QRcode
     */
    @ApiModelProperty("QRcode")
    private String qrCode;

    /**
     * 机台
     */
    @ApiModelProperty("机台")
    private String printMachine;

    /**
     * 孔
     */
    @ApiModelProperty("孔")
    private Integer hole;

    /**
     * 检测结果
     */
    @ApiModelProperty("检测结果")
    private String checkResult;

    /**
     * Bin
     */
    @ApiModelProperty("Bin")
    private String bin;

    /**
     * FailItem
     */
    @ApiModelProperty("FailItem")
    private String failItem;

    /**
     * Ct
     */
    @ApiModelProperty("移印AOI检测id")
    private Double ct;

    /**
     * 检测次数（1：一次；2：重复）
     */
    @ApiModelProperty("检测次数（1：一次；2：重复）")
    private Integer checkType;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    /**
     * 一次良率
     */
    @ApiModelProperty("一次良率")
    @TableField(exist = false)
    private String onePassPoint;

    /**
     * 尺寸良率
     */
    @ApiModelProperty("尺寸良率")
    @TableField(exist = false)
    private String dimensionPassPoint;

    /**
     * 外观良率
     */
    @ApiModelProperty("外观良率")
    @TableField(exist = false)
    private String cosmeticPassPoint;

    /**
     * 最终良率
     */
    @ApiModelProperty("最终良率")
    @TableField(exist = false)
    private String finalPassPoint;

    @Override
    public String toString() {
        return "DfPrintAoiCheck{" +
                "id=" + id +
                ", checkTime=" + checkTime +
                ", project='" + project + '\'' +
                ", factory='" + factory + '\'' +
                ", colour='" + colour + '\'' +
                ", lineBody='" + lineBody + '\'' +
                ", virBarcode='" + virBarcode + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", printMachine='" + printMachine + '\'' +
                ", hole=" + hole +
                ", checkResult='" + checkResult + '\'' +
                ", bin='" + bin + '\'' +
                ", failItem='" + failItem + '\'' +
                ", ct=" + ct +
                ", checkType=" + checkType +
                ", createTime=" + createTime +
                ", onePassPoint='" + onePassPoint + '\'' +
                ", dimensionPassPoint='" + dimensionPassPoint + '\'' +
                ", cosmeticPassPoint='" + cosmeticPassPoint + '\'' +
                ", finalPassPoint='" + finalPassPoint + '\'' +
                '}';
    }
}
