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
 * 
 * </p>
 *
 * @author zhao
 * @since 2022-08-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfDataSet extends Model<DfDataSet> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    public String id;

    /**
     * 名称
     */
    public String name;

    /**
     * 别名
     */
    public String alias;

    /**
     * 数据类型
     */
    public String dataType;


    /**
     * 父级id
     */
    public String parentId;

    /**
     * 是否有子级 0无 1有
     */
    public Integer hasChild;

    public Integer dataOrder;

    /**
     * 0  false 1 true
     */
    public Integer showOnPc;

    /**
     * 0  false 1 true
     */
    public Integer hiddenInBrowse;

    /**
     * 0  false 1 true
     */
    public Integer showOnPhone;

    /**
     * 0  false 1 true
     */
    public Integer showOnPad;

    public String dataDesc;

    /**
     * 0  false 1 true
     */
    public Integer detectChild;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;
  

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfDataSet() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }



    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getHasChild() {
        return hasChild;
    }

    public void setHasChild(Integer hasChild) {
        this.hasChild = hasChild;
    }

    public Integer getDataOrder() {
        return dataOrder;
    }

    public void setDataOrder(Integer dataOrder) {
        this.dataOrder = dataOrder;
    }

    public Integer getShowOnPc() {
        return showOnPc;
    }

    public void setShowOnPc(Integer showOnPc) {
        this.showOnPc = showOnPc;
    }

    public Integer getHiddenInBrowse() {
        return hiddenInBrowse;
    }

    public void setHiddenInBrowse(Integer hiddenInBrowse) {
        this.hiddenInBrowse = hiddenInBrowse;
    }

    public Integer getShowOnPhone() {
        return showOnPhone;
    }

    public void setShowOnPhone(Integer showOnPhone) {
        this.showOnPhone = showOnPhone;
    }

    public Integer getShowOnPad() {
        return showOnPad;
    }

    public void setShowOnPad(Integer showOnPad) {
        this.showOnPad = showOnPad;
    }

    public String getDataDesc() {
        return dataDesc;
    }

    public void setDataDesc(String dataDesc) {
        this.dataDesc = dataDesc;
    }

    public Integer getDetectChild() {
        return detectChild;
    }

    public void setDetectChild(Integer detectChild) {
        this.detectChild = detectChild;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
