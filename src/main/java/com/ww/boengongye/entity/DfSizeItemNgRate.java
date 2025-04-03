package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 尺寸看板汇总表--尺寸NG TOP
 * </p>
 *
 * @author zhao
 * @since 2023-07-04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DfSizeItemNgRate extends Model<DfSizeItemNgRate> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 工序
     */
    private String process;

    /**
     * 不良项
     */
    private String itemName;

    /**
     * NG个数
     */
    private Integer ngNum;

    /**
     * 工序测量总数
     */
    private Integer allNum;

    /**
     * NG率
     */
    private Double ngRate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    private String dayOrNight;

    /**
     * 项目
     */
    private String project;

    /**
     * 颜色
     */
    private String color;

}
