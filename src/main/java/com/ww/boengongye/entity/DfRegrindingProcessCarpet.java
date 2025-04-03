package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 返磨工序地毯更换记录
 * </p>
 *
 * @author zhao
 * @since 2023-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@Accessors(chain = true)
@ApiModel("返磨工序地毯更换记录")
public class DfRegrindingProcessCarpet extends Model<DfRegrindingProcessCarpet> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 月份
     */
    @ApiModelProperty("月份")
    private String month;

    /**
     * A班次
     */
    @ApiModelProperty("班次")
    private String classes;

    /**
     * A日期
     */
    @ApiModelProperty("日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp date;

    /**
     * A机台号
     */
    @ApiModelProperty("机台号")
    private String machineId;

    /**
     * A地毯
     */
    @ApiModelProperty("地毯")
    private String carpet;

    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    private String operator;

    /**
     * 确认人
     */
    @ApiModelProperty("确认人")
    private String confirmer;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("工厂id")
    private String factoryId;

    @ApiModelProperty("项目")
    private String project;

    @ApiModelProperty("颜色")
    private String color;

    @ApiModelProperty("加工天数")
    private Integer processDay;


    @ApiModelProperty("磨液浓度")
    private Double LiquidDensity;

    @TableField(exist = false)
    private String ok;

    @TableField(exist = false)
    private String ng;

    @TableField(exist = false)
    private String num;

    @TableField(exist = false)
    private String x;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
