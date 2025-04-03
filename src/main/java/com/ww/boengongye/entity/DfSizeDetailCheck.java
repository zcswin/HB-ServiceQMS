package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 尺寸数据
 * </p>
 *
 * @author zhao
 * @since 2025-01-12
 */
@Data
@ApiModel("天准诊断-尺寸")
public class DfSizeDetailCheck extends Model<DfSizeDetailCheck> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 机台号
     */
    private String machineCode;

    /**
     * 状态 首件/过程检
     */
    private String status;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 机台状态 正常/闲置/调机/隔离
     */
    private String machineStatus;

    /**
     * 测量结果 OK/NG
     */
    private String result;

    /**
     * 测量时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp testTime;

    /**
     * 工厂
     */
    private String factory;

    /**
     * 项目
     */
    private String project;

    /**
     * 工序
     */
    private String process;

    /**
     * 线体
     */
    private String linebody;

    /**
     * 白/夜班
     */
    private String dayOrNight;

    /**
     * 测试人
     */
    private String testMan;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    /**
     * 是否已发起任务
     */
    private String haveJob;

    private String checkDevCode;

    private String checkType;

    private String groupCode;

    private String checkId;

    private String itemName;

    private String macType;

    private String sn;

    private String shiftName;

    private String tester;

    private String unitCode;

    private String workShopCode;

    private String curTime;

    /**
     * OK/NG/TJ  正常/隔离/调机
     */
    private String infoResult;

    /**
     * 加工时间
     */
    private LocalDateTime workTime;

    /**
     * RFID架子编号
     */
    private String fixtureId;

    /**
     * 刀具1寿命
     */
    private Integer knifeFirstLife;

    /**
     * 刀具2寿命
     */
    private Integer knifeSecondLife;

    /**
     * 刀具3寿命
     */
    private Integer knifeThirdLife;

    /**
     * 刀具4寿命
     */
    private Integer knifeFourthLife;

    /**
     * 有无换刀 (0=无换刀；1=测量异常换刀后首件；2=刀寿到期换刀后首件)
     */
    private Integer changeKnifeStatus;

    /**
     * 有无调机(0=无调机；1=测量异常调机后首件)
     */
    private Integer debugStatus;

    /**
     * 有无换班(0=无换班；1=换班后首件)
     */
    private Integer changeClassStatus;

    /**
     * 有无换夹治具(0=无换夹治具；1=换夹治具后首件)
     */
    private Integer changeClampStatus;

    /**
     *  机台寿命
     */
    private Integer machineLife;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfSizeDetailCheck{" +
            "id=" + id +
            ", machineCode=" + machineCode +
            ", status=" + status +
            ", remarks=" + remarks +
            ", machineStatus=" + machineStatus +
            ", result=" + result +
            ", testTime=" + testTime +
            ", factory=" + factory +
            ", project=" + project +
            ", process=" + process +
            ", linebody=" + linebody +
            ", dayOrNight=" + dayOrNight +
            ", testMan=" + testMan +
            ", createTime=" + createTime +
            ", haveJob=" + haveJob +
            ", checkDevCode=" + checkDevCode +
            ", checkType=" + checkType +
            ", groupCode=" + groupCode +
            ", checkId=" + checkId +
            ", itemName=" + itemName +
            ", macType=" + macType +
            ", sn=" + sn +
            ", shiftName=" + shiftName +
            ", tester=" + tester +
            ", unitCode=" + unitCode +
            ", workShopCode=" + workShopCode +
            ", curTime=" + curTime +
            ", infoResult=" + infoResult +
            ", workTime=" + workTime +
            ", fixtureId=" + fixtureId +
            ", knifeFirstLife=" + knifeFirstLife +
            ", knifeSecondLife=" + knifeSecondLife +
            ", knifeThirdLife=" + knifeThirdLife +
            ", knifeFourthLife=" + knifeFourthLife +
            ", changeKnifeStatus=" + changeKnifeStatus +
            ", debugStatus=" + debugStatus +
            ", changeClassStatus=" + changeClassStatus +
            ", changeClampStatus=" + changeClampStatus +
            ", machineLife=" + machineLife +
        "}";
    }
}
