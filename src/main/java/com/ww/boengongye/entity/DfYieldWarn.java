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
 *
 * </p>
 *
 * @author zhao
 * @since 2023-09-06
 */
@Data
@ApiModel("工序良率配置表")
public class DfYieldWarn extends Model<DfYieldWarn> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("创建时间")
    private Integer id;

    /**
     * 工序
     */
    @ApiModelProperty("工序")
    private String process;

    /**
     * 外观/尺寸
     */
    @ApiModelProperty("外观/尺寸")
    private String type;

    /**
     * 预警值
     */
    @ApiModelProperty("预警值")
    private Double prewarningValue;

    /**
     * 报警值
     */
    @ApiModelProperty("报警值")
    private Double alarmValue;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;

    @Override
    public String toString() {
        return "DfYieldWarn{" +
                "id=" + id +
                ", process='" + process + '\'' +
                ", type='" + type + '\'' +
                ", prewarningValue=" + prewarningValue +
                ", alarmValue=" + alarmValue +
                ", name='" + name + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
