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
 * 消耗品库存表

 * </p>
 *
 * @author zhao
 * @since 2022-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfInventory extends Model<DfInventory> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 备品名称
     */
    public String name;

    /**
     * 备品类型
     */
    public String spareType;

    /**
     * 工厂编号
     */
    public String factoryCode;

    /**
     * 线体编号
     */
    public String lineBodyCode;

    /**
     * 剩余数量
     */
    public Double quantity;

    /**
     * 备品计量单位
     */
    public String unit;

    /**
     * 可供应时间
     */
    public String availableTime;

    /**
     * 补货周期
     */
    public String replenishmentCycle;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    /**
     * 创建人
     */
    public String createName;
    @TableField(exist = false)
    public String factoryName;
    @TableField(exist = false)
    public String lineBodyName;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfInventory() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpareType() {
        return spareType;
    }

    public void setSpareType(String spareType) {
        this.spareType = spareType;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getLineBodyCode() {
        return lineBodyCode;
    }

    public void setLineBodyCode(String lineBodyCode) {
        this.lineBodyCode = lineBodyCode;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(String availableTime) {
        this.availableTime = availableTime;
    }

    public String getReplenishmentCycle() {
        return replenishmentCycle;
    }

    public void setReplenishmentCycle(String replenishmentCycle) {
        this.replenishmentCycle = replenishmentCycle;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getLineBodyName() {
        return lineBodyName;
    }

    public void setLineBodyName(String lineBodyName) {
        this.lineBodyName = lineBodyName;
    }
}
