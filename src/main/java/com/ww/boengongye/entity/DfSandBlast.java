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
 * 喷砂记录表
 * </p>
 *
 * @author zhao
 * @since 2023-08-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@Accessors(chain = true)
@ApiModel("喷砂记录表")
public class DfSandBlast extends Model<DfSandBlast> {

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
     * 机台号
     */
    @ApiModelProperty("机台号")
    private String machineId;

    /**
     * 型号
     */
    @ApiModelProperty("型号")
    private String model;

    /**
     * 砂型
     */
    @ApiModelProperty("砂型")
    private String sandMould;

    /**
     * 砂编号
     */
    @ApiModelProperty("砂编号")
    private String sandId;

    /**
     * RM喷砂-速度
     */
    @ApiModelProperty("RM喷砂-速度")
    private Double speed;

    /**
     * RM喷砂-排砂量
     */
    @ApiModelProperty("RM喷砂-排砂水量")
    private Double sandWaterDischarge;

    /**
     * RM喷砂-加水量
     */
    @ApiModelProperty("RM喷砂-加水量")
    private Double waterAddition;

    /**
     * RM喷砂-加砂量-m2
     */
    @ApiModelProperty("RM喷砂-加砂量-m2")
    private Double sandAdditionM2;

    /**
     * RM喷砂-加砂量-m3
     */
    @ApiModelProperty("RM喷砂-加砂量-m3")
    private Double sandAdditionM3;

    /**
     * RM喷砂-M2浓度-加前
     */
    @ApiModelProperty("RM喷砂-M2浓度-加前")
    private Double m2Before;

    /**
     * RM喷砂-M2浓度-加后
     */
    @ApiModelProperty("RM喷砂-M2浓度-加后")
    private Double m2After;

    /**
     * RM喷砂-M3浓度-加前
     */
    @ApiModelProperty("RM喷砂-M3浓度-加前")
    private Double m3Before;

    /**
     * RM喷砂-M3浓度-加后
     */
    @ApiModelProperty("RM喷砂-M3浓度-加后")
    private Double m3After;

    /**
     * M02单元-p21
     */
    @ApiModelProperty("M02单元-p21")
    private Double m02P21;

    /**
     * M02单元-p22
     */
    @ApiModelProperty("M02单元-p22")
    private Double m02P22;

    /**
     * M02单元-p23
     */
    @ApiModelProperty("M02单元-p23")
    private Double m02P23;

    /**
     * M02单元-p24
     */
    @ApiModelProperty("M02单元-p24")
    private Double m02P24;

    /**
     * M02单元-p31
     */
    @ApiModelProperty("M02单元-p31")
    private Double m02P31;

    /**
     * M02单元-p32
     */
    @ApiModelProperty("M02单元-p32")
    private Double m02P32;

    /**
     * M02单元-p33
     */
    @ApiModelProperty("M02单元-p33")
    private Double m02P33;

    /**
     * M02单元-p34
     */
    @ApiModelProperty("M02单元-p34")
    private Double m02P34;

    /**
     * 手动流量m2
     */
    @ApiModelProperty("手动流量m2")
    private Double m2;

    /**
     * 手动流量m3
     */
    @ApiModelProperty("手动流量m3")
    private Double m3;

    /**
     * 记录人
     */
    @ApiModelProperty("记录人")
    private String recorder;

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
    private String ok;

    @TableField(exist = false)
    private String additionNum;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
