package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @since 2025-01-05
 */
@Data
public class DfDynamicIpqcAppearanceLog extends Model<DfDynamicIpqcAppearanceLog> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 机台号
     */
    private String machineCode;

    /**
     * 工序
     */
    private String process;

    /**
     * 项目
     */
    private String project;

    /**
     * 状态
     */
    private String status;

    /**
     * 持续时长(秒)
     */
    private Integer durationTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp startDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp endDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    /**
     * 状态颜色
     */
    private String statusColor;

    @Override
    public String toString() {
        return "DfDynamicIpqcAppearanceLog{" +
            "id=" + id +
            ", machineCode=" + machineCode +
            ", process=" + process +
            ", project=" + project +
            ", status=" + status +
            ", durationTime=" + durationTime +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", createTime=" + createTime +
            ", statusColor=" + statusColor +
        "}";
    }
}
