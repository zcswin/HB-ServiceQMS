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
 * 
 * </p>
 *
 * @author zhao
 * @since 2023-09-14
 */
public class DfAgvPoint extends Model<DfAgvPoint> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private Double agvX;

    private Double agvY;

    private Double x;

    private Double y;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

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
    public Double getAgvX() {
        return agvX;
    }

    public void setAgvX(Double agvX) {
        this.agvX = agvX;
    }
    public Double getAgvY() {
        return agvY;
    }

    public void setAgvY(Double agvY) {
        this.agvY = agvY;
    }
    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }
    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
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
        return "DfAgvPoint{" +
            "id=" + id +
            ", name=" + name +
            ", agvX=" + agvX +
            ", agvY=" + agvY +
            ", x=" + x +
            ", y=" + y +
            ", createTime=" + createTime +
        "}";
    }
}
