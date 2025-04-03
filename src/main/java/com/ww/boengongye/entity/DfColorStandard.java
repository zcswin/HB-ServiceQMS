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
 * @since 2023-08-22
 */
@ApiModel("颜色标准")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfColorStandard extends Model<DfColorStandard> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 型号
     */
    @ApiModelProperty("型号")
    private String model;

    /**
     * 颜色
     */
    @ApiModelProperty("颜色")
    private String color;

    /**
     * 测量模式
     */
    @ApiModelProperty("测量模式")
    private String testMode;

    /**
     * p2蒙砂区规格
     */
    @ApiModelProperty("p2蒙砂区规格")
    private String p2SandArea;

    /**
     * evt蒙砂区规格
     */
    @ApiModelProperty("evt蒙砂区规格")
    private String evtSandArea;

    /**
     * p2光泽度规格
     */
    @ApiModelProperty("p2光泽度规格")
    private String p2Gloss;

    /**
     * evt光泽度规格
     */
    @ApiModelProperty("evt光泽度规格")
    private String evtGloss;

    /**
     * p2备注
     */
    @ApiModelProperty("p2备注")
    private String p2Remarks;

    /**
     * evt备注
     */
    @ApiModelProperty("evt备注")
    private String evtRemarks;

    /**
     * 路径
     */
    @ApiModelProperty("路径")
    private String realPath;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @ApiModelProperty("版本号")
    private String version;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfColorStandard{" +
            "id=" + id +
            ", model=" + model +
            ", color=" + color +
            ", testMode=" + testMode +
            ", p2SandArea=" + p2SandArea +
            ", evtSandArea=" + evtSandArea +
            ", p2Gloss=" + p2Gloss +
            ", evtGloss=" + evtGloss +
            ", p2Remarks=" + p2Remarks +
            ", evtRemarks=" + evtRemarks +
        "}";
    }
}
