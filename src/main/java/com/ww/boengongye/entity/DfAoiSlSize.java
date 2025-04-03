package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * df_aoi尺寸表
 * </p>
 *
 * @author zhao
 * @since 2023-09-11
 */
@Data
@ToString
@ApiModel("aoi尺寸")
public class DfAoiSlSize extends Model<DfAoiSlSize> {

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
     * 检测时间
     */
    @ApiModelProperty("检测时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp checkTime;

    private Double faiUp;

    private Double faiDown;

    private Double cpkUp;

    private Double cpkDown;

    /**
     * 标准值
     */
    @ApiModelProperty("标准值")
    private Double standard;

    /**
     * 上限
     */
    @ApiModelProperty("上限")
    private Double up;

    /**
     * 下限
     */
    @ApiModelProperty("下限")
    private Double down;

    private Integer checkId;

    private String item;

    private Double itemValue;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
