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
 * 丝印-厚度
 * </p>
 *
 * @author zhao
 * @since 2023-09-18
 */
@Data
public class DfAoiSlThick extends Model<DfAoiSlThick> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    /**
     * 检测时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp checkTime;

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
     * 型号
     */
    private String model;

    /**
     * 检测人
     */
    private String userName;

    /**
     * fai点名称
     */
    private String faiName;

    /**
     * 名称
     */
    private String name;

    /**
     * 标准
     */
    private Double standard;

    /**
     * 上线
     */
    private Double up;

    /**
     * 下限
     */
    private Double down;

    /**
     * 隔离上限
     */
    private Double isolationUp;

    /**
     * 隔离下限
     */
    private Double isolationDown;

    /**
     * 状态
     */
    private String status;

    /**
     * 点位
     */
    private String position;

    /**
     * 机台号
     */
    private String machineCode;

    /**
     * 重复发生次数
     */
    private Integer numberOfRepetitions;

    private Double checkValue;

    private String process;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
