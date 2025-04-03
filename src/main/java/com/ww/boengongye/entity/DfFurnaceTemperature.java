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
 * 炉温表
 * </p>
 *
 * @author guangyao
 * @since 2023-10-16
 */
@Data
@ApiModel("炉温表")
public class DfFurnaceTemperature extends Model<DfFurnaceTemperature> {

    private static final long serialVersionUID = 1L;

    /**
     * 炉温id
     */
    @ApiModelProperty("炉温id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 线体
     */
    @ApiModelProperty("线体")
    private String lineBody;

    /**
     * 隧道炉名称
     */
    @ApiModelProperty("隧道炉名称")
    private String name;

    /**
     * 检测值
     */
    @ApiModelProperty("检测值")
    private Double checkValue;

    /**
     * 检测时间
     */
    @ApiModelProperty("检测时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp checkTime;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp createTime;

    @Override
    public String toString() {
        return "DfFurnaceTemperature{" +
            "id=" + id +
            ", lineBody=" + lineBody +
            ", name=" + name +
            ", checkValue=" + checkValue +
            ", checkTime=" + checkTime +
            ", createTime=" + createTime +
        "}";
    }
}
