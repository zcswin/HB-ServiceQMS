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
 *
 * </p>
 *
 * @author zhao
 * @since 2023-08-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfProgrammingChange extends Model<DfProgrammingChange> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 型号
     */
    @ApiModelProperty("型号")
    private String model;

    /**
     * 登记时间
     */
    @ApiModelProperty("登记时间")
    private String registTime;

    /**
     * 测试程序版本
     */
    @ApiModelProperty("测试程序版本")
    private String testProgrammingVersion;

    /**
     * 程序变更时间
     */
    @ApiModelProperty("程序变更时间")
    private String programmingChangeTime;

    /**
     * 对应尺寸图纸版本
     */
    @ApiModelProperty("对应尺寸图纸版本")
    private String sizeDrawingVersion;

    /**
     * 对应颜色图纸版本
     */
    @ApiModelProperty("对应颜色图纸版本")
    private String colorDrawingVersion;

    /**
     * 尺寸图纸适用范围
     */
    @ApiModelProperty("尺寸图纸适用范围")
    private String sizeDrawingArea;

    /**
     * 尺寸图纸变更时间
     */
    @ApiModelProperty("尺寸图纸变更时间")
    private String sizeChangeTime;

    /**
     * 程序变更原因
     */
    @ApiModelProperty("程序变更原因")
    private String programmingChangeReason;

    /**
     * 程序变更内容
     */
    @ApiModelProperty("程序变更内容")
    private String programmingChangeContent;

    /**
     * 程序导入时间
     */
    @ApiModelProperty("程序导入时间")
    private String programmingImportTime;

    /**
     * 确认人
     */
    @ApiModelProperty("确认人")
    private String confirmer;

	private String realPath;

	private String version;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfProgrammingChange{" +
            "id=" + id +
            ", model=" + model +
            ", registTime=" + registTime +
            ", testProgrammingVersion=" + testProgrammingVersion +
            ", programmingChangeTime=" + programmingChangeTime +
            ", sizeDrawingVersion=" + sizeDrawingVersion +
            ", colorDrawingVersion=" + colorDrawingVersion +
            ", sizeDrawingArea=" + sizeDrawingArea +
            ", sizeChangeTime=" + sizeChangeTime +
            ", programmingChangeReason=" + programmingChangeReason +
            ", programmingChangeContent=" + programmingChangeContent +
            ", programmingImportTime=" + programmingImportTime +
            ", confirmer=" + confirmer +
        "}";
    }
}
