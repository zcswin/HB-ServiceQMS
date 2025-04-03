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
 * EMD检测
 * </p>
 *
 * @author zhao
 * @since 2023-09-13
 */
@Data
@ToString
@ApiModel("EMD检测")
public class DfEmdCheckItemInfos extends Model<DfEmdCheckItemInfos> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("创建时间")
    private String createTime;

    /**
     * 检测时间
     */
    @ApiModelProperty("检测时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp checkTime;

    private Integer checkId;

    private String pointPosition;

    private Double result;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
