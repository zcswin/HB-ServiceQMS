package com.ww.boengongye.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 点检率表
 * </p>
 *
 * @author zhao
 * @since 2022-10-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfSpotCheckRate extends Model<DfSpotCheckRate> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer factoryId;

    @TableField(exist = false)
    private String factoryName;

    private BigDecimal checkRate;

    private String type;

    private String checkType;

    private Integer checkYear;

    private Integer checkMonth;

    private Integer passNum;

    private Integer failNum;

    private String createMan;

    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfSpotCheckRate() {
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

    public Integer getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Integer factoryId) {
        this.factoryId = factoryId;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public BigDecimal getCheckRate() {
        return checkRate;
    }

    public void setCheckRate(BigDecimal checkRate) {
        this.checkRate = checkRate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public Integer getCheckYear() {
        return checkYear;
    }

    public void setCheckYear(Integer checkYear) {
        this.checkYear = checkYear;
    }

    public Integer getCheckMonth() {
        return checkMonth;
    }

    public void setCheckMonth(Integer checkMonth) {
        this.checkMonth = checkMonth;
    }

    public Integer getPassNum() {
        return passNum;
    }

    public void setPassNum(Integer passNum) {
        this.passNum = passNum;
    }

    public Integer getFailNum() {
        return failNum;
    }

    public void setFailNum(Integer failNum) {
        this.failNum = failNum;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
