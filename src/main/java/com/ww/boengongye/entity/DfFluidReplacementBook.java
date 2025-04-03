package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 磨液更换记录本-磨液池-非单机台
 * </p>
 *
 * @author zhao
 * @since 2023-08-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@Accessors(chain = true)
@ApiModel("磨液更换记录本-磨液池-非单机台")
public class DfFluidReplacementBook extends Model<DfFluidReplacementBook> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 工厂id
     */
    @ApiModelProperty("工厂id")
    private String factoryId;

    /**
     * 班次
     */
    @ApiModelProperty("班次")
    private String classes;

    /**
     * 工序
     */
    @ApiModelProperty("工序")
    private String process;

    /**
     * 更换人
     */
    @ApiModelProperty("更换人")
    private String replaceUser;

    /**
     * 更换时间
     */
    @ApiModelProperty("更换时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Timestamp replaceTime;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 机台id
     */
    @ApiModelProperty("机台id")
    private String machineId;

    /**
     * 时间
     */
    @ApiModelProperty("时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Timestamp createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
