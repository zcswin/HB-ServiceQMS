package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 菜单
 * </p>
 *
 * @author zhao
 * @since 2022-01-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Menu extends Model<Menu> {

    public static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 菜单名称
     */
    public String name;

    /**
     * 是否显示
     */
    public String isUse;

    /**
     * 父菜单名称
     */
    public Integer parentId;

    /**
     * 显示顺序
     */
    public Integer sort;

    /**
     * 备注
     */
    public String remark;

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
     * 图标
     */
    public String icon;

    /**
     * 菜单标题
     */
    public String title;

    /**
     * 1:外部连接,2:内部跳转
     */
    public String isUrl;

    /**
     * 连接地址
     */
    public String jump;
    /**
     * 菜单类型(等级)
     */
    public String type;

    /**
     * 菜单分类
     */
    public String menuType;

    /**
     * 外部链接地址
     */
    public String webUrl;

    /**
     * 所属系统
     */
    public String useType;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public Menu() {
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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsUrl() {
        return isUrl;
    }

    public void setIsUrl(String isUrl) {
        this.isUrl = isUrl;
    }

    public String getJump() {
        return jump;
    }

    public void setJump(String jump) {
        this.jump = jump;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }
}
