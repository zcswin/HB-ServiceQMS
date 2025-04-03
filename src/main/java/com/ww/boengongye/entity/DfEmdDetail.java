package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2023-09-15
 */
@Data
public class DfEmdDetail extends Model<DfEmdDetail> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * SN
     */
    private String sn;

    /**
     * 检测类型 1 一次 2综合
     */
    private Integer checkType;

    /**
     * 机台号
     */
    private String machineCode;

    /**
     * 工厂
     */
    private String factory;

    /**
     * 线体
     */
    private String lineBody;

    /**
     * 项目
     */
    private String project;

    /**
     * 颜色
     */
    private String color;

    /**
     * F或B
     */
    private String workPosition;

    /**
     * 工位结果 - 合格
     */
    private String workResult;

    /**
     * 工位值
     */
    private Double workValue;

    /**
     * 工位形变
     */
    private String workChange;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp checkTime;

    private String machine;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
