package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("df_iqc_material_config")
public class DfIqcMaterialConfig {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String code;
    private String name;
    private String specification;
    private String supplier;
    private String processUsed;
    private String appearance;
    private String size;
    private LocalDateTime createTime;
    private String materialType;

    // 关联字段（非数据库字段）
    @TableField(exist = false)
    private String processDesc;      // 关联工序描述
}