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
 * AOI配置表
 * </p>
 *
 * @author guangyao
 * @since 2023-10-08
 */
@Data
@ApiModel("AOI配置表")
public class DfAoiConfigData extends Model<DfAoiConfigData> {

    private static final long serialVersionUID = 1L;

    /**
     * AOI配置ID
     */
    @ApiModelProperty("AOI配置ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 配置内容
     */
    @ApiModelProperty("配置内容")
    private String configName;

    /**
     * 配置值
     */
    @ApiModelProperty("配置值")
    private Double configValue;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @Override
    public String toString() {
        return "DfAoiConfigData{" +
            "id=" + id +
            ", configName=" + configName +
            ", configValue=" + configValue +
            ", createTime=" + createTime +
        "}";
    }
}
