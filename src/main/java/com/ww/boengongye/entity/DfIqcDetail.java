package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("df_iqc_detail")
public class DfIqcDetail {
    @TableId(type = IdType.AUTO)
    private Long id;
    private LocalDate incomeDate;
    private String materialCode;
    private String materialName;
    private String specification;
    private String supplier;
    private String processUsed;
    private Double samplingQuantity;
    private String appearance;
    private String appearanceNum;
    private String size;
    private Integer sizeNum;
    private String appearanceDefect;
    private String sizeDefect;
    private String ngMethod;
    private String remarks;
    private LocalDateTime createTime;
    private String type;
    private String factory;
    private String materialType;

    // 关联字段（非数据库字段）
    @TableField(exist = false)
    private String factoryName;      // 关联工厂名称
    @TableField(exist = false)
    private String supplierAddress; // 关联供应商地址
}