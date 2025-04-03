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
 * QA-BOM- 收集产品生产过程需参考的文件-主流程
 * </p>
 *
 * @author zhao
 * @since 2022-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfQoaBomMain extends Model<DfQoaBomMain> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 园区
     */
    public String industrialPark;

    /**
     * 项目
     */
    public String project;

    /**
     * 生产阶段
     */
    public String productionPhase;

    /**
     * 颜色
     */
    public String color;

    /**
     * 图纸类型
     */
    public String drawingType;

    /**
     * 图纸版本名称
     */
    public String drawingVersionName;

    /**
     * 出货数量
     */
    public String shippingQty;

    /**
     * 出货地
     */
    public String salePlace;

    /**
     * 工厂分配
     */
    public String distributionPlant;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    /**
     * 创建人
     */
    public String createName;

    /**
     * 工厂编号
     */
    public String factoryCode;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfQoaBomMain() {
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

    public String getIndustrialPark() {
        return industrialPark;
    }

    public void setIndustrialPark(String industrialPark) {
        this.industrialPark = industrialPark;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getProductionPhase() {
        return productionPhase;
    }

    public void setProductionPhase(String productionPhase) {
        this.productionPhase = productionPhase;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDrawingType() {
        return drawingType;
    }

    public void setDrawingType(String drawingType) {
        this.drawingType = drawingType;
    }

    public String getDrawingVersionName() {
        return drawingVersionName;
    }

    public void setDrawingVersionName(String drawingVersionName) {
        this.drawingVersionName = drawingVersionName;
    }

    public String getShippingQty() {
        return shippingQty;
    }

    public void setShippingQty(String shippingQty) {
        this.shippingQty = shippingQty;
    }

    public String getSalePlace() {
        return salePlace;
    }

    public void setSalePlace(String salePlace) {
        this.salePlace = salePlace;
    }

    public String getDistributionPlant() {
        return distributionPlant;
    }

    public void setDistributionPlant(String distributionPlant) {
        this.distributionPlant = distributionPlant;
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

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }
}
