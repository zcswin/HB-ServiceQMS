package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 炉内尘点数量-良率
 * </p>
 *
 * @author zhao
 * @since 2023-09-06
 */
public class DfFurnaceOkRate extends Model<DfFurnaceOkRate> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 类型：um05/um5
     */
    private String type;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 良率
     */
    private String okRate;

    private LocalDateTime createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
    public String getOkRate() {
        return okRate;
    }

    public void setOkRate(String okRate) {
        this.okRate = okRate;
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
        return "DfFurnaceOkRate{" +
            "id=" + id +
            ", type=" + type +
            ", num=" + num +
            ", okRate=" + okRate +
            ", createTime=" + createTime +
        "}";
    }
}
