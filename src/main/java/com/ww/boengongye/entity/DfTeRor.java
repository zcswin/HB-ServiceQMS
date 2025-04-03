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
 * ror测试
 * </p>
 *
 * @author zhao
 * @since 2022-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfTeRor extends Model<DfTeRor> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 检测单号
     */
    private String testCode;

    /**
     * 送检日期
     */
    private LocalDateTime submitDate;

    /**
     * 机台号
     */
    private String machineCode;

    /**
     * 操作员
     */
    private String operator;

    /**
     * 测试日期
     */
    private LocalDateTime testDate;

    /**
     * 样本号
     */
    @TableField("Sample_id")
    private Integer sampleId;

    /**
     * 二维码
     */
    @TableField("SN2D_code")
    private String sn2dCode;

    /**
     * SF
     */
    @TableField("SF")
    private Double sf;

    /**
     * Instron机器编号
     */
    @TableField("InstronMachineCode")
    private String InstronMachineCode;

    /**
     * 弯曲位移[mm]
     */
    @TableField("Displacement")
    private Double Displacement;

    /**
     * 压缩载荷[N]
     */
    @TableField("BreakingLoad")
    private Double BreakingLoad;

    /**
     * 弯曲应力[Mpa]
     */
    @TableField("BreakingStress")
    private Double BreakingStress;

    /**
     * 厚度[mm]
     */
    @TableField("ActualThickness")
    private Double ActualThickness;

    /**
     * 表面应力1[Mpa]
     */
    @TableField("CS1")
    private Double cs1;

    /**
     * 电子交换深度1[µm]
     */
    @TableField("DOL1")
    private Double dol1;

    /**
     * 表面应力2[Mpa]
     */
    @TableField("CS2")
    private Double cs2;

    /**
     * 电子交换深度2[µm]
     */
    @TableField("DOL2")
    private Double dol2;

    /**
     * 中心应力值[Mpa]
     */
    @TableField("CT")
    private Double ct;

    /**
     * 朝向
     */
    @TableField("FaceUp")
    private String FaceUp;

    /**
     * 结果
     */
    private String result;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
