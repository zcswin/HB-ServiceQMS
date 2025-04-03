package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2023-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("外观标准")
public class DfAppearanceStandard extends Model<DfAppearanceStandard> {

    private static final long serialVersionUID = 1L;
    @TableId(value="id",type= IdType.AUTO)
    private Integer id;

    /**
     * 型号
     */
    @ApiModelProperty("型号")
    private String model;

    /**
     * 规格等级
     */
    @ApiModelProperty("规格等级")
    private String level;

    /**
     * 项目
     */
    @ApiModelProperty("项目")
    private String project;

    /**
     * 类别
     */
    @ApiModelProperty("类别")
    private String category;

    /**
     * 区域
     */
    @ApiModelProperty("区域")
    private String area;

    /**
     * 名称
     */@ApiModelProperty("名称")

    private String name;

    /**
     * 定义
     */
    @ApiModelProperty("定义")
    private String definition;

    /**
     * 检验方法
     */
    @ApiModelProperty("检验方法")
    private String testMethod;

    /**
     * 允收标准
     */
    @ApiModelProperty("允收标准")
    private String standard;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    private String realPath;

    private String version;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfAppearanceStandard{" +
            "id=" + id +
            ", model=" + model +
            ", level=" + level +
            ", project=" + project +
            ", category=" + category +
            ", area=" + area +
            ", name=" + name +
            ", definition=" + definition +
            ", testMethod=" + testMethod +
            ", standard=" + standard +
        "}";
    }
}
