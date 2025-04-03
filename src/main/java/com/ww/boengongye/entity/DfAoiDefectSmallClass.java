package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * AOI缺陷小类表
 * </p>
 *
 * @author guangyao
 * @since 2023-10-18
 */
@Data
@ApiModel("AOI缺陷小类表")
public class DfAoiDefectSmallClass extends Model<DfAoiDefectSmallClass> {

    private static final long serialVersionUID = 1L;

    /**
     * 缺陷小类id
     */
    @ApiModelProperty("缺陷小类id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 是否是重大缺陷（Y/N）
     */
    @ApiModelProperty("缺陷小类id")
    private String majorDefect;

    /**
     * 缺陷小类名称
     */
    @ApiModelProperty("缺陷小类id")
    private String name;

    /**
     * 缺陷大类名称
     */
    @ApiModelProperty("缺陷小类id")
    private String className;

    /**
     * 创建时间
     */
    @ApiModelProperty("缺陷小类id")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @Override
    public String toString() {
        return "DfAoiDefectSmallClass{" +
                "id=" + id +
                ", majorDefect='" + majorDefect + '\'' +
                ", name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", createTime=" + createTime +
                '}';
    }


}
