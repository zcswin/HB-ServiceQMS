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
 * @since 2024-09-13
 */
@Data
public class DfQmsTimeOverCheckLog extends Model<DfQmsTimeOverCheckLog> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 机台号
     */
    private String machineCode;

    /**
     * 测试人员
     */
    private String testMan;

    /**
     * 项目
     */
    private String project;

    /**
     * 工序
     */
    private String process;

    /**
     * 下机时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp finishTime;

    /**
     * 抽检时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfQmsTimeOverCheckLog{" +
            "id=" + id +
            ", machineCode=" + machineCode +
            ", testMan=" + testMan +
            ", project=" + project +
            ", process=" + process +
            ", finishTime=" + finishTime +
            ", createTime=" + createTime +
        "}";
    }
}
