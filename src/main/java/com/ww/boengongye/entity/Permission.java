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
 * 权限表
 * </p>
 *
 * @author zhao
 * @since 2021-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Permission extends Model<Permission> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 名称
     */
    public String name;

    /**
     * 可访问页面
     */
    public String pages;

    /**
     * 可操作按钮
     */
    public String buttons;

    /**
     * 创建时间
     */
//    @TableField("createTime")
    public String createTime;

    /**
     * 创建人
     */
//    @TableField("createName")
    public String createName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Permission() {
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

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getButtons() {
        return buttons;
    }

    public void setButtons(String buttons) {
        this.buttons = buttons;
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
}
