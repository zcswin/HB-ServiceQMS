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
 * @since 2024-02-21
 */
@Data
public class DfDynamicIpqcSize extends Model<DfDynamicIpqcSize> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 机台号
     */
    private String machineCode;

    /**
     * 下限
     */
    private Double lsl;

    /**
     * 上限
     */
    private Double usl;

    /**
     * 标准
     */
    private Double standard;

    /**
     * 西格玛值
     */
    private Double sigma;

    /**
     * 关联尺寸的id
     */
    private Integer checkId;


    /**
     * 编号
     */
    private Integer number;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    /**
     * 测量项名称
     */
    private String itemName;
}
