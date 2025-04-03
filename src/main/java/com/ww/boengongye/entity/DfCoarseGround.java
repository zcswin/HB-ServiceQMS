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
 * 粗磨,磨皮
 * </p>
 *
 * @author zhao
 * @since 2023-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@Accessors(chain = true)
@ApiModel("粗磨,磨皮")
public class DfCoarseGround extends Model<DfCoarseGround> {

    private static final long serialVersionUID = 1L;

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
    private Integer useTime;

    /**
     * 日期
     */
    @ApiModelProperty("日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    /**
     * 更换时间
     */
    @ApiModelProperty("更换时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp replaceTime;

    /**
     * 类型: 粗磨 || 磨皮
     */
    @ApiModelProperty("类型: 粗磨 || 磨皮")
    private String type;

    /**
     * 项目
     */
    @ApiModelProperty("项目")
    private String project;

    @ApiModelProperty("工序")
    private String process;

    /**
     * 颜色
     */
    @ApiModelProperty("颜色")
    private String color;

    /**
     * 机台号
     */
    @ApiModelProperty("机台号")
    private String machineId;

    @ApiModelProperty("厚度")
    private Double thick;

    @ApiModelProperty("线体")
    private String lineBody;

    @ApiModelProperty("水")
    private Double water;

    @ApiModelProperty("研磨液")
    private Double liquid;

    @TableField(exist = false)
    private String date;

    @TableField(exist = false)
    private String num;

    @TableField(exist = false)
    private String ok;

    @TableField(exist = false)
    private String ng;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfCoarseGround{" +
            "id=" + id +
            ", factoryId=" + factoryId +
            ", classes=" + classes +
            ", useTime=" + useTime +
            ", createTime=" + createTime +
            ", replaceTime=" + replaceTime +
            ", type=" + type +
            ", project=" + project +
            ", color=" + color +
            ", machineId=" + machineId +
        "}";
    }
}
