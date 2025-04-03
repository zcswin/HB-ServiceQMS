package com.ww.boengongye.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *  AOI机台全检
 */
@Data
@ApiModel("AOI机台全检")
public class DfAoiMachineInspection {
    /**
     * 机台名
     */
    @ApiModelProperty("机台号")
    private String machineCode;

    /**
     * 员工名
     */
    @ApiModelProperty("员工名")
    private String userName;

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
     * 综合投入
     */
    @ApiModelProperty("综合投入")
    private Integer synthesizeInput;

    /**
     * 综合产出
     */
    @ApiModelProperty("综合产出")
    private Integer synthesizeOutput;

    /**
     *  综合良率
     */
    @ApiModelProperty("综合良率")
    private String synthesizePassPoint;


    @Override
    public String toString() {
        return "DfAoiMachineInspection{" +
                "machineCode='" + machineCode + '\'' +
                ", userName='" + userName + '\'' +
                ", projectName='" + projectName + '\'' +
                ", colour='" + colour + '\'' +
                ", synthesizeInput='" + synthesizeInput + '\'' +
                ", synthesizeOutput='" + synthesizeOutput + '\'' +
                ", synthesizePassPoint='" + synthesizePassPoint + '\'' +
                '}';
    }
}
