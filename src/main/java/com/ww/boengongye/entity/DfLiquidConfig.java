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
 * 液抛参数记录表
 * </p>
 *
 * @author zhao
 * @since 2023-08-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@Accessors(chain = true)
@ApiModel("液抛参数记录表")
public class DfLiquidConfig extends Model<DfLiquidConfig> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 工厂id
     */
    @ApiModelProperty("工厂id")
    private String factoryId;

    /**
     * 班次
     */
    @ApiModelProperty("班次")
    private String classes;

    /**
     * 型号
     */
    @ApiModelProperty("型号(实际指的是项目)")
    private String model;

    /**
     * 材质
     */
    @ApiModelProperty("材质")
    private String material;

    /**
     * 投料时间
     */
    @ApiModelProperty("投料时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Timestamp feedTime;

    /**
     * 出料时间
     */
    @ApiModelProperty("出料时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Timestamp dischargeTime;

    /**
     * 作业员
     */
    @ApiModelProperty("作业员")
    private String workUser;

    /**
     * 生产数量(投入数)
     */
    @ApiModelProperty("生产数量(投入数)")
    private String productionQuantity;

    /**
     * 药水槽体编号
     */
    @ApiModelProperty("药水槽体编号")
    private String potionTankCode;

    /**
     * 加工时间
     */
    @ApiModelProperty("加工时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Timestamp processTime;

    /**
     * 温度(120+-5度)
     */
    @ApiModelProperty("温度(120+-5度)")
    private String temperature;

    /**
     * 批次(小于等于14)
     */
    @ApiModelProperty("批次(小于等于14)")
    private String batch;

    /**
     * 下丝量
     */
    @ApiModelProperty("下丝量")
    private String silkNumber;

    /**
     * config信息
     */
    @ApiModelProperty("config信息")
    private String configMessage;

    @ApiModelProperty("颜色")
    private String color;


    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Timestamp createTime;

    @TableField(exist = false)
    private String date;

    @TableField(exist = false)
    private String equal;

    @TableField(exist = false)
    private String less;

    @TableField(exist = false)
    private String count;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
