package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class DfTzFaCaSuggest extends Model<DfTzFaCaSuggest> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * cpk结果
     */
    private String cpk;

    /**
     * ca结果
     */
    private String ca;

    /**
     * fa
     */
    private String faContent;

    /**
     * ca
     */
    private String caContent;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;

    @TableField(exist = false)
    private String titile;

    @Override
    public String toString() {
        return "DfTzFaCaSuggest{" +
            "id=" + id +
            ", cpk=" + cpk +
            ", ca=" + ca +
            ", faContent=" + faContent +
            ", caContent=" + caContent +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
