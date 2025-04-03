package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2023-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfStatusCode extends Model<DfStatusCode> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 状态编号
     */
    public Integer statusCode;

    /**
     * 名称
     */
    public String name;

    /**
     * 颜色
     */
    public String color;

    /**
     * 父状态
     */
    public Integer parentStatusId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public DfStatusCode() {
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

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getParentStatusId() {
        return parentStatusId;
    }

    public void setParentStatusId(Integer parentStatusId) {
        this.parentStatusId = parentStatusId;
    }
}
