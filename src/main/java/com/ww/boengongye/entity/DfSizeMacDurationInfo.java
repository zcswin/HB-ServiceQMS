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
 * 尺寸机台每小时统计记录
 * </p>
 *
 * @author guangyao
 * @since 2023-11-17
 */
@Data
@ApiModel("尺寸机台每小时统计记录")
public class DfSizeMacDurationInfo extends Model<DfSizeMacDurationInfo> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("尺寸机台每小时统计记录id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 工序
     */
    @ApiModelProperty("工序")
    private String process;

    /**
     * 统计时间
     */
    @ApiModelProperty("统计时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp infoTime;

    /**
     * 正常
     */
    @ApiModelProperty("正常")
    private Double normal;

    /**
     * 调机
     */
    @ApiModelProperty("调机")
    private Double debug;

    /**
     * 隔离
     */
    @ApiModelProperty("隔离")
    private Double quarantine;

    /**
     * 开机数
     */
    @ApiModelProperty("开机数")
    private Integer machineCount;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @Override
    public String toString() {
        return "DfSizeMacDurationInfo{" +
                "id=" + id +
                ", process='" + process + '\'' +
                ", infoTime=" + infoTime +
                ", normal=" + normal +
                ", debug=" + debug +
                ", quarantine=" + quarantine +
                ", machineCount=" + machineCount +
                ", createTime=" + createTime +
                '}';
    }
}
