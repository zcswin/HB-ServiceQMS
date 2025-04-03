package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 炉内尘点
 * </p>
 *
 * @author zhao
 * @since 2023-09-05
 */
public class DfFurnaceDust extends Model<DfFurnaceDust> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 测量时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp checkTime;

    /**
     * 点位
     */
    private String position;

    /**
     * 0.5um尘点数
     */
    private Integer um05;

    /**
     * 5um尘点数
     */
    private Integer um5;

    /**
     * 结果
     */
    private String result;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Timestamp getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Timestamp checkTime) {
        this.checkTime = checkTime;
    }
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
    public Integer getUm05() {
        return um05;
    }

    public void setUm05(Integer um05) {
        this.um05 = um05;
    }
    public Integer getUm5() {
        return um5;
    }

    public void setUm5(Integer um5) {
        this.um5 = um5;
    }
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfFurnaceDust{" +
            "id=" + id +
            ", checkTime=" + checkTime +
            ", position=" + position +
            ", um05=" + um05 +
            ", um5=" + um5 +
            ", result=" + result +
            ", createTime=" + createTime +
        "}";
    }
}
