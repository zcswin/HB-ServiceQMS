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
 * 岗位关联菜单
 * </p>
 *
 * @author zhao
 * @since 2022-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StationRelationMenu extends Model<StationRelationMenu> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 用户id
     */
    public Integer menuId;

    /**
     * 岗位id
     */
    public String stationId;

    /**
     * 创建时间
     */
    public String createTime;

    /**
     * 创建人
     */
    public String createName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public StationRelationMenu() {
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

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
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
