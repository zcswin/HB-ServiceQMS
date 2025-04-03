package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 平台粗糙度
 * </p>
 *
 * @author zhao
 * @since 2022-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfTePlateauRoughness extends Model<DfTePlateauRoughness> {

    private static final long serialVersionUID = 1L;

    /**
     * 供应商
     */
    private String vendor;

    /**
     * 地点
     */
    private String site;

    /**
     * 项目
     */
    private String project;

    /**
     * 描述
     */
    private String phase;

    /**
     * 颜色
     */
    private String color;

    /**
     * 测试名称
     */
    private String testName;

    /**
     * 工艺配方
     */
    private String processRecipe;

    /**
     * 工艺夹具
     */
    private Integer processFixture;

    /**
     * 测量装置
     */
    private String measurementDevice;

    /**
     * 此批id
     */
    private Integer partId;

    /**
     * 测试地点
     */
    private Integer testLocation;

    /**
     * Logo Step height
     */
    private String logoStepHeight;

    /**
     * Logo edge crispness
     */
    private String logoEdgeCrispness;

    /**
     * Spec(Sq)标准
     */
    private String spec;

    /**
     * Sq(Spec<2)
     */
    private Double sq;

    /**
     * Ssk
     */
    private Double ssk;

    /**
     * Sku
     */
    private Double sku;

    /**
     * Sal (Spec>3)
     */
    private Double sal;

    /**
     * Sk (Not filtered)
     */
    private Double sk;

    /**
     * Spk (Not filtered)
     */
    private Double spk;

    /**
     * Svk (Not filtered)
     */
    private Double svk;

    /**
     * Smr1 (Not filtered)
     */
    private Double smr1;

    /**
     * Smr2 (Not filtered)
     */
    private Double smr2;

    /**
     * Sdq(Spec<0.75)
     */
    private Double sdq;

    /**
     * Sdr
     */
    private Double sdr;

    /**
     * Ssc(Spec<1.75)
     */
    private Double ssc;

    /**
     * Length1
     */
    private Double length1;

    /**
     * Length2
     */
    private Double length2;

    /**
     * number_of_motifs1
     */
    private Double numberOfMotifs1;

    /**
     * Height[Mean]1
     */
    private Double heightMean1;

    /**
     * Area[Mean]1
     */
    private Double areaMean1;

    /**
     * Pitch[Mean]1
     */
    private Double pitchMean1;

    /**
     * Mean diameter[Mean]1
     */
    private Double meanDiameterMean1;

    /**
     * Number of motifs2
     */
    private Double numberOfMotifs2;

    /**
     * Height[Mean]2
     */
    private Double heightMean2;

    /**
     * Area[Mean]2
     */
    private Double areaMean2;

    /**
     * Pitch[Mean]2
     */
    private Double pitchMean2;

    /**
     * Mean diameter[Mean]2
     */
    private Double meanDiameterMean2;

    /**
     * Pass/Fail
     */
    private String result;

    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
