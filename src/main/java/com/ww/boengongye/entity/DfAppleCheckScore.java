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
 * APPLE每月稽查得分表
 * </p>
 *
 * @author guangyao
 * @since 2023-10-23
 */
@Data
@ApiModel("APPLE每月稽查得分表")
public class DfAppleCheckScore extends Model<DfAppleCheckScore> {

    private static final long serialVersionUID = 1L;

    /**
     * APPLE每月稽查得分Id
     */
    @ApiModelProperty("APPLE每月稽查得分Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 类型
     */
    @ApiModelProperty("APPLE每月稽查得分Id")
    private String dataType;

    /**
     * 年份
     */
    @ApiModelProperty("年份")
    private Integer year;

    /**
     * 月份
     */
    @ApiModelProperty("月份")
    private Integer month;


    @ApiModelProperty("检测时间")
    private String checkTime;

    /**
     * 得分
     */
    @ApiModelProperty("APPLE每月稽查得分Id")
    private Double score;

    /**
     * 创建时间
     */
    @ApiModelProperty("APPLE每月稽查得分Id")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;


    @Override
    public String toString() {
        return "DfAppleCheckScore{" +
                "id=" + id +
                ", dataType='" + dataType + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", checkTime='" + checkTime + '\'' +
                ", score=" + score +
                ", createTime=" + createTime +
                '}';
    }
}
