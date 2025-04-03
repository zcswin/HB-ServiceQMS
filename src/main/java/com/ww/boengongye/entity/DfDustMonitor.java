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
 * 落尘监控状况表
 * </p>
 *
 * @author guangyao
 * @since 2023-09-19
 */
@Data
@ApiModel("落尘监控状况表")
public class DfDustMonitor extends Model<DfDustMonitor> {

    private static final long serialVersionUID = 1L;

    /**
     * 落尘监控状况id
     */
    @ApiModelProperty("落尘监控状况id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 厂别
     */
    @ApiModelProperty("厂别")
    private String factory;

    /**
     * 标准值（≤）
     */
    @ApiModelProperty("标准值（≤）")
    private Double standard;

    /**
     * 位置
     */
    @ApiModelProperty("位置")
    private String location;

    /**
     * 检测时间
     */
    @ApiModelProperty("检测时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp checkTime;

    /**
     * 检测值
     */
    @ApiModelProperty("检测值")
    private Double checkValue;

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
    public String toString() {
        return "DfDustMonitor{" +
                "id=" + id +
                ", factory='" + factory + '\'' +
                ", standard=" + standard +
                ", location='" + location + '\'' +
                ", checkTime=" + checkTime +
                ", checkValue=" + checkValue +
                ", checkResult='" + checkResult + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
