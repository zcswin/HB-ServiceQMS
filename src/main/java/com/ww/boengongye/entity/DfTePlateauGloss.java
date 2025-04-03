package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 平台光泽度
 * </p>
 *
 * @author zhao
 * @since 2022-11-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfTePlateauGloss extends Model<DfTePlateauGloss> {

    private static final long serialVersionUID = 1L;

    /**
     * Vendor供应商
     */
    @TableField("Vendor")
    private String Vendor;

    /**
     * Site
     */
    @TableField("Site")
    private String Site;

    /**
     * Project项目
     */
    @TableField("Project")
    private String Project;

    /**
     * Phase描述
     */
    @TableField("Phase")
    private String Phase;

    /**
     * Line
     */
    @TableField("Line")
    private String Line;

    @TableField("Date")
    private LocalDateTime Date;

    /**
     * Shift
     */
    @TableField("Shift")
    private String Shift;

    /**
     * Config
     */
    @TableField("Config")
    private String Config;

    /**
     * Color颜色
     */
    @TableField("Color")
    private String Color;

    /**
     * Test Name测试名称
     */
    @TableField("TestName")
    private String TestName;

    /**
     * Last Process before Measurement工艺
     */
    @TableField("LastProcessbeforeMeasurement")
    private String LastProcessbeforeMeasurement;

    /**
     * Dwell time since Process
     */
    @TableField("DwelltimesinceProcess")
    private String DwelltimesinceProcess;

    /**
     * Key Process Machine Name
     */
    @TableField("KeyProcessMachineName")
    private String KeyProcessMachineName;

    /**
     * Key Process Machine #
     */
    @TableField("KeyProcessMachineID")
    private Integer KeyProcessMachineID;

    /**
     * Process Recipe
     */
    @TableField("ProcessRecipe")
    private String ProcessRecipe;

    /**
     * Process Fixture #
     */
    @TableField("ProcessFixtureID")
    private Integer ProcessFixtureID;

    /**
     * Measurement Device
     */
    @TableField("MeasurementDevice")
    private String MeasurementDevice;

    /**
     * Measurement Device #
     */
    @TableField("MeasurementDeviceID")
    private Integer MeasurementDeviceID;

    /**
     * Part ID
     */
    @TableField("PartID")
    private Integer PartID;

    /**
     * Test Location测量点位
     */
    @TableField("TestLocation")
    private Integer TestLocation;

    /**
     * Measure 1第一次
     */
    @TableField("Measure1")
    private Double Measure1;

    /**
     * Measure 2第二次
     */
    @TableField("Measure2")
    private Double Measure2;

    /**
     * Measure 3第三次
     */
    @TableField("Measure3")
    private Double Measure3;

    /**
     * Measure 4第四次
     */
    @TableField("Measure4")
    private Double Measure4;

    /**
     * Measure 5第五次
     */
    @TableField("Measure5")
    private Double Measure5;

    /**
     * Gloss Average平均值
     */
    @TableField("GlossAverage")
    private Double GlossAverage;

    /**
     * Spec标准
     */
    @TableField("Spec")
    private String Spec;

    /**
     * Failure/Pass
     */
    private String result;

    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
