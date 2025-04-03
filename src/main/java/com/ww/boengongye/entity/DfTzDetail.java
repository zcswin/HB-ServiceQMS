package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * TZ测量
 * </p>
 *
 * @author guangyao
 * @since 2023-09-11
 */
@Data
@ApiModel("TZ测量")
public class DfTzDetail extends Model<DfTzDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * TZ测量id
     */
    @ApiModelProperty("TZ测量id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 检测时间
     */
    @ApiModelProperty("检测时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp checkTime;

    /**
     * 条码
     */
    @ApiModelProperty("条码")
    private String barcode;

    /**
     * 批次条形
     */
    @ApiModelProperty("批次条形")
    private String batchBarcode;

    /**
     * 结果
     */
    @ApiModelProperty("结果")
    private String result;

    /**
     * 信道
     */
    @ApiModelProperty("信道")
    private String channel;

    /**
     * 班别
     */
    @ApiModelProperty("班别")
    private String classes;

    /**
     * 站别
     */
    @ApiModelProperty("站别")
    private String stationClass;

    /**
     * 线别
     */
    @ApiModelProperty("线别")
    private String lineBody;

    /**
     * RowID
     */
    @ApiModelProperty("RowID")
    private String rowId;

    /**
     * Pos
     */
    @ApiModelProperty("Pos")
    private Integer pos;

    /**
     * GB
     */
    @ApiModelProperty("GB")
    private String gb;

    /**
     * 颜色
     */
    @ApiModelProperty("颜色")
    private String color;

    /**
     * 厂别
     */
    @ApiModelProperty("厂别")
    private String factory;

    /**
     * 项目
     */
    @ApiModelProperty("项目")
    private String project;

    /**
     * 设备
     */
    @ApiModelProperty("设备")
    private String device;

    /**
     * 机台号
     */
    @ApiModelProperty("机台号")
    private String machineCode;

    /**
     * 检测次数（1：一次，2：重复）
     */
    @ApiModelProperty("检测次数（1：一次，2：重复）")
    private Integer checkType;

    @ApiModelProperty("楼层")
    private String floor;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @Override
    public String toString() {
        return "DfTzDetail{" +
                "id=" + id +
                ", checkTime=" + checkTime +
                ", barcode='" + barcode + '\'' +
                ", batchBarcode='" + batchBarcode + '\'' +
                ", result='" + result + '\'' +
                ", channel='" + channel + '\'' +
                ", classes='" + classes + '\'' +
                ", stationClass='" + stationClass + '\'' +
                ", lineBody='" + lineBody + '\'' +
                ", rowId='" + rowId + '\'' +
                ", pos=" + pos +
                ", gb='" + gb + '\'' +
                ", color='" + color + '\'' +
                ", factory='" + factory + '\'' +
                ", project='" + project + '\'' +
                ", device='" + device + '\'' +
                ", machineCode='" + machineCode + '\'' +
                ", checkType=" + checkType +
                ", floor='" + floor + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
