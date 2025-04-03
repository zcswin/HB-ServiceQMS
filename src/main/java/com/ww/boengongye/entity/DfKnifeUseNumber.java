package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 刀具使用次数信息
 * </p>
 *
 * @author guangyao
 * @since 2023-11-13
 */
@Data
@ApiModel("刀具使用次数信息")
public class DfKnifeUseNumber extends Model<DfKnifeUseNumber> {

    private static final long serialVersionUID = 1L;

    /**
     * 刀具使用次数信息id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 工序
     */
    @ApiModelProperty("工序")
    private String process;

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
     * 检测时间
     */
    @ApiModelProperty("检测时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp checkTime;

    /**
     * 信息推送时间戳
     */
    @ApiModelProperty("信息推送时间戳")
    private String pubTime;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @Override
    public String toString() {
        return "DfKnifeUseNumber{" +
                "id=" + id +
                ", process='" + process + '\'' +
                ", machineCode='" + machineCode + '\'' +
                ", macToolIndex=" + macToolIndex +
                ", lifeMax=" + lifeMax +
                ", lifeAlarm=" + lifeAlarm +
                ", lifeAct=" + lifeAct +
                ", checkTime=" + checkTime +
                ", pubTime='" + pubTime + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
