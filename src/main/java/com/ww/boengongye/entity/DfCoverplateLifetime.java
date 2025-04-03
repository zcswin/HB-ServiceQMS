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
 * 磨底盖板使用寿命管控
 * </p>
 *
 * @author zhao
 * @since 2023-08-07
 */

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@Accessors(chain = true)
@ApiModel("磨底盖板使用寿命管控")
public class DfCoverplateLifetime extends Model<DfCoverplateLifetime> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 厂号
     */
    @ApiModelProperty("厂号")
    private String factoryId;

    /**
     * 班次
     */
    @ApiModelProperty("班次")
    private String classes;

    /**
     * 使用次数
     */
    @ApiModelProperty("使用次数")
    private String useTime;

    /**
     * 日期
     */
    @ApiModelProperty("日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Timestamp createTime;
    @ApiModelProperty("类型(磨底,磨平台)")
    private String type;

    @ApiModelProperty("项目")
    private String project;

    @ApiModelProperty("颜色")
    private String color;

    @ApiModelProperty("机台号")
    private String machineId;

    @TableField(exist = false)
    private String date;

    @TableField(exist = false)
    private String less;

    @TableField(exist = false)
    private String equal;

    @TableField(exist = false)
    private String count;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
