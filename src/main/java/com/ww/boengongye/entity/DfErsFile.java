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
 * ERS文件
 * </p>
 *
 * @author zhao
 * @since 2022-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfErsFile extends Model<DfErsFile> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 文件APN
     */
    public String fileApn;

    /**
     * ERS文件名称
     */
    public String ersFileName;

    /**
     * 版本
     */
    public String version;

    /**
     * 对应图纸编号版本
     */
    public String brawingCodeVersion;

    /**
     * 版本更新描述
     */
    public String updateDescription;

    /**
     * 更新日期
     */
    public String updateDate;

    /**
     * 修改人
     */
    public String editMan;

    /**
     * 检验清单
     */
    public String listInspection;

    /**
     * MSOP
     */
    public String msop;

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
    /**
     * 保存路径
     */
    public String uploadPath;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfErsFile() {
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

    public String getFileApn() {
        return fileApn;
    }

    public void setFileApn(String fileApn) {
        this.fileApn = fileApn;
    }

    public String getErsFileName() {
        return ersFileName;
    }

    public void setErsFileName(String ersFileName) {
        this.ersFileName = ersFileName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBrawingCodeVersion() {
        return brawingCodeVersion;
    }

    public void setBrawingCodeVersion(String brawingCodeVersion) {
        this.brawingCodeVersion = brawingCodeVersion;
    }

    public String getUpdateDescription() {
        return updateDescription;
    }

    public void setUpdateDescription(String updateDescription) {
        this.updateDescription = updateDescription;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getEditMan() {
        return editMan;
    }

    public void setEditMan(String editMan) {
        this.editMan = editMan;
    }

    public String getListInspection() {
        return listInspection;
    }

    public void setListInspection(String listInspection) {
        this.listInspection = listInspection;
    }

    public String getMsop() {
        return msop;
    }

    public void setMsop(String msop) {
        this.msop = msop;
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
