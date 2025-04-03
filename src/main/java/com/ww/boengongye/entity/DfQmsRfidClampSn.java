package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2024-06-24
 */
@Data
public class DfQmsRfidClampSn extends Model<DfQmsRfidClampSn> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 玻璃码
     */
    private String barCode;

    /**
     * 载具码
     */
    private String clampCode;

    /**
     * 生产时间
     */
    private String productTime;

    /**
     * 工序
     */
    private String process;

    private LocalDateTime createTime;

    @TableField(exist = false)
    private String isWg;
    @TableField(exist = false)
    private String isSize;
    @TableField(exist = false)
    private String wgTime;
    @TableField(exist = false)
    private String sizeTime;



}
