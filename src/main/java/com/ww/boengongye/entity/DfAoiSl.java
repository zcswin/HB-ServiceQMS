package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2023-09-10
 */
@ApiModel("丝印")
@Data
@ToString
public class DfAoiSl extends Model<DfAoiSl> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 录入时间
     */
    @JsonFormat(pattern="yy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp createtime;

    /**
     * 类型(单印LOGO,一层BG,一层LOGO,二层BG,二层LOGO...)
     */
    private String type;

    /**
     * 日期
     */
    @JsonFormat(pattern="MM-dd",timezone = "GMT+8")
    private Timestamp date;
    /**
     * 班别
     */
    private String clazz;
    /**
     * 时间
     */
    @JsonFormat(pattern="mm:ss",timezone = "GMT+8")
    private Timestamp time;
    /**
     * 阶段
     */
    private String stage;
    /**
     * 首件/巡检
     */
    private String firstOrPoi;
    /**
     * 群组特性
     */
    private String groupCharacteristics;
    /**
     * lf2
     */
    private BigDecimal lf2;
    /**
     * af2
     */
    private BigDecimal af2;
    /**
     * bf2
     */
    private BigDecimal bf2;
    /**
     * dlf2
     */
    private BigDecimal dlf2;
    /**
     * daf2
     */
    private BigDecimal daf2;
    /**
     * daf3
     */
    private BigDecimal daf3;
    /**
     * dbf2
     */
    private BigDecimal dbf2;
    /**
     * de94f2
     */
    private BigDecimal de94f2;
    /**
     * judge
     */
    private String judge;
    /**
     * L上限
     */
    private BigDecimal lUp;
    /**
     * L下限
     */
    private BigDecimal LDown;

    /**
     * A上限
     */
    private BigDecimal aUp;
    /**
     * A下限
     */
    private BigDecimal aDown;

    /**
     * B上限
     */
    private BigDecimal bUp;
    /**
     * B下限
     */
    private BigDecimal bDown;

    private String project;

    private String factory;

    private String process;

    private String lineBody;

    private String model;

    private String color;

    private Integer auditId;

    private Integer numberOfRepetitions;

    private String produceDri;

    private String ngType;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
