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
 * 机台产量数据
 * </p>
 *
 * @author zhao
 * @since 2023-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfMacYieldData extends Model<DfMacYieldData> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 机台编号
     */
    private String macCode;

    /**
     * 产品实际良率
     */
    private Double produceRealOkRate;

    /**
     * 产品目标良率
     */
    private Double produceTargetOkRate;

    /**
     * 外观实际良率
     */
    private Double appearanceRealOkRate;

    /**
     * 外观目标良率
     */
    private Double appearanceTargetOkRate;

    /**
     * 尺寸实际良率
     */
    private Double sizeRealOkRate;

    /**
     * 尺寸目标良率
     */
    private Double sizeTargetOkRate;

    /**
     * 不良率
     */
    private Double ngRate;

    /**
     * 不良类型
     */
    private String ngType;

    /**
     * 班别
     */
    private String dayOrNight;

    /**
     * 工序
     */
    private String process;

    /**
     * 过杀率
     */
    private Double overkillRate;

    /**
     * 时间开动率
     */
    @TableField("AR")
    private Double ar;

    /**
     * 性能开动率
     */
    @TableField("PR")
    private Double pr;

    /**
     * 合格品率
     */
    @TableField("QR")
    private Double qr;

    /**
     * 产线整体效率
     */
    @TableField("OEE")
    private Double oee;

    /**
     * 稼动率
     */
    private Double cropRate;

    /**
     * 达成率
     */
    private Double achievementRate;

    /**
     * 直通率
     */
    private Double passThroughRate;

    /**
     * 漏检率
     */
    private Double undetectedRate;

    private LocalDateTime createTime;

    @TableField(exist = false)
    private String statusCurDetail;  // 当前状态详情

    @TableField(exist = false)
    private String statusCur;  // 当前状态

    @TableField(exist = false)
    private String processCode; // 工序编号

    @TableField(exist = false)
    private String date;

    @TableField(exist = false)
    private LocalDateTime statusUpdateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
