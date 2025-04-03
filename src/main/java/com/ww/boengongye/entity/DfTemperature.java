package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 温度表
 * </p>
 *
 * @author zhao
 * @since 2023-10-18
 */
@Data
@ApiModel("温度表")
public class DfTemperature extends Model<DfTemperature> {

    private static final long serialVersionUID = 1L;

    /**
     * 温度id
     */
        @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 厂区
     */
    @ApiModelProperty("厂区")
    private String factoryArea;

    /**
     * 厂别
     */
    @ApiModelProperty("厂区")
    private String factory;

    /**
     * 检测时间
     */
    @ApiModelProperty("检测时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp checkTime;

    /**
     * 检测位置
     */
    @ApiModelProperty("检测位置")
    private String checkPosition;

    /**
     * 记录人
     */
    @ApiModelProperty("记录人")
    private String recordUser;

    /**
     * 确定人
     */
    @ApiModelProperty("确定人")
    private String determineUser;

    /**
     * 下限
     */
    @ApiModelProperty("下限")
    private Double lsl;

    /**
     * 检测值
     */
    @ApiModelProperty("检测值")
    private Double checkValue;

    /**
     * 上限
     */
    @ApiModelProperty("上限")
    private Double usl;

    /**
     * 检测结果
     */
    @ApiModelProperty("检测结果")
    private String checkResult;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
