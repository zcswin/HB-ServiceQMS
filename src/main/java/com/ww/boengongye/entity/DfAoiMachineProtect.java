package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * AOI机台维护
 * </p>
 *
 * @author guangyao
 * @since 2023-08-03
 */
@Data
@ApiModel("AOI机台维护类")
public class DfAoiMachineProtect extends Model<DfAoiMachineProtect> {

    private static final long serialVersionUID = 1L;

    /**
     * AOI机台维护id
     */
    @ApiModelProperty("AOI机台维护id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 机台厂商
     */
    @ApiModelProperty("机台厂商")
    private String firm;

    /**
     * 厂商代码
     */
    @ApiModelProperty("厂商代码")
    private String firmCode;

    /**
     * 机台名称
     */
    @ApiModelProperty("机台名称")
    private String machineName;

    /**
     * 机台识别码
     */
    @ApiModelProperty("机台识别码")
    private String machineCode;

    /**
     * 所属车间
     */
    @ApiModelProperty("所属车间")
    private String workshop;

    /**
     * 所属工厂id
     */
    @ApiModelProperty("所属工厂id")
    private int factoryId;

    /**
     * 所属工厂
     */
    @ApiModelProperty("所属工厂")
    @TableField(exist = false)
    private String factoryName;

    /**
     * 机台ip地址
     */
    @ApiModelProperty("机台ip地址")
    private String ip;

    /**
     * 员工编号
     */
    @ApiModelProperty("员工编号")
    private String userCode;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;

    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createName;

    /**
     * 最新修改日期
     */
    @ApiModelProperty("最新修改日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateName;

    @Override
    public String toString() {
        return "DfAoiMachineProtect{" +
                "id=" + id +
                ", firm='" + firm + '\'' +
                ", firmCode='" + firmCode + '\'' +
                ", machineName='" + machineName + '\'' +
                ", machineCode='" + machineCode + '\'' +
                ", workshop='" + workshop + '\'' +
                ", factoryId=" + factoryId +
                ", factoryName='" + factoryName + '\'' +
                ", ip='" + ip + '\'' +
                ", userCode='" + userCode + '\'' +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                ", createName='" + createName + '\'' +
                ", updateTime=" + updateTime +
                ", updateName='" + updateName + '\'' +
                '}';
    }
}
