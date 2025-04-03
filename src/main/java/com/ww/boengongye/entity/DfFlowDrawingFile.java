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
 * 流程图纸
 * </p>
 *
 * @author zhao
 * @since 2022-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfFlowDrawingFile extends Model<DfFlowDrawingFile> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 业务类别
     */
    public String category;

    /**
     * 变更范围
     */
    public String changeScope;

    /**
     * 内部项目代码
     */
    public String projectCode;

    /**
     * 变更前后版本号
     */
    public String changeVersionCode;

    /**
     * 适用阶段
     */
    public String applicationStage;

    /**
     * 变更内容
     */
    public String changeContent;

    /**
     * 变更日期
     */
    public String changeDate;

    /**
     * Sift下载日期  
     */
    public String downloadDate;

    /**
     * 工厂名字
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

    public String createName;

    public String uploadPath;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfFlowDrawingFile() {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getChangeScope() {
        return changeScope;
    }

    public void setChangeScope(String changeScope) {
        this.changeScope = changeScope;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getChangeVersionCode() {
        return changeVersionCode;
    }

    public void setChangeVersionCode(String changeVersionCode) {
        this.changeVersionCode = changeVersionCode;
    }

    public String getApplicationStage() {
        return applicationStage;
    }

    public void setApplicationStage(String applicationStage) {
        this.applicationStage = applicationStage;
    }

    public String getChangeContent() {
        return changeContent;
    }

    public void setChangeContent(String changeContent) {
        this.changeContent = changeContent;
    }

    public String getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }

    public String getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(String downloadDate) {
        this.downloadDate = downloadDate;
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
