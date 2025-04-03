package com.ww.boengongye.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * AOI良率
 */
@Data
@ApiModel("AOI良率表")
public class DfAoiPassPoint {

    /**
     * 项目名
     */
    @ApiModelProperty("项目名")
    private String projectName;

    /**
     * 颜色
     */
    @ApiModelProperty("颜色")
    private String colour;

    /**
     * 总投入数量
     */
    @ApiModelProperty("总投入数量")
    private Integer totalInput;

    /**
     * 返投数
     */
    @ApiModelProperty("返投数")
    private Integer backNumber;


    /**
     * 返投OK数
     */
    @ApiModelProperty("返投OK数")
    private Integer backOKNumber;

    /**
     * 综合投入数量
     */
    @ApiModelProperty("综合投入数量")
    private Integer  synthesizeInput;

    /**
     * 一次合格的数量
     */
    @ApiModelProperty("一次合格的数量")
    private Integer oneOutput;

    /**
     * 一次良率
     */
    @ApiModelProperty("一次良率")
    private String onePassPoint;


    /**
     * 综合产出
     */
    @ApiModelProperty("综合产出")
    private Integer synthesizeOutput;

    /**
     *  综合合格率
     */
    @ApiModelProperty("综合合格率")
    private String synthesizePassPoint;

    /**
     * 当前时间
     */
    @ApiModelProperty("当前时间")
    private String currentTime;

    /**
     * 单个项目颜色一周的良率
     */
    @ApiModelProperty("单个项目颜色一周的良率集合")
    private Map<String,DfAoiPassPoint> dfAoiPassPointMap;

    @Override
    public String toString() {
        return "DfAoiPassPoint{" +
                "oneOutput=" + oneOutput +
                ", onePassPoint='" + onePassPoint + '\'' +
                ", synthesizeInput=" + synthesizeInput +
                ", synthesizeOutput=" + synthesizeOutput +
                ", synthesizePassPoint='" + synthesizePassPoint + '\'' +
                '}';
    }
}
