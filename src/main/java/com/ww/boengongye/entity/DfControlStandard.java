package com.ww.boengongye.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2022-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfControlStandard extends Model<DfControlStandard> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 工厂编号
     */
    public String factoryCode;

    /**
     * 工序名称
     */
    public String processName;

    /**
     * 管控类型名称
     */
    public String controlName;

    /**
     * 分类
     */
    public String classification;

    /**
     * 细项
     */
    public String detail;

    /**
     * 标准值/方法
     */
    public String standard;

    /**
     * 抽检频率
     */
    public String checkFrequency;

    /**
     * 设备
     */
    public String device;

    /**
     * 最小值
     */
    public BigDecimal min;

    /**
     * 最大值
     */
    public BigDecimal max;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public DfControlStandard() {
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

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getControlName() {
        return controlName;
    }

    public void setControlName(String controlName) {
        this.controlName = controlName;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getCheckFrequency() {
        return checkFrequency;
    }

    public void setCheckFrequency(String checkFrequency) {
        this.checkFrequency = checkFrequency;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }

    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }
}
