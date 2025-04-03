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
 * 光孔磨粉添加/更换记录表
 * </p>
 *
 * @author zhao
 * @since 2023-08-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@Accessors(chain = true)
@ApiModel("光孔磨粉")
public class DfLightHoleGrinding extends Model<DfLightHoleGrinding> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 班别
     */
    @ApiModelProperty("班别")
    private String classes;

    /**
     * 添加量
     */
    @ApiModelProperty("添加量")
    private String addAddition;

    /**
     * 添加时间
     */
    @ApiModelProperty("添加时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Timestamp addTime;

    /**
     * 是否更换 0:否 1:是
     */
    @ApiModelProperty("是否更换0否1是")
    private String isReplace;

    /**
     * 添加人
     */
    @ApiModelProperty("添加人")
    private String addUser;

    /**
     * 确认人
     */
    @ApiModelProperty("确认人")
    private String confirmUser;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

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
