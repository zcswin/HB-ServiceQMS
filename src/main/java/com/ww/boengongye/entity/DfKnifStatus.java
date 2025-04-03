package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 * 砂轮刀具状态
 * </p>
 *
 * @author zhao
 * @since 2023-09-21
 */
@Data
public class DfKnifStatus extends Model<DfKnifStatus> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    private String typeData;

    /**
     * 0检测 1更换新刀 2刀具结束使用
     */
    private String eventType;

    /**
     * 机台号
     */
    private String machineCode;

    /**
     * 主轴刀号
     */
    private String nNumTool;

    /**
     * 刀具切削时间
     */
    private Integer secDuration;

    /**
     * 刀具刀柄编号
     */
    private String toolCode;

    /**
     * 刀具规格编号
     */
    private String toolSpecCode;

    /**
     * 刀具测量报告结果
     */
    private String toolCheckResult;

    /**
     * 事件时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp dtEvent;

    /**
     * 工序
     */
    private String process;

    /**
     * 发布时间
     */
    private Integer pubTime;

    private Integer toolCutNum;

    @TableField(exist = false)
    private String checkTime;

    @TableField(exist = false)
    private String shiftName;

    @TableField(exist = false)
    private String Tester;

    @TableField(exist = false)
    private String checkDevCode;

    @TableField(exist = false)
    private String macCode;

    @TableField(exist = false)
    private List<NumTool> detail;

    @TableField(exist = false)
    private String code;

    @TableField(exist = false)
    private String userCount;

    @TableField(exist = false)
    private String oldToolCode;

    @TableField(exist = false)
    private String subTime;

    @TableField(exist = false)
    private String count;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
