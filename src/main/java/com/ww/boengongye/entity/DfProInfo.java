package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 产品信息
 * </p>
 *
 * @author zhao
 * @since 2022-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfProInfo extends Model<DfProInfo> {

    public static final long serialVersionUID = 1L;

    @TableId("ID")
    public String id;

    @TableField("Code")
    public String Code;

    @TableField("Name")
    public String Name;

    /**
     * 创建时间
     */
    @TableField("CreateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp CreateTime;

    /**
     * 创建人
     */
    @TableField("CreateUser")
    public String CreateUser;

    @TableField("UpdateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp UpdateTime;

    /**
     * 修改人
     */
    @TableField("UpdateUser")
    public String UpdateUser;

    /**
     * 是否删除
     */
    @TableField("IsDelete")
    public Boolean IsDelete;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public DfProInfo() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Timestamp getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Timestamp createTime) {
        CreateTime = createTime;
    }

    public String getCreateUser() {
        return CreateUser;
    }

    public void setCreateUser(String createUser) {
        CreateUser = createUser;
    }

    public Timestamp getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        UpdateTime = updateTime;
    }

    public String getUpdateUser() {
        return UpdateUser;
    }

    public void setUpdateUser(String updateUser) {
        UpdateUser = updateUser;
    }

    public Boolean getDelete() {
        return IsDelete;
    }

    public void setDelete(Boolean delete) {
        IsDelete = delete;
    }
}
