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
 * 图纸变更
 * </p>
 *
 * @author zhao
 * @since 2023-08-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)

public class DfDrawingChange extends Model<DfDrawingChange> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 型号
     */
    @ApiModelProperty("型号")
    private String model;

    /**
     * 生产阶段
     */
    @ApiModelProperty("生产阶段")
    private String productionStage;

    /**
     * 流程单号
     */
    @ApiModelProperty("流程单号")
    private String flowId;

    /**
     * 类别
     */
    @ApiModelProperty("类别")
    private String category;

    /**
     * 绘图室
     */
    @ApiModelProperty("绘图室")
    private String drawingRoom;

    /**
     * 工厂文档
     */
    @ApiModelProperty("工厂文控")
    private String factoryDocumentControl;

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
     * BOM
     */
    @ApiModelProperty("BOM")
    private String bom;

    /**
     * ERS
     */
    @ApiModelProperty("ERS")
    private String ers;

    /**
     * 尺寸变更清单
     */
    @ApiModelProperty("尺寸变更清单")
    private String sizeChangeList;

    /**
     * 外部QCP
     */
    @ApiModelProperty("外部QCP")
    private String outerQcp;

    /**
     * 变更范围(记录白片/成品)
     */
    @ApiModelProperty("变更范围(记录白片/成品)")
    private String changeArea;

    /**
     * 客户图纸名称
     */
    @ApiModelProperty("客户图纸名称")
    private String customerDrawingName;

    /**
     * 内部图纸名称
     */
    @ApiModelProperty("内部图纸名称")
    private String innerDrawingName;

    /**
     * 菲林图纸名称
     */
    @ApiModelProperty("菲林图纸名称")
    private String feiDrawingName;

    /**
     * 变更时间
     */
    @ApiModelProperty("变更时间")
    private String changeDate;

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
        return "DfDrawingChange{" +
            "id=" + id +
            ", model=" + model +
            ", productionStage=" + productionStage +
            ", flowId=" + flowId +
            ", category=" + category +
            ", drawingRoom=" + drawingRoom +
            ", factoryDocumentControl=" + factoryDocumentControl +
            ", outerDfm=" + outerDfm +
            ", innerDfm=" + innerDfm +
            ", bom=" + bom +
            ", ers=" + ers +
            ", sizeChangeList=" + sizeChangeList +
            ", outerQcp=" + outerQcp +
            ", changeArea=" + changeArea +
            ", customerDrawingName=" + customerDrawingName +
            ", innerDrawingName=" + innerDrawingName +
            ", feiDrawingName=" + feiDrawingName +
            ", changeDate=" + changeDate +
        "}";
    }
}
