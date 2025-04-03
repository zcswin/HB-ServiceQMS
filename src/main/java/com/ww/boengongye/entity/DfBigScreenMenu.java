package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2023-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfBigScreenMenu extends Model<DfBigScreenMenu> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题名称
     */
    private String name;

    private String url;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    private String type;


    private String floor;
    /**
     * 排序
     */
    private Integer sort;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }



}
