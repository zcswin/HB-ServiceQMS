package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 风险品
 * </p>
 *
 * @author guangyao
 * @since 2023-10-25
 */
@Data
@ApiModel("风险品")
public class DfRiskProduct extends Model<DfRiskProduct> {

    private static final long serialVersionUID = 1L;

    /**
     * 风险品id
     */
    @ApiModelProperty("风险品id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父id
     */
    @ApiModelProperty("父id")
    private Integer parentId;

    /**
     * 工序
     */
    @ApiModelProperty("工序")
    private String process;

    /**
     * 坏码源
     */
    @ApiModelProperty("坏码源")
    private String sourceBadCode;

    /**
     * 生产时间
     */
    @ApiModelProperty("生产时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private String productiveTime;

    /**
     * 明码
     */
    @ApiModelProperty("明码")
    private String barCode;

    /**
     * 机台号
     */
    @ApiModelProperty("机台号")
    private String machineCode;

    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String address;

    /**
     * 结果
     */
    @ApiModelProperty("结果")
    private String result;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp createTime;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("抽检结果")
    @TableField(exist = false)
    private String checkResult;
    @Override
    public String toString() {
        return "DfRiskProduct{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", process='" + process + '\'' +
                ", sourceBadCode='" + sourceBadCode + '\'' +
                ", productiveTime=" + productiveTime +
                ", barCode='" + barCode + '\'' +
                ", machineCode='" + machineCode + '\'' +
                ", address='" + address + '\'' +
                ", result='" + result + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
