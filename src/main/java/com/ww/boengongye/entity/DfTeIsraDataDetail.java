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
 * 17.4-ISRA Data
 * </p>
 *
 * @author zhao
 * @since 2022-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfTeIsraDataDetail extends Model<DfTeIsraDataDetail> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("OverallYield")
    private String OverallYield;

    /**
     * BG  Waviness Yield(5c9c9d11a)
     */
    @TableField("BGWavinessYield")
    private String BGWavinessYield;

    /**
     * Plateau Cutter mark Yield(8e-8h)
     */
    @TableField("PlateauCuttermarkYield")
    private String PlateauCuttermarkYield;

    /**
     * Others(1a-8a 12a)
     */
    @TableField("Others")
    private String Others;

    @TableField("OverallYield2")
    private String OverallYield2;

    @TableField("BGWavinessYield2")
    private String BGWavinessYield2;

    @TableField("PlateauCuttermarkYield2")
    private String PlateauCuttermarkYield2;

    @TableField("Others2")
    private String Others2;

    @TableField("Barcode")
    private String Barcode;

    @TableField("Slot")
    private String Slot;

    /**
     * Overall OK/NG
     */
    @TableField("OverallResult")
    private String OverallResult;

    @TableField("p_1c_HOOK_TL")
    private Double p1cHookTl;

    @TableField("p_1c_FANG_R")
    private Double p1cFangR;

    @TableField("p_1c_FANG_L")
    private Double p1cFangL;

    @TableField("p_1f_HOOK_SP_TR")
    private Double p1fHookSpTr;

    @TableField("p_1f_HOOK_SP_BR")
    private Double p1fHookSpBr;

    @TableField("p_1f_HOOK_SP_TL")
    private Double p1fHookSpTl;

    @TableField("p_1f_HOOK_SP_ML")
    private Double p1fHookSpMl;

    @TableField("p_1f_HOOK_SP_BL")
    private Double p1fHookSpBl;

    @TableField("p_3a_CORNER_BR")
    private Double p3aCornerBr;

    @TableField("p_3a_CORNER_BL")
    private Double p3aCornerBl;

    @TableField("p_3a_CORNER_TR")
    private Double p3aCornerTr;

    @TableField("p_4a_GLUE_T")
    private Double p4aGlueT;

    @TableField("p_4b_GLUE_B")
    private Double p4bGlueB;

    @TableField("p_5c_WAVINESS_CMAX")
    private Double p5cWavinessCmax;

    @TableField("p_7a_SW_L")
    private Double p7aSwL;

    @TableField("p_7a_SW_R")
    private Double p7aSwR;

    @TableField("p_7b_SW_T")
    private Double p7bSwT;

    @TableField("p_7b_SW_B")
    private Double p7bSwB;

    @TableField("p_8e_PLATEAU_CUTTER_R")
    private Double p8ePlateauCutterR;

    @TableField("p_8f_PLATEAU_CUTTER_B")
    private Double p8fPlateauCutterB;

    @TableField("p_8g_PLATEAU_CUTTER_C")
    private Double p8gPlateauCutterC;

    @TableField("p_8h_PLATEAU_CUTTER_MEAN")
    private Double p8hPlateauCutterMean;

    @TableField("p_8i_PLATEAU_STRAY_CUTTER")
    private Double p8iPlateauStrayCutter;

    @TableField("p_9c_RANDOM_DENTS")
    private Double p9cRandomDents;

    @TableField("p_9d_RANDOM_DENTS")
    private Double p9dRandomDents;

    @TableField("p_11a_CUTTER_POLISH")
    private Double p11aCutterPolish;

    @TableField("p_11b_CUTTER_TR")
    private Double p11bCutterTr;

    @TableField("p_11c_CUTTER_ML")
    private Double p11cCutterMl;

    @TableField("p_11d_CUTTER_MR")
    private Double p11dCutterMr;

    @TableField("p_11e_CUTTER_BL")
    private Double p11eCutterBl;

    @TableField("p_11f_CUTTER_BR")
    private Double p11fCutterBr;

    @TableField("p_11g_POLISH_TR")
    private Double p11gPolishTr;

    @TableField("p_11h_POLISH_ML")
    private Double p11hPolishMl;

    @TableField("p_11i_POLISH_MR")
    private Double p11iPolishMr;

    @TableField("p_11j_POLISH_BL")
    private Double p11jPolishBl;

    @TableField("p_11k_POLISH_BR")
    private Double p11kPolishBr;

    @TableField("p_11l_CUTTER_MEAN")
    private Double p11lCutterMean;

    @TableField("p_11m_POLISH_MEAN")
    private Double p11mPolishMean;

    @TableField("p_12a_FLATNESS")
    private Double p12aFlatness;

    @TableField("p_12p_FLATNESS")
    private Double p12pFlatness;

    @TableField("p_20a_Length")
    private Double p20aLength;

    @TableField("p_20b_Width")
    private Double p20bWidth;

    private LocalDateTime submitDate;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
