package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * OBA工厂比较
 * </p>
 *
 * @author guangyao
 * @since 2023-09-04
 */
@Data
@ApiModel("OBA工厂比较表")
public class DfAoiObaCompare extends Model<DfAoiObaCompare> {

    private static final long serialVersionUID = 1L;

    /**
     * OBA记录id
     */
    @ApiModelProperty("OBA记录id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 审核日期
     */
    @ApiModelProperty("审核日期")
    private String checkTime;

    /**
     * 审核周别
     */
    @ApiModelProperty("审核周别")
    private String checkWeek;

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
     * 型号
     */
    @ApiModelProperty("型号")
    private String type;

    /**
     * 颜色
     */
    @ApiModelProperty("颜色")
    private String colour;

    /**
     * 批次号
     */
    @ApiModelProperty("批次号")
    private String batch;

    /**
     * 批量
     */
    @ApiModelProperty("批量")
    private Integer batchNumber;

    /**
     * 批通率
     */
    @ApiModelProperty("批通率")
    @TableField(exist = false)
    private String batchPassPoint;

    /**
     * 抽检数
     */
    @ApiModelProperty("抽检数")
    private Integer checkNumber;

    /**
     * NG
     */
    @ApiModelProperty("NG")
    @TableField("NG")
    private Integer NG;

    /**
     * 良率目标
     */
    @ApiModelProperty("良率目标")
    private String passPointGoal;

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
     * 缺陷名称
     */
    @ApiModelProperty("缺陷名称")
    @TableField(exist = false)
    private String defectName;

    /**
     * 抽检结果
     */
    @ApiModelProperty("抽检结果")
    private String checkResult;

    @Override
    public String toString() {
        return "DfAoiObaCompare{" +
                "id=" + id +
                ", checkTime='" + checkTime + '\'' +
                ", checkWeek='" + checkWeek + '\'' +
                ", factory='" + factory + '\'' +
                ", project='" + project + '\'' +
                ", type='" + type + '\'' +
                ", colour='" + colour + '\'' +
                ", batch='" + batch + '\'' +
                ", batchNumber=" + batchNumber +
                ", batchPassPoint='" + batchPassPoint + '\'' +
                ", checkNumber=" + checkNumber +
                ", NG=" + NG +
                ", passPointGoal='" + passPointGoal + '\'' +
                ", passPoint='" + passPoint + '\'' +
                ", defectPoint='" + defectPoint + '\'' +
                ", defectName='" + defectName + '\'' +
                ", checkResult='" + checkResult + '\'' +
                '}';
    }
}
