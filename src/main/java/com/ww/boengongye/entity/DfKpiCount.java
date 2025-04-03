package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * KPI计算
 * </p>
 *
 * @author zhao
 * @since 2023-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfKpiCount extends Model<DfKpiCount> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 工厂
     */
    private String factory;

    /**
     * 线体
     */
    private String line;

    /**
     * 项目
     */
    private String project;

    /**
     * 工段
     */
    private String workshop;

    /**
     * 工站
     */
    private String workstation;

    /**
     * 工序
     */
    private String process;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 发起人工号
     */
    private Integer createUserCode;

    /**
     * 稽核率
     */
    private Double checkRate;

    /**
     * OK数量
     */
    private Integer okNum;

    /**
     * NG数量
     */
    private Integer ngNum;

    /**
     * 稽核类别
     */
    private String typeCheck;

    /**
     * 发起时间
     */
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
