package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * QA-BOM
 * </p>
 *
 * @author zhao
 * @since 2023-08-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfQaBom extends Model<DfQaBom> {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 项目型号
     */
    @ApiModelProperty("项目型号")
    private String projectModel;

    /**
     * 阶段
     */
    @ApiModelProperty("阶段")
    private String stage;

    /**
     * config
     */
    @ApiModelProperty("config")
    private String config;

    /**
     * 确认状态
     */
    @ApiModelProperty("确认状态")
    private String confirmStatus;

    /**
     * 流程编号
     */
    @ApiModelProperty("流程编号")
    private String flowId;

    /**
     * 颜色
     */
    @ApiModelProperty("颜色")
    private String color;

    /**
     * qa-bom
     */
    @ApiModelProperty("qa-bom")
    private String qaBom;

    /**
     * 图纸下放
     */
    @ApiModelProperty("图纸下放")
    private String lowerDrawings;

    /**
     * 图纸转换
     */
    @ApiModelProperty("图纸转换")
    private String convertDrawings;

    /**
     * 外部DFM
     */
    @ApiModelProperty("外部DFM")
    private String outerDfm;

    /**
     * 内部DFM
     */
    @ApiModelProperty("内部DFM")
    private String innerDfm;

    /**
     * 外部QCP
     */
    @ApiModelProperty("外部QCP")
    private String outerQcp;

    /**
     * 内部QCP-尺寸
     */
    @ApiModelProperty("内部QCP-尺寸")
    private String innerQcpSize;

    /**
     * 内部QCP-外观
     */
    @ApiModelProperty("内部QCP-外观")
    private String innerQcpAppearance;

    /**
     * 物料-BOM
     */
    @ApiModelProperty("物料-BOM")
    private String materialBom;

    /**
     * ERS
     */
    @ApiModelProperty("ERS")
    private String ers;

    /**
     * 过程MIL
     */
    @ApiModelProperty("过程MIL")
    private String processMil;

    /**
     * 工厂审核
     */
    @ApiModelProperty("工厂审核")
    private String factoryCheck;

    /**
     * 生产单号
     */
    @ApiModelProperty("生产单号")
    private String manufacturingOrder;

    /**
     * 首件确认
     */
    @ApiModelProperty("首件确认")
    private String firstReport;

    /**
     * 首件报告
     */
    @ApiModelProperty("首件报告")
    private String firstConfirm;

    /**
     * 路径
     */
    @ApiModelProperty("路径")
    private String realPath;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    private String version;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfQaBom{" +
            "id=" + id +
            ", projectModel=" + projectModel +
            ", stage=" + stage +
            ", config=" + config +
            ", confirmStatus=" + confirmStatus +
            ", flowId=" + flowId +
            ", color=" + color +
            ", qaBom=" + qaBom +
            ", lowerDrawings=" + lowerDrawings +
            ", convertDrawings=" + convertDrawings +
            ", outerDfm=" + outerDfm +
            ", innerDfm=" + innerDfm +
            ", outerQcp=" + outerQcp +
            ", innerQcpSize=" + innerQcpSize +
            ", innerQcpAppearance=" + innerQcpAppearance +
            ", materialBom=" + materialBom +
            ", ers=" + ers +
            ", processMil=" + processMil +
            ", factoryCheck=" + factoryCheck +
            ", manufacturingOrder=" + manufacturingOrder +
            ", firstReport=" + firstReport +
            ", firstConfirm=" + firstConfirm +
        "}";
    }
}
