package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 尺寸看板汇总表--尺寸良率汇总
 * </p>
 *
 * @author zhao
 * @since 2023-08-15
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DfSizeOkRate extends Model<DfSizeOkRate> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 工序
     */
    private String process;

    /**
     * OK个数
     */
    private Integer okNum;

    /**
     * 工序测量总数
     */
    private Integer allNum;

    /**
     * OK率
     */
    private Double okRate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp date;

    /**
     * 项目
     */
    private String project;

    /**
     * 项目
     */
    private String color;
}
