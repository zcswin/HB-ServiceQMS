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
 * 机台状态持续时间记录
 * </p>
 *
 * @author guangyao
 * @since 2023-11-17
 */
@Data
@ApiModel("机台状态持续时间记录")
public class DfSizeMacDuration extends Model<DfSizeMacDuration> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("机台状态持续时间记录id")
    private Integer id;

    /**
     * 工序
     */
    @ApiModelProperty("工序")
    private String process;

    /**
     * 机台
     */
    @ApiModelProperty("机台")
    private String machineCode;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer status;

    /**
     * 更新后的状态
     */
    @ApiModelProperty("更新后的状态")
    private Integer afterStatus;

    /**
     * 持续时间
     */
    @ApiModelProperty("持续时间(s)")
    private Integer durationTime;

    /**
     * 检测时间
     */
    @ApiModelProperty("检测时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp checkTime;


    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @Override
    public String toString() {
        return "DfSizeMacDuration{" +
                "id=" + id +
                ", process='" + process + '\'' +
                ", machineCode='" + machineCode + '\'' +
                ", status='" + status + '\'' +
                ", afterStatus='" + afterStatus + '\'' +
                ", durationTime=" + durationTime +
                ", checkTime=" + checkTime +
                ", createTime=" + createTime +
                '}';
    }
}
