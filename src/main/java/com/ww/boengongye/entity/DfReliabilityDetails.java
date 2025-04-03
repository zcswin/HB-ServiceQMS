package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * ORT可靠性明细
 * </p>
 *
 * @author zhao
 * @since 2023-08-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("ORT可靠性明细")
public class DfReliabilityDetails extends Model<DfReliabilityDetails> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 工厂
     */
    @ApiModelProperty("工厂")
    private String factory;

    /**
     * 型号
     */
    @ApiModelProperty("型号")
    private String model;

    /**
     * 工序
     */
    @ApiModelProperty("工序")
    private String process;

    /**
     * 测试项目
     */
    @ApiModelProperty("测试项目")
    private String testProject;

    /**
     * 测试标准
     */
    @ApiModelProperty("测试标准")
    private String testStandard;

    /**
     * 测试设备
     */
    @ApiModelProperty("测试设备")
    private String testDevice;

    /**
     * 测试数量
     */
    @ApiModelProperty("测试数量")
    private String testAmount;

    /**
     * fai频率
     */
    @ApiModelProperty("fai频率")
    private String faiRate;

    /**
     * fai数量
     */
    @ApiModelProperty("fai数量")
    private String faiAmount;

    /**
     * fai单位
     */
    @ApiModelProperty("fai单位")
    private String faiUnit;

    /**
     * ipqc频率
     */
    @ApiModelProperty("ipqc频率")
    private String ipqcRate;

    /**
     * ipqc数量
     */
    @ApiModelProperty("ipqc数量")
    private String ipqcAmount;

    /**
     * ipqc单位
     */
    @ApiModelProperty("ipqc单位")
    private String ipqcUnit;

    /**
     * 路径
     */
    @ApiModelProperty("路径")
    private String realPath;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    private String version;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfReliabilityDetails{" +
            "id=" + id +
            ", factory=" + factory +
            ", model=" + model +
            ", process=" + process +
            ", testProject=" + testProject +
            ", testStandard=" + testStandard +
            ", testDevice=" + testDevice +
            ", testAmount=" + testAmount +
            ", faiRate=" + faiRate +
            ", faiAmount=" + faiAmount +
            ", faiUnit=" + faiUnit +
            ", ipqcRate=" + ipqcRate +
            ", ipqcAmount=" + ipqcAmount +
            ", ipqcUnit=" + ipqcUnit +
        "}";
    }
}
