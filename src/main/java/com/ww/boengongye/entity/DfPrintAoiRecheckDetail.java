package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 移印AOI复查缺陷明细
 * </p>
 *
 * @author guangyao
 * @since 2023-09-13
 */
@Data
@ApiModel("移印AOI复查缺陷明细")
public class DfPrintAoiRecheckDetail extends Model<DfPrintAoiRecheckDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * 缺陷id
     */
    @ApiModelProperty("缺陷id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父类id
     */
    @ApiModelProperty("父类id")
    private Integer checkId;

    /**
     * 检测时间
     */
    @ApiModelProperty("检测时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp checkTime;

    /**
     * 缺陷名称
     */
    @ApiModelProperty("缺陷名称")
    private String defectName;

    /**
     * 缺陷数量
     */
    @ApiModelProperty("缺陷数量")
    private Integer defectNumber;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @Override
    public String toString() {
        return "DfPrintAoiRecheckDetail{" +
                "id=" + id +
                ", checkId='" + checkId + '\'' +
                ", checkTime=" + checkTime +
                ", defectName='" + defectName + '\'' +
                ", defectNumber='" + defectNumber + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
