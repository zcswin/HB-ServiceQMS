package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2022-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserRole extends Model<UserRole> {

    public static final long serialVersionUID = 1L;

    public String id;

    public String name;

    public String alias;

    public String componentDefine;

    public String dataDesc;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public UserRole() {
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
}
