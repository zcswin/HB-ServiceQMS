package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 测试_冷热循环+百格
 * </p>
 *
 * @author zhao
 * @since 2022-12-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfTeThermalCycling extends Model<DfTeThermalCycling> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 二维码
     */
    @TableField("QRcode")
    private String QRcode;

    /**
     * 序号
     */
    @TableField("SN")
    private Integer sn;

    /**
     * 位置
     */
    @TableField("Location")
    private String Location;

    /**
     * 先值L
     */
    @TableField("Pre_L")
    private Double preL;

    /**
     * 先值a
     */
    @TableField("Pre_a")
    private Double preA;

    /**
     * 先值b
     */
    @TableField("Pre_b")
    private Double preB;

    /**
     * 后值L
     */
    @TableField("Post_L")
    private Double postL;

    /**
     * 后值a
     */
    @TableField("Post_a")
    private Double postA;

    /**
     * 后值b
     */
    @TableField("Post_b")
    private Double postB;

    /**
     * 比较值L
     */
    @TableField("Shift_L")
    private Double shiftL;

    /**
     * 比较值a
     */
    @TableField("Shift_a")
    private Double shiftA;

    /**
     * 比较值b
     */
    @TableField("Shift_b")
    private Double shiftB;

    /**
     * 比较值E94
     */
    @TableField("Shift_E94")
    private Double shiftE94;

    /**
     * 结果
     */
    private String result;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
