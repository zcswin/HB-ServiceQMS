package com.ww.boengongye.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 判定信息
 */
@Data
@ApiModel("判定信息")
public class DfAoiDetermine {

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String projectName;

    /**
     * AOI机台名称
     */
    @ApiModelProperty("AOI机台名称")
    private String machineName;

    /**
     * 颜色
     */
    @ApiModelProperty("颜色")
    private String colour;

    /**
     * 已过AOI次数
     */
    @ApiModelProperty("已过AOI次数")
    private String aoiNumber;

    /**
     * AOI最新判定结果
     */
    @ApiModelProperty("AOI最新判定结果")
    private String newResult;

    /**
     * IA复判结果
     */
    @ApiModelProperty("IA复判结果")
    private String reResult;

    /**
     * 生产周期
     */
    @ApiModelProperty("生产周期")
    private String productWeek;

    /**
     * 玻璃框架id
     */
    @ApiModelProperty("玻璃框架id")
    private String frameid;

    /**
     * 父id
     */
    private Integer checkId;

    @Override
    public String toString() {
        return "DfAoiDetermine{" +
                "projectName='" + projectName + '\'' +
                ", machineName='" + machineName + '\'' +
                ", colour='" + colour + '\'' +
                ", aoiNumber='" + aoiNumber + '\'' +
                ", newResult='" + newResult + '\'' +
                ", reResult='" + reResult + '\'' +
                ", productWeek='" + productWeek + '\'' +
                ", frameid='" + frameid + '\'' +
                '}';
    }
}
