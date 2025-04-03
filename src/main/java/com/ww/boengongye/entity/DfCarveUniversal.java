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

/**
 * <p>
 * 精雕通⽤
吸嘴/吸盘清洁/更换记录表
 * </p>
 *
 * @author zhao
 * @since 2023-08-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@Accessors(chain = true)
@ApiModel("精雕通用")
public class DfCarveUniversal extends Model<DfCarveUniversal> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 工厂id
     */
    @ApiModelProperty("工厂id")
    private String factoryId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Timestamp createTime;

    /**
     * 类别
     */
    @ApiModelProperty("类别")
    private String classes;

    /**
     * 机台号
     */
    @ApiModelProperty("机台号")
    private String machineId;

    /**
     * 是否清洁
     */
    @ApiModelProperty("是否清洁 0:否 1:是")
    private String isClean;

    /**
     * 清洁时间
     */
    @ApiModelProperty("清洁时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Timestamp cleanTime;

    /**
     * 是否更换
     */
    @ApiModelProperty("是否更换 0:否 1:是")
    private String isReplace;

    /**
     * 更换时间
     */
    @ApiModelProperty("更换时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Timestamp replaceTime;

    /**
     * 确认人
     */
    @ApiModelProperty("确认人")
    private String confirmUser;

    /**
     * 刀具号
     */
    @ApiModelProperty("刀具号")
    private String knifeNumber;

    /**
     * 产品型号
     */
    @ApiModelProperty("产品型号")
    private String productModel;

    /**
     * 使用时间
     */
    @ApiModelProperty("使用时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Timestamp useTime;

    /**
     * 加工数量
     */
    @ApiModelProperty("加工数量")
    private Integer quantityProcessed;

    /**
     * 更换人
     */
    @ApiModelProperty("更换人")
    private String replaceUser;

    /**
     * 机台_轴号
     */
    @ApiModelProperty("机台_轴号")
    private String machineAxisNo;

    /**
     * 上机时间
     */
    @ApiModelProperty("上机时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Timestamp computerTime;

    /**
     * 下机时间
     */
    @ApiModelProperty("下机时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Timestamp offlineTime;

    /**
     * 下机寿命
     */
    @ApiModelProperty("下机寿命")
    private String offlineLife;

    /**
     * 下机原因
     */
    @ApiModelProperty("下机原因")
    private String offlineReason;

    /**
     * 磨削液浓度
     */
    @ApiModelProperty("磨削液浓度")
    private String grindingFluidConcentration;

    /**
     * 添加记录
     */
    @ApiModelProperty("添加记录")
    private String addRecord;

    /**
     * 点检人
     */
    @ApiModelProperty("点检人")
    private String enumerator;
    /**
     * 组长确认
     */
    @ApiModelProperty("组长确认")
    private String leaderConfirmation;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("工序")
    private String process;


    /**
     * 砂轮寿命
     */
    @ApiModelProperty("砂轮寿命")
    private String carvePosition;

    @TableField(exist = false)
    private String Date;

    @TableField(exist = false)
    private Integer ok;

    @TableField(exist = false)
    private Integer ng;

    @TableField(exist = false)
    private String replaceNum;


    /**
     * 类型: 1(吸嘴/吸盘清洁/更换记录表) 2(砂轮/⼑具更换记录表)
     */
    @ApiModelProperty("类型: 1(吸嘴/吸盘清洁/更换记录表) 2(砂轮/⼑具更换记录表) 3(砂轮/⼑具更换记录表) 4(涂层砂轮寿命记录表)")
    private Integer type;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
