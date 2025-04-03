package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * AOI SI工厂对比
 * </p>
 *
 * @author guangyao
 * @since 2023-09-07
 */
@Data
@ApiModel("AOI SI工厂对比表")
public class DfAoiSiCompare extends Model<DfAoiSiCompare> {

    private static final long serialVersionUID = 1L;

    /**
     * SI工厂对比id
     */
    @ApiModelProperty("SI工厂对比id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 系列
     */
    @ApiModelProperty("系列")
    private String series;

    /**
     * 日期
     */
    @ApiModelProperty("日期")
    private String checkTime;

    /**
     * 年
     */
    @ApiModelProperty("年")
    private String checkYear;

    /**
     * 月
     */
    @ApiModelProperty("月")
    private String checkMonth;

    /**
     * 周
     */
    @ApiModelProperty("周")
    private String checkWeek;

    /**
     * 工厂
     */
    @ApiModelProperty("工厂")
    private String factory;

    /**
     * SI客户
     */
    @ApiModelProperty("SI客户")
    private String siCustomer;

    /**
     * 出货地区
     */
    @ApiModelProperty("出货地区")
    private String sellPlace;

    /**
     * 型号
     */
    @ApiModelProperty("型号")
    private String type;

    /**
     * 批次号
     */
    @ApiModelProperty("批次号")
    private String batch;

    /**
     * Bin/config
     */
    @ApiModelProperty("Bin/config")
    private String binConfig;

    /**
     * 颜色
     */
    @ApiModelProperty("颜色")
    private String colour;

    /**
     * Bin
     */
    @ApiModelProperty("Bin")
    private String bin;

    /**
     * Vender
     */
    @ApiModelProperty("Vender")
    private String vender;

    /**
     * SI次数
     */
    @ApiModelProperty("SI次数")
    private String siNumber;

    /**
     * 阶段
     */
    @ApiModelProperty("阶段")
    private String stage;

    /**
     * 标杆线
     */
    @ApiModelProperty("标杆线")
    private String benchmarkLine;

    /**
     * AQL
     */
    @ApiModelProperty("AQL")
    private String aql;

    /**
     * 投入数
     */
    @ApiModelProperty("投入数")
    private Integer inputNumber;

    /**
     * 抽检数
     */
    @ApiModelProperty("抽检数")
    private Integer checkNumber;

    /**
     * OK数
     */
    @ApiModelProperty("OK数")
    private Integer okNumber;

    /**
     * NG数
     */
    @ApiModelProperty("NG数")
    private Integer ngNumber;

    /**
     * 良率
     */
    @ApiModelProperty("良率")
    private String passPoint;

    /**
     * 不良率
     */
    @ApiModelProperty("不良率")
    @TableField(exist = false)
    private String defectPoint;

    /**
     * 批通率
     */
    @ApiModelProperty("批通率")
    @TableField(exist = false)
    private String batchPassPoint;

    /**
     * 缺陷名称
     */
    @ApiModelProperty("缺陷名称")
    @TableField(exist = false)
    private String defectName;

    /**
     * Target
     */
    @ApiModelProperty("Target")
    private String target;

    /**
     * 判定
     */
    @ApiModelProperty("判定")
    private String determine;

    @Override
    public String toString() {
        return "DfAoiSiCompare{" +
            "id=" + id +
            ", series=" + series +
            ", checkTime=" + checkTime +
            ", checkYear=" + checkYear +
            ", checkMonth=" + checkMonth +
            ", checkWeek=" + checkWeek +
            ", factory=" + factory +
            ", siCustomer=" + siCustomer +
            ", sellPlace=" + sellPlace +
            ", type=" + type +
            ", batch=" + batch +
            ", binConfig=" + binConfig +
            ", colour=" + colour +
            ", bin=" + bin +
            ", vender=" + vender +
            ", siNumber=" + siNumber +
            ", stage=" + stage +
            ", benchmarkLine=" + benchmarkLine +
            ", aql=" + aql +
            ", inputNumber=" + inputNumber +
            ", checkNumber=" + checkNumber +
            ", okNumber=" + okNumber +
            ", ngNumber=" + ngNumber +
            ", passPoint=" + passPoint +
            ", target=" + target +
            ", determine=" + determine +
        "}";
    }
}
