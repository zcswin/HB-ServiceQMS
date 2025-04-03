package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 风压点检表
 * </p>
 *
 * @author guangyao
 * @since 2023-09-18
 */
@Data
@ApiModel("风压点检表")
public class DfWindPressure extends Model<DfWindPressure> {

    private static final long serialVersionUID = 1L;

    /**
     * 风压点检测id
     */
    @ApiModelProperty("风压点检测id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 风压点
     */
    @ApiModelProperty("风压点")
    private String spot;

    /**
     * 厂区
     */
    @ApiModelProperty("厂区")
    private String factoryArea;

    /**
     * 厂别
     */
    @ApiModelProperty("厂别")
    private String factory;

    /**
     * 部门
     */
    @ApiModelProperty("部门")
    private String department;

    /**
     * 下限
     */
    @ApiModelProperty("下限")
    private Double lsl;

    /**
     * 标准值
     */
    @ApiModelProperty("标准值")
    private Double standard;

    /**
     * 上限
     */
    @ApiModelProperty("上限")
    private Double usl;

    /**
     * 检测时间
     */
    @ApiModelProperty("检测时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp checkTime;

    /**
     * 压差值
     */
    @ApiModelProperty("压差值")
    private Double checkValue;

    /**
     * 检测结果
     */
    @ApiModelProperty("检测结果")
    private String checkResult;

    /**
     * 记录人
     */
    @ApiModelProperty("记录人")
    private String checkUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @Override
    public String toString() {
        return "DfWindPressure{" +
                "id=" + id +
                ", spot='" + spot + '\'' +
                ", factoryArea='" + factoryArea + '\'' +
                ", factory='" + factory + '\'' +
                ", department='" + department + '\'' +
                ", lsl=" + lsl +
                ", standard=" + standard +
                ", usl=" + usl +
                ", checkTime=" + checkTime +
                ", checkValue=" + checkValue +
                ", checkResult='" + checkResult + '\'' +
                ", checkUser='" + checkUser + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
