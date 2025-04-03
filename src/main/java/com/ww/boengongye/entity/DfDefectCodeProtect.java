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
import java.io.Serializable;

/**
 * <p>
 * 不良代码维护
 * </p>
 *
 * @author guangyao
 * @since 2023-08-07
 */
@Data
@ApiModel("不良代码维护表")
public class DfDefectCodeProtect extends Model<DfDefectCodeProtect> {

    private static final long serialVersionUID = 1L;

    /**
     * 不良代码维护id
     */
    @ApiModelProperty("不良代码维护id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
     * 缺陷区域
     */
    @ApiModelProperty("缺陷区域")
    private String defectArea;

    /**
     * 缺陷区域代码
     */
    @ApiModelProperty("缺陷区域代码")
    private String defectAreaData;

    /**
     * 不良名称
     */
    @ApiModelProperty("不良名称")
    private String defectName;

    /**
     * 不良名称英文
     */
    @ApiModelProperty("不良名称英文")
    private String defectNameEnglish;

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
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfDefectCodeProtect{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", defectArea='" + defectArea + '\'' +
                ", defectAreaData='" + defectAreaData + '\'' +
                ", defectName='" + defectName + '\'' +
                ", defectNameEnglish='" + defectNameEnglish + '\'' +
                ", createTime=" + createTime +
                ", createName='" + createName + '\'' +
                ", updateTime=" + updateTime +
                ", updateName='" + updateName + '\'' +
                '}';
    }
}
