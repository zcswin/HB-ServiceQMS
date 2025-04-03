package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * E-Code维护
 * </p>
 *
 * @author guangyao
 * @since 2023-08-07
 */
@Data
@ApiModel("E-Code维护表")
public class DfECodeProtect extends Model<DfECodeProtect> {

    private static final long serialVersionUID = 1L;

    /**
     * E-Code维护Id
     */
    @ApiModelProperty("E-Code维护Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 系列
     */
    @ApiModelProperty("E-系列")
    private String series;

    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private Integer projectId;

    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    @TableField(exist = false)
    private String projectName;

    /**
     * 颜色
     */
    @ApiModelProperty("颜色")
    private String colour;

    /**
     * 条码长度-规则1
     */
    @ApiModelProperty("条码长度-规则1")
    private String barCodeLength1;

    /**
     * 匹配值-规则1
     */
    @ApiModelProperty("匹配值-规则1")
    private String matchData1;

    /**
     * 起始值-规则1
     */
    @ApiModelProperty("起始值-规则1")
    private String startData1;

    /**
     * 截取长度-规则1
     */
    @ApiModelProperty("截取长度-规则1")
    private String cutLength1;

    /**
     * 条码长度-规则2
     */
    @ApiModelProperty("条码长度-规则2")
    private String barCodeLength2;

    /**
     * 匹配值-规则2
     */
    @ApiModelProperty("匹配值-规则2")
    private String matchData2;

    /**
     * 起始值-规则2
     */
    @ApiModelProperty("起始值-规则2")
    private String startData2;

    /**
     * 截取长度-规则2
     */
    @ApiModelProperty("截取长度-规则2")
    private String cutLength2;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createName;

    /**
     * 最新修改时间
     */
    @ApiModelProperty("最新修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateName;

    @Override
    public String toString() {
        return "DfECodeProtect{" +
                "id=" + id +
                ", series='" + series + '\'' +
                ", projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", colour='" + colour + '\'' +
                ", barCodeLength1='" + barCodeLength1 + '\'' +
                ", matchData1='" + matchData1 + '\'' +
                ", startData1='" + startData1 + '\'' +
                ", cutLength1='" + cutLength1 + '\'' +
                ", barCodeLength2='" + barCodeLength2 + '\'' +
                ", matchData2='" + matchData2 + '\'' +
                ", startData2='" + startData2 + '\'' +
                ", cutLength2='" + cutLength2 + '\'' +
                ", createTime=" + createTime +
                ", createName='" + createName + '\'' +
                ", updateTime=" + updateTime +
                ", updateName='" + updateName + '\'' +
                '}';
    }
}
