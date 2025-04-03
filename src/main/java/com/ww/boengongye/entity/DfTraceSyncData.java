package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * trace大数据平台同步数据表
 * </p>
 *
 * @author guangyao
 * @since 2024-04-22
 */
@Data
@ApiModel("trace大数据平台")
public class DfTraceSyncData extends Model<DfTraceSyncData> {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识，无特殊含义
     */
    @ApiModelProperty("trace大数据唯一表示")
    private String id;

    /**
     * 产品追溯编码
     */
    @ApiModelProperty("产品追溯编码")
    private String code;

    /**
     * 白片码
     */
    @ApiModelProperty("白片码")
    private String serialNumber;

    /**
     * 后段码
     */
    @ApiModelProperty("后段码")
    private String fullSerialNumber;

    /**
     * 工厂码.trace定义的工厂唯一标识
     */
    @ApiModelProperty("工厂码")
    private String ppp;

    /**
     * trace内定义的当前产品的工程码
     */
    @ApiModelProperty("trace内定义的当前产品的工程码")
    private String eeee;

    /**
     * trace日期码，trace定义的产品白片段镭码日期编码
     */
    @ApiModelProperty("trace日期码")
    private String traceDateCode;

    /**
     * 工序码
     */
    @ApiModelProperty("工序码")
    private String processCode;

    /**
     * 工站码
     */
    @ApiModelProperty("工站码")
    private String stationCode;

    /**
     * 生产事件
     */
    @ApiModelProperty("生产事件")
    private String event;

    /**
     * log息（json）原始信息
     */
    @ApiModelProperty("log息（json）原始信息")
    private String logs;

    /**
     * 开始时间，该产品在trace工站操作开始的时间
     */
    @ApiModelProperty("开始时间")
    private String startTime;

    /**
     * 结束时间，该产品在trace站操作结束的时间
     */
    @ApiModelProperty("结束时间")
    private String endTime;

    /**
     * 创建时间，这条数据存入大数据平台的时间
     */
    @ApiModelProperty("创建时间")
    private String createdTime;

    /**
     * 入库时间
     */
    @ApiModelProperty("trace大数据唯一表示")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp localTime;

    private String machineId;

    @Override
    public String toString() {
        return "DfTraceSyncData{" +
            "id=" + id +
            ", code=" + code +
            ", serialNumber=" + serialNumber +
            ", fullSerialNumber=" + fullSerialNumber +
            ", ppp=" + ppp +
            ", eeee=" + eeee +
            ", traceDateCode=" + traceDateCode +
            ", processCode=" + processCode +
            ", stationCode=" + stationCode +
            ", event=" + event +
            ", logs=" + logs +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            ", createdTime=" + createdTime +
            ", localTime=" + localTime +
        "}";
    }
}
