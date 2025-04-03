package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 
 * </p>
 *
 * @author zhao
 * @since 2023-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfSizeNgData extends Model<DfSizeNgData> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 检测值
     */
    private Double checkValue;

    /**
     * 绑定的id
     */
    private String checkId;

    /**
     * 上限
     */
    private Double usl;

    /**
     * 下限
     */
    private Double lsl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp checkTime;

    /**
     * 检测项名称
     */
    private String itemName;

    /**
     * 不良情况
     */
    private String badCondition;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfSizeNgData() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getCheckValue() {
        return checkValue;
    }

    public void setCheckValue(Double checkValue) {
        this.checkValue = checkValue;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public Double getUsl() {
        return usl;
    }

    public void setUsl(Double usl) {
        this.usl = usl;
    }

    public Double getLsl() {
        return lsl;
    }

    public void setLsl(Double lsl) {
        this.lsl = lsl;
    }

    public Timestamp getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Timestamp checkTime) {
        this.checkTime = checkTime;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getBadCondition() {
        return badCondition;
    }

    public void setBadCondition(String badCondition) {
        this.badCondition = badCondition;
    }
}
