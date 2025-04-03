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
 * @since 2025-03-13
 */
@Data
public class DfControlStandardConfigBatch extends Model<DfControlStandardConfigBatch> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 批次号
     */
    private String batchCode;

    /**
     * 项目
     */
    private String project;

    /**
     * 线体
     */
    private String lineBody;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfControlStandardConfigBatch{" +
            "id=" + id +
            ", batchCode=" + batchCode +
            ", project=" + project +
            ", lineBody=" + lineBody +
            ", createTime=" + createTime +
        "}";
    }
}
