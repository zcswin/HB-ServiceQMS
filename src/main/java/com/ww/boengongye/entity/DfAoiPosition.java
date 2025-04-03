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
 * @since 2023-09-01
 */
public class DfAoiPosition extends Model<DfAoiPosition> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 大区域
     */
    private String bigArea;

    private String area;

    /**
     * 项目
     */
    private String project;

    /**
     * 宽
     */
    private Double width;

    /**
     * 高
     */
    private Double height;

    private Double x1;

    private Double x2;

    private Double x3;

    private Double x4;

    private Double y1;

    private Double y2;

    private Double y3;

    private Double y4;

    /**
     * 类型
     */
    private String type;

    /**
     * 定位类型
     */
    private String positionType;

    private Double pTop;

    private Double pBottom;

    private Double pLeft;

    private Double pRight;

    /**
     * 到中心点x距离
     */
    private Double cententX;

    /**
     * 到中心店y距离
     */
    private Double cententY;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp updateTime;

    /**
     * 层级
     */
    private Integer tier;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getBigArea() {
        return bigArea;
    }

    public void setBigArea(String bigArea) {
        this.bigArea = bigArea;
    }
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }
    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }
    public Double getX1() {
        return x1;
    }

    public void setX1(Double x1) {
        this.x1 = x1;
    }
    public Double getX2() {
        return x2;
    }

    public void setX2(Double x2) {
        this.x2 = x2;
    }
    public Double getX3() {
        return x3;
    }

    public void setX3(Double x3) {
        this.x3 = x3;
    }
    public Double getX4() {
        return x4;
    }

    public void setX4(Double x4) {
        this.x4 = x4;
    }
    public Double getY1() {
        return y1;
    }

    public void setY1(Double y1) {
        this.y1 = y1;
    }
    public Double getY2() {
        return y2;
    }

    public void setY2(Double y2) {
        this.y2 = y2;
    }
    public Double getY3() {
        return y3;
    }

    public void setY3(Double y3) {
        this.y3 = y3;
    }
    public Double getY4() {
        return y4;
    }

    public void setY4(Double y4) {
        this.y4 = y4;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }






















    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
    public Integer getTier() {
        return tier;
    }

    public void setTier(Integer tier) {
        this.tier = tier;
    }


    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public Double getpTop() {
        return pTop;
    }

    public void setpTop(Double pTop) {
        this.pTop = pTop;
    }

    public Double getpBottom() {
        return pBottom;
    }

    public void setpBottom(Double pBottom) {
        this.pBottom = pBottom;
    }

    public Double getpLeft() {
        return pLeft;
    }

    public void setpLeft(Double pLeft) {
        this.pLeft = pLeft;
    }

    public Double getpRight() {
        return pRight;
    }

    public void setpRight(Double pRight) {
        this.pRight = pRight;
    }

    public Double getCententX() {
        return cententX;
    }

    public void setCententX(Double cententX) {
        this.cententX = cententX;
    }

    public Double getCententY() {
        return cententY;
    }

    public void setCententY(Double cententY) {
        this.cententY = cententY;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfAoiPosition{" +
            "id=" + id +
            ", bigArea=" + bigArea +
            ", area=" + area +
            ", project=" + project +
            ", width=" + width +
            ", height=" + height +
            ", x1=" + x1 +
            ", x2=" + x2 +
            ", x3=" + x3 +
            ", x4=" + x4 +
            ", y1=" + y1 +
            ", y2=" + y2 +
            ", y3=" + y3 +
            ", y4=" + y4 +
            ", type=" + type +
            ", positionType=" + positionType +

            ", cententX=" + cententX +
            ", cententY=" + cententY +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", tier=" + tier +
        "}";
    }
}
