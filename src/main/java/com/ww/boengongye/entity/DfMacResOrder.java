package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 机台-工单关系
 * </p>
 *
 * @author zhao
 * @since 2023-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfMacResOrder extends Model<DfMacResOrder> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 机台编号
     */
    private String machineCode;

    /**
     * 工单编号
     */
    private String workOrderCode;

    /**
     * 工序名称
     */
    private String processName;

    @TableField(exist = false)
    private String processCode;

    @TableField(exist = false)
    private String responsibleMan;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 投入数量
     */
    private Integer inNum;

    /**
     * 实际产出数量
     */
    private Integer outNum;

    /**
     * 剩余未完成
     */
    private Integer noAccompNum;

    /**
     * 不良品数量
     */
    private Integer ngNum;

    /**
     * IPQC抽料
     */
    private Integer ipqcSampleNum;

    /**
     * IPQC补料
     */
    private Integer ipqcFeedNum;

    /**
     * 废品数量
     */
    private Integer wasteNum;

    /**
     * 设备总数
     */
    private Integer macNum;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;




    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
