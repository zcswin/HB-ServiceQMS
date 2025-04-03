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
 * 车间维护
 * </p>
 *
 * @author guangyao
 * @since 2023-08-03
 */
@Data
@ApiModel("车间维护表")
public class DfAoiCarProtect extends Model<DfAoiCarProtect> {

    private static final long serialVersionUID = 1L;

    /**
     * 车间维护id
     */
    @ApiModelProperty("车间维护id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 工厂id
     */
    @ApiModelProperty("工厂id")
    private Integer factoryId;

    /**
     * 车间名称
     */
    @ApiModelProperty("车间名称")
    private String workshop;

    /**
     * 车间识别码
     */
    @ApiModelProperty("车间识别码")
    private String workshopCode;

    /**
     * 工厂名称
     */
    @ApiModelProperty("工厂名称")
    @TableField(exist = false)
    private String factoryName;

    /**
     * 车间识别码
     */
    @ApiModelProperty("车间识别码")
    @TableField(exist = false)
    private String factoryCode;

    /**
     * 所属园区
     */
    @ApiModelProperty("所属园区")
    private String park;


    /**
     * 事业群
     */
    @ApiModelProperty("事业群")
    private String crowd;

    /**
     * 项目分类
     */
    @ApiModelProperty("项目分类")
    private String category;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;

    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createName;

    /**
     * 最新修改日期
     */
    @ApiModelProperty("最新修改日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateName;

    @Override
    public String toString() {
        return "DfAoiCarProtect{" +
                "id=" + id +
                ", factoryId=" + factoryId +
                ", workshop='" + workshop + '\'' +
                ", workshopCode='" + workshopCode + '\'' +
                ", factoryName='" + factoryName + '\'' +
                ", factoryCode='" + factoryCode + '\'' +
                ", park='" + park + '\'' +
                ", crowd='" + crowd + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                ", createName='" + createName + '\'' +
                ", updateTime=" + updateTime +
                ", updateName='" + updateName + '\'' +
                '}';
    }
}
