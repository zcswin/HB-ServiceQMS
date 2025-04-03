package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * BC-linker表
 * </p>
 *
 * @author guangyao
 * @since 2023-09-09
 */
@Data
@ApiModel("BC-linker表")
public class DfBcLinker extends Model<DfBcLinker> {

    private static final long serialVersionUID = 1L;

    /**
     * BC-linkerId
     */
    @ApiModelProperty("BC-linkerId")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * RowID
     */
    @ApiModelProperty("RowID")
    private String rowId;

    /**
     * 条码
     */
    @ApiModelProperty("条码")
    private String barcode;

    /**
     * 暗码
     */
    @ApiModelProperty("暗码")
    private String cipher;

    /**
     * 工厂
     */
    @ApiModelProperty("工厂")
    private String factory;

    /**
     * 线体
     */
    @ApiModelProperty("线体")
    private String lineBody;

    /**
     * 检测时间
     */
    @ApiModelProperty("检测时间")
    private String checkTime;

    /**
     * 项目
     */
    @ApiModelProperty("项目")
    private String project;

    /**
     * 颜色
     */
    @ApiModelProperty("颜色")
    private String colour;

    /**
     * 设备
     */
    @ApiModelProperty("设备")
    private String device;

    /**
     * 结果
     */
    @ApiModelProperty("结果")
    private String result;

    /**
     * 镭码结果
     */
    @ApiModelProperty("镭码结果")
    private String radiumResult;

    /**
     * Trace卡站
     */
    @ApiModelProperty("Trace卡站")
    private String traceCard;

    /**
     * 卡站率
     */
    @ApiModelProperty("卡站率")
    @TableField(exist = false)
    private String traceCardPoint;

    /**
     * 获取2D结果
     */
    @ApiModelProperty("获取2D结果")
    @TableField("get2D_result")
    private String get2DResult;

    /**
     * 暗码结果
     */
    @ApiModelProperty("暗码结果/读不出码")
    private String cipherResult;

    /**
     * 读不出码率
     */
    @ApiModelProperty("暗码率/读不出码率")
    @TableField(exist = false)
    private String cipherPoint;

    /**
     * 分料料仓
     */
    @ApiModelProperty("分料料仓")
    private String silo;

    /**
     * 网络校验结果
     */
    @ApiModelProperty("网络校验结果")
    private String networkResult;

    /**
     * 回捞ISM结果
     */
    @ApiModelProperty("回捞ISM结果")
    private String ismResult;

    /**
     * 位置
     */
    @ApiModelProperty("位置")
    private String position;

    /**
     * 检查数量
     */
    @ApiModelProperty("检查数量")
    @TableField(exist = false)
    private Integer checkNumber;

    /**
     * 不良数量
     */
    @ApiModelProperty("不良数量")
    @TableField(exist = false)
    private Integer defectNumber;

    /**
     * 不良名称
     */
    @ApiModelProperty("不良名称")
    @TableField(exist = false)
    private String defectName;

    /**
     * 不良名称(中文)
     */
    @ApiModelProperty("不良名称(中文)")
    @TableField(exist = false)
    private String defectZHName;

    /**
     * 不良率
     */
    @ApiModelProperty("不良率")
    @TableField(exist = false)
    private String defectPoint;

    @Override
    public String toString() {
        return "DfBcLinker{" +
                "id=" + id +
                ", rowId='" + rowId + '\'' +
                ", barcode='" + barcode + '\'' +
                ", cipher='" + cipher + '\'' +
                ", factory='" + factory + '\'' +
                ", lineBody='" + lineBody + '\'' +
                ", checkTime='" + checkTime + '\'' +
                ", project='" + project + '\'' +
                ", colour='" + colour + '\'' +
                ", device='" + device + '\'' +
                ", result='" + result + '\'' +
                ", radiumResult='" + radiumResult + '\'' +
                ", traceCard='" + traceCard + '\'' +
                ", traceCardPoint='" + traceCardPoint + '\'' +
                ", get2DResult='" + get2DResult + '\'' +
                ", cipherResult='" + cipherResult + '\'' +
                ", cipherPoint='" + cipherPoint + '\'' +
                ", silo='" + silo + '\'' +
                ", networkResult='" + networkResult + '\'' +
                ", ismResult='" + ismResult + '\'' +
                ", position='" + position + '\'' +
                ", checkNumber=" + checkNumber +
                ", defectNumber=" + defectNumber +
                ", defectName='" + defectName + '\'' +
                ", defectZHName='" + defectZHName + '\'' +
                ", defectPoint='" + defectPoint + '\'' +
                '}';
    }
}
