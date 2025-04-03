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
 * 换盘登记本
 * </p>
 *
 * @author zhao
 * @since 2023-08-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@Accessors(chain = true)
@ApiModel("换盘登记本")
public class DfChangeDiskBook extends Model<DfChangeDiskBook> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 工厂id
     */
    private String factoryId;

    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    private String machineName;

    /**
     * 机台号
     */
    @ApiModelProperty("机台号")
    private String machineId;

    /**
     * 故障描述
     */
    @ApiModelProperty("故障描述")
    private String errorDesc;

    /**
     * 报修人
     */
    @ApiModelProperty("报修人")
    private String repairUser;

    /**
     * 完成时间
     */
    @ApiModelProperty("完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Timestamp finishTime;

    /**
     * 安装人
     */
    @ApiModelProperty("安装人")
    private String installer;

    /**
     * 产线签收
     */
    @ApiModelProperty("产线签收")
    private String productSignoff;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Timestamp createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
