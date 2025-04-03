package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2024-03-08
 */
public class DfProTime extends Model<DfProTime> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 产品
     */
    private String product;

    /**
     * 工序
     */
    private String process;

    /**
     * 标准值
     */
    private Integer standard;

    /**
     * 上限范围
     */
    private Integer upperLimit;

    /**
     * 下限范围
     */
    private Integer lowerLimit;

    private LocalDateTime createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
    public Integer getStandard() {
        return standard;
    }

    public void setStandard(Integer standard) {
        this.standard = standard;
    }
    public Integer getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(Integer upperLimit) {
        this.upperLimit = upperLimit;
    }
    public Integer getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(Integer lowerLimit) {
        this.lowerLimit = lowerLimit;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfProTime{" +
            "id=" + id +
            ", product=" + product +
            ", process=" + process +
            ", standard=" + standard +
            ", upperLimit=" + upperLimit +
            ", lowerLimit=" + lowerLimit +
            ", createTime=" + createTime +
        "}";
    }
}
