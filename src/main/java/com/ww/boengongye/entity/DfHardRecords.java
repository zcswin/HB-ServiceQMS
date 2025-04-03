package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
import java.util.Date;

/**
 * <p>
 * 加硬生产记录本
 * </p>
 *
 * @author zhao
 * @since 2023-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@Accessors(chain = true)
@ApiModel("加硬生产记录本")
public class DfHardRecords extends Model<DfHardRecords> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 厂id
     */
    @ApiModelProperty("厂id")
    private String factoryId;

    /**
     * 产品型号
     */
    @ApiModelProperty("产品型号")
    private String productModel;

    /**
     * 材质
     */
    @ApiModelProperty("材质")
    private String meterial;

    /**
     * 预热开始时间
     */
    @ApiModelProperty("预热开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date hotStartDate;

    @ApiModelProperty("预热开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp hotStartTime;

    /**
     * 预热结束时间
     */
    @ApiModelProperty("预热结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date hotEndDate;

    @ApiModelProperty("预热结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp hotEndTime;

    /**
     * 预热温度
     */
    @ApiModelProperty("预热温度")
    private Double hotTemperature;

    /**
     * 加硬开始时间
     */
//    @ApiModelProperty("加硬开始日期")
//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
//    private Date hardStartDate;

//    @ApiModelProperty("加硬开始时间")
//    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
//    private Time hardStartTime;

    /**
     * 加硬结束时间
     */
    @ApiModelProperty("加硬结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date hardEndDate;

    @ApiModelProperty("加硬结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp hardEndTime;

    @ApiModelProperty("加硬总时间")
    private String hardTotalTime;

    /**
     * 加硬温度
     */
    @ApiModelProperty("加硬温度")
    private Double hardTemperature;

    /**
     * 出炉时间
     */
    @ApiModelProperty("出炉日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date outDate;

    @ApiModelProperty("出炉时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp outTime;

    /**
     * 冷却4
     */
    @ApiModelProperty("冷却4")
    private String cool_4;

    /**
     * 冷却8
     */
    @ApiModelProperty("冷却8")
    private String cool_8;

    /**
     * 冷却12
     */
    @ApiModelProperty("冷却12")
    private String cool_12;

    /**
     * 加硬数量-总数
     */
    @ApiModelProperty("加硬数量-总数")
    private Integer hardTotal;

    /**
     * 加硬数量-架数
     */
    @ApiModelProperty("加硬数量-架数")
    private Integer hardAmount;

    /**
     * 加硬数量-笼数
     */
    @ApiModelProperty("加硬数量-笼数")
    private Integer hardCageNum;

    /**
     * 过程添加剂时间
     */
    @ApiModelProperty("过程添加剂日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date processStartDate;

    @ApiModelProperty("过程添加剂时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp processStartTime;


    /**
     * 过程添加剂名称
     */
    @ApiModelProperty("过程添加剂名称")
    private String processName;

    /**
     * 过程添加剂数量
     */
    @ApiModelProperty("过程添加剂数量")
    private Double processsKgAmount;

    /**
     * 确认人-操作员
     */
    @ApiModelProperty("确认人-操作员")
    private String confirmerOperator;

    /**
     * 确认人-IPQC
     */
    @ApiModelProperty("确认人-IPQC")
    private String confirmerIpqc;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;


    @ApiModelProperty("班次")
    private String classess;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @ApiModelProperty("工序")
    private String process;

    @ApiModelProperty("项目")
    private Timestamp project;

    @ApiModelProperty("颜色")
    private Timestamp color;


    @TableField(exist = false)
    private String ok;

    @TableField(exist = false)
    private String ng;

    @TableField(exist = false)
    private String date;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
