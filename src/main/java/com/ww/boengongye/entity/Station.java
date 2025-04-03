package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 岗位表
 * </p>
 *
 * @author zhao
 * @since 2022-01-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Station extends Model<Station> {

    public static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.INPUT)
    public String id;

    /**
     * 岗位名称
     */
    public String name;

    /**
     * 是否显示
     */
    public String isUse;

    /**
     * 操作内容
     */
    public String content;

    /**
     * 创建时间
     */
    public String createTime;

    /**
     * 创建人
     */
    public String createName;

    /**
     * 更新时间
     */
    public String updateTime;

    /**
     * 更新人
     */
    public String updateName;

    /**
     * 包含的菜单id
     */
    public String menuId;

    /**
     * 权限
     */
    public String permission;

    public String alias;

    public String componentDefine;

    public String dataDesc;

    public String groupId;
      public String type;

    @TableField(exist = false)
    public List<SaveList> saveList;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public Station() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public List<SaveList> getSaveList() {
        return saveList;
    }

    public void setSaveList(List<SaveList> saveList) {
        this.saveList = saveList;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getComponentDefine() {
        return componentDefine;
    }

    public void setComponentDefine(String componentDefine) {
        this.componentDefine = componentDefine;
    }

    public String getDataDesc() {
        return dataDesc;
    }

    public void setDataDesc(String dataDesc) {
        this.dataDesc = dataDesc;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
