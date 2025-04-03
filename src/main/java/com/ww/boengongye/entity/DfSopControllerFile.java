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
 * SOP/SIP/MSOP控制文件
 * </p>
 *
 * @author zhao
 * @since 2022-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfSopControllerFile extends Model<DfSopControllerFile> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 项目
     */
    public String project;

    /**
     * 变更类型
     */
    public String changeType;

    /**
     * 适用型号
     */
    public String applyModel;

    /**
     * 适用场合
     */
    public String occasionApplicable;

    /**
     * 文件名称
     */
    public String fileName;

    /**
     * 文件类型
     */
    public String fileType;

    /**
     * 版本号
     */
    public String version;

    /**
     * 制定部门
     */
    public String makingDerpartment;

    /**
     * 文件制定人
     */
    public String documentMaker;

    /**
     * 发布日期
     */
    public String releaseDate;

    /**
     * 文件上传
     */
    public String fileUpload;

    /**
     * 确认人
     */
    public String confirmor;

    /**
     * 登记时间
     */
    public String registrationTime;

    /**
     * 工厂
     */
    public String factoryName;

    /**
     * 工厂编号
     */
    public String factoryCode;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    /**
     * 创建人
     */
    public String createName;

    public String uploadPath;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfSopControllerFile() {
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

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getApplyModel() {
        return applyModel;
    }

    public void setApplyModel(String applyModel) {
        this.applyModel = applyModel;
    }

    public String getOccasionApplicable() {
        return occasionApplicable;
    }

    public void setOccasionApplicable(String occasionApplicable) {
        this.occasionApplicable = occasionApplicable;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMakingDerpartment() {
        return makingDerpartment;
    }

    public void setMakingDerpartment(String makingDerpartment) {
        this.makingDerpartment = makingDerpartment;
    }

    public String getDocumentMaker() {
        return documentMaker;
    }

    public void setDocumentMaker(String documentMaker) {
        this.documentMaker = documentMaker;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(String fileUpload) {
        this.fileUpload = fileUpload;
    }

    public String getConfirmor() {
        return confirmor;
    }

    public void setConfirmor(String confirmor) {
        this.confirmor = confirmor;
    }

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
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

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }
}
