package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.io.Serializable;

/**
 * <p>
 * 漏检率颜色配置
 * </p>
 *
 * @author guangyao
 * @since 2023-08-08
 */
public class DfLossColourConfigure extends Model<DfLossColourConfigure> {

    private static final long serialVersionUID = 1L;

    /**
     * 漏检率颜色配置id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 漏检率颜色名称
     */
    private String name;

    /**
     * 颜色
     */
    private String colour;

    /**
     * 上限
     */
    private String upperLimit;

    /**
     * 下限
     */
    private String lowerLimit;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    /**
     * 创建人
     */
    private String createName;

    /**
     * 最新修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;

    /**
     * 修改人
     */
    private String updateName;

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
    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
    public String getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(String upperLimit) {
        this.upperLimit = upperLimit;
    }
    public String getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(String lowerLimit) {
        this.lowerLimit = lowerLimit;
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
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfLossColourConfigure{" +
            "id=" + id +
            ", name=" + name +
            ", colour=" + colour +
            ", upperLimit=" + upperLimit +
            ", lowerLimit=" + lowerLimit +
            ", createTime=" + createTime +
            ", createName=" + createName +
            ", updateTime=" + updateTime +
            ", updateName=" + updateName +
        "}";
    }
}
