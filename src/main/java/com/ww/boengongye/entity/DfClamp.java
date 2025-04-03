package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * <p>
 * 夹具表
 * </p>
 *
 * @author liwei
 * @since 2024-05-27
 */
@ApiModel("夹具")
@Data
public class DfClamp extends Model<DfClamp> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    /**
     * 项目
     */
    @ApiModelProperty("项目")
    private String project;

    /**
     * 工序
     */
    @ApiModelProperty("工序")
    private String process;

    @ApiModelProperty("线体")
    private String lineBody;

    /**
     * 机台号
     */
    @ApiModelProperty("机台号")
    private String machine;

    /**
     * 时间
     */
    @ApiModelProperty("时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp time;

    /**
     * 是否使用
     */
    @ApiModelProperty("是否使用")
    private Integer isUse;

    @Override
    public String toString() {
        return "DfClamp{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", project='" + project + '\'' +
                ", process='" + process + '\'' +
                ", lineBody='" + lineBody + '\'' +
                ", machine='" + machine + '\'' +
                ", time=" + time +
                ", isUse=" + isUse +
                '}';
    }
}
