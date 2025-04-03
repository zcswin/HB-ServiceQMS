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
 * 审批时间配置表
 * </p>
 *
 * @author zhao
 * @since 2023-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfApprovalTime extends Model<DfApprovalTime> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 类型
     */
    private String type;

    private Integer level1;

    private Integer level2;

    private Integer level3;

    private Integer level4;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    private String createName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp updateTime;

    private String updateName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp updateCode;
    private Integer readTimeLevel1;
    private Integer readTimeLevel2;
    private Integer readTimeLevel3;
    private Integer disposeTimeLevel1;
    private Integer disposeTimeLevel2;
    private Integer disposeTimeLevel3;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfApprovalTime() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLevel1() {
        return level1;
    }

    public void setLevel1(Integer level1) {
        this.level1 = level1;
    }

    public Integer getLevel2() {
        return level2;
    }

    public void setLevel2(Integer level2) {
        this.level2 = level2;
    }

    public Integer getLevel3() {
        return level3;
    }

    public void setLevel3(Integer level3) {
        this.level3 = level3;
    }

    public Integer getLevel4() {
        return level4;
    }

    public void setLevel4(Integer level4) {
        this.level4 = level4;
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

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public Timestamp getUpdateCode() {
        return updateCode;
    }

    public void setUpdateCode(Timestamp updateCode) {
        this.updateCode = updateCode;
    }

    public Integer getReadTimeLevel1() {

        return readTimeLevel1;
    }

    public void setReadTimeLevel1(Integer readTimeLevel1) {
        this.readTimeLevel1 = readTimeLevel1;
    }

    public Integer getReadTimeLevel2() {
        return readTimeLevel2;
    }

    public void setReadTimeLevel2(Integer readTimeLevel2) {
        this.readTimeLevel2 = readTimeLevel2;
    }

    public Integer getReadTimeLevel3() {
        return readTimeLevel3;
    }

    public void setReadTimeLevel3(Integer readTimeLevel3) {
        this.readTimeLevel3 = readTimeLevel3;
    }

    public Integer getDisposeTimeLevel1() {
        return disposeTimeLevel1;
    }

    public void setDisposeTimeLevel1(Integer disposeTimeLevel1) {
        this.disposeTimeLevel1 = disposeTimeLevel1;
    }

    public Integer getDisposeTimeLevel2() {
        return disposeTimeLevel2;
    }

    public void setDisposeTimeLevel2(Integer disposeTimeLevel2) {
        this.disposeTimeLevel2 = disposeTimeLevel2;
    }

    public Integer getDisposeTimeLevel3() {
        return disposeTimeLevel3;
    }

    public void setDisposeTimeLevel3(Integer disposeTimeLevel3) {
        this.disposeTimeLevel3 = disposeTimeLevel3;
    }
}
