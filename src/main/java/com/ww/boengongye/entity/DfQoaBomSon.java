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
 * QA-BOM- 收集产品生产过程需参考的文件-子流程
 * </p>
 *
 * @author zhao
 * @since 2022-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfQoaBomSon extends Model<DfQoaBomSon> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 父id
     */
    public Integer parentId;

    /**
     * 确认时间
     */
    public String acknowledgingTime;

    /**
     * 图纸文件
     */
    public String brawingFile;

    /**
     * DFM文件
     */
    public String dfmFile;

    /**
     * 外部QCP
     */
    public String externalQcp;

    /**
     * 内部QCP
     */
    public String interiorQcp;

    /**
     * 物料BOM
     */
    public String materialBom;

    /**
     * ERS文件
     */
    public String ersFile;

    /**
     * MIL文件
     */
    public String milFile;

    /**
     * 工厂审批
     */
    public String factoryApproval;

    /**
     * 生产单号
     */
    public String productionOrder;

    /**
     * 首件报告
     */
    public String firstReport;

    /**
     * 文件存放位置
     */
    public String fileStorageLocation;

    /**
     * 生传文件的存放位置
     */
    public String uploadPath;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    /**
     * 创建人
     */
    public String createName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfQoaBomSon() {
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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getAcknowledgingTime() {
        return acknowledgingTime;
    }

    public void setAcknowledgingTime(String acknowledgingTime) {
        this.acknowledgingTime = acknowledgingTime;
    }

    public String getBrawingFile() {
        return brawingFile;
    }

    public void setBrawingFile(String brawingFile) {
        this.brawingFile = brawingFile;
    }

    public String getDfmFile() {
        return dfmFile;
    }

    public void setDfmFile(String dfmFile) {
        this.dfmFile = dfmFile;
    }

    public String getExternalQcp() {
        return externalQcp;
    }

    public void setExternalQcp(String externalQcp) {
        this.externalQcp = externalQcp;
    }

    public String getInteriorQcp() {
        return interiorQcp;
    }

    public void setInteriorQcp(String interiorQcp) {
        this.interiorQcp = interiorQcp;
    }

    public String getMaterialBom() {
        return materialBom;
    }

    public void setMaterialBom(String materialBom) {
        this.materialBom = materialBom;
    }

    public String getErsFile() {
        return ersFile;
    }

    public void setErsFile(String ersFile) {
        this.ersFile = ersFile;
    }

    public String getMilFile() {
        return milFile;
    }

    public void setMilFile(String milFile) {
        this.milFile = milFile;
    }

    public String getFactoryApproval() {
        return factoryApproval;
    }

    public void setFactoryApproval(String factoryApproval) {
        this.factoryApproval = factoryApproval;
    }

    public String getProductionOrder() {
        return productionOrder;
    }

    public void setProductionOrder(String productionOrder) {
        this.productionOrder = productionOrder;
    }

    public String getFirstReport() {
        return firstReport;
    }

    public void setFirstReport(String firstReport) {
        this.firstReport = firstReport;
    }

    public String getFileStorageLocation() {
        return fileStorageLocation;
    }

    public void setFileStorageLocation(String fileStorageLocation) {
        this.fileStorageLocation = fileStorageLocation;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
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
}
