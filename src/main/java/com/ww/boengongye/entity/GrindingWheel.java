package com.ww.boengongye.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 砂轮mq报文对象
 */
@Data
@ApiModel("砂轮mq报文对象")
public class GrindingWheel {

    /**
     * 数据code
     */
    @ApiModelProperty("数据code")
    private String typeData;

    /**
     * 刀具二维码编号
     */
    @ApiModelProperty("刀具二维码编号")
    private String toolCode;

    /**
     * 设备编号
     */
    @ApiModelProperty("设备编号")
    private String machineCode;

    /**
     * 机床刀号
     */
    @ApiModelProperty("机床刀号")
    private Integer macToolIndex;

    /**
     * 刀具状态
     */
    @ApiModelProperty("刀具状态")
    private String dtType;

    /**
     * 刀具最大寿命
     */
    @ApiModelProperty("刀具最大寿命")
    private Integer lifeMax;

    /**
     * 刀具寿命告警次数
     */
    @ApiModelProperty("刀具寿命告警次数")
    private Integer lifeAlarm;

    /**
     * 刀具目前使用次数
     */
    @ApiModelProperty("刀具目前使用次数")
    private Integer lifeAct;

    /**
     * 推送时间
     */
    @ApiModelProperty("推送时间")
    private Timestamp dtOp;

    /**
     * 推送时间戳
     */
    @ApiModelProperty("推送时间戳")
    private String pubTime;

    @Override
    public String toString() {
        return "GrindingWheel{" +
                "typeData='" + typeData + '\'' +
                ", toolCode='" + toolCode + '\'' +
                ", machineCode='" + machineCode + '\'' +
                ", macToolIndex=" + macToolIndex +
                ", dtType='" + dtType + '\'' +
                ", lifeMax=" + lifeMax +
                ", lifeAlarm=" + lifeAlarm +
                ", lifeAct=" + lifeAct +
                ", dtOp=" + dtOp +
                ", pubTime='" + pubTime + '\'' +
                '}';
    }
}
