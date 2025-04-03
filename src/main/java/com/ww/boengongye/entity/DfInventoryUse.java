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
 * 库存使用记录
 * </p>
 *
 * @author zhao
 * @since 2022-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfInventoryUse extends Model<DfInventoryUse> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 消耗的物品id
     */
    public Integer parentId;

    /**
     * 消耗数量
     */
    public String quantity;

    /**
     * 使用日期
     */
    public String dateUsed;

    /**
     * 使用人
     */
    public String useMan;

    /**
     * 使用人工号
     */
    public String useManCode;

    /**
     * 用途
     */
    public String purpose;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    /**
     * 创建人
     */
    public String createName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfInventoryUse() {
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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDateUsed() {
        return dateUsed;
    }

    public void setDateUsed(String dateUsed) {
        this.dateUsed = dateUsed;
    }

    public String getUseMan() {
        return useMan;
    }

    public void setUseMan(String useMan) {
        this.useMan = useMan;
    }

    public String getUseManCode() {
        return useManCode;
    }

    public void setUseManCode(String useManCode) {
        this.useManCode = useManCode;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
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
}
