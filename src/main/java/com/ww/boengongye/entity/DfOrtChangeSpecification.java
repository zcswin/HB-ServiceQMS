package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * ORT规格变更
 * </p>
 *
 * @author zhao
 * @since 2023-08-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfOrtChangeSpecification extends Model<DfOrtChangeSpecification> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 型号
     */
    @ApiModelProperty("型号")
    private String model;

    /**
     * 测试名称
     */

    @ApiModelProperty("测试名称")
    private String testName;

    /**
     * 工序测试频率/数量
     */
    @ApiModelProperty("工序测试频率/数量")
    private String testProcessAmount;

    /**
     * 测试项目
     */
    @ApiModelProperty("测试项目")
    private String testProject;

    /**
     * 判定
     */
    @ApiModelProperty("判定")
    private String predicate;

    /**
     * 阶段
     */
    @ApiModelProperty("阶段")
    private String stage;

    /**
     * 规格
     */
    @ApiModelProperty("规格")
    private String standard;


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
        return "DfOrtChangeSpecification{" +
            "id=" + id +
            ", model=" + model +
            ", testName=" + testName +
            ", testProcessAmount=" + testProcessAmount +
            ", testProject=" + testProject +
            ", predicate=" + predicate +
            ", stage=" + stage +
            ", standard=" + standard +
        "}";
    }
}
