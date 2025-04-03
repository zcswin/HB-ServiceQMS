package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2023-03-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("机台状态超时管理")
public class DfMacStatusOverTime extends Model<DfMacStatusOverTime> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

    /**
     * 超时时间
     */
    @ApiModelProperty("超时时间")
    private Integer overTime;

    /**
     * 调机时间
     */
    @ApiModelProperty("调机时间")
    private Integer debugTime;

    /**
     * 停机时间
     */
    @ApiModelProperty("停机时间")
    private Integer stopTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


}
