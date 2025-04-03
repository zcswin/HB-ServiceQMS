package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2024-12-26
 */
@Data
public class DfTzSuggest extends Model<DfTzSuggest> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 等级
     */
    private String level;

    /**
     * 最小值
     */
    private Double min;

    /**
     * 最大值
     */
    private Double max;

    /**
     * 建议
     */
    private String suggest;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;

    /**
     * 判定类型
     */
    private String type;

    /**
     * 排序
     */
    private Double sort;

    /**
     * 计算方式一
     */
    private String calculate1;

    /**
     * 计算方式二
     */
    private String calculate2;

    private String result;

    @Override
    public String toString() {
        return "DfTzSuggest{" +
            "id=" + id +
            ", name=" + name +
            ", level=" + level +
            ", min=" + min +
            ", max=" + max +
            ", suggest=" + suggest +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", type=" + type +
            ", sort=" + sort +
            ", calculate1=" + calculate1 +
            ", calculate2=" + calculate2 +
        "}";
    }
}
