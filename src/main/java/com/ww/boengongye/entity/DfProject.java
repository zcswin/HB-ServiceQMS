package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2022-09-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("项目类")
public class DfProject extends Model<DfProject> {

    public static final long serialVersionUID = 1L;

    @ApiModelProperty("项目id")
    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    @ApiModelProperty("项目名称")
    public String name;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp updateTime;

    @ApiModelProperty("项目编号")
    public String code;

    @ApiModelProperty("工厂编号")
    public String factoryCode;

    @ApiModelProperty("线体编号")
    public String lineCode;
    @ApiModelProperty("楼层")
    public String floor;
    @ApiModelProperty("工厂名称")
    @TableField(exist = false)
    public String factoryName;


    @ApiModelProperty("线体名称")
    @TableField(exist = false)
    public String lineName;

    @Override
    public String toString() {
        return "DfProject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", code='" + code + '\'' +
                ", factoryCode='" + factoryCode + '\'' +
                ", lineCode='" + lineCode + '\'' +
                ", factoryName='" + factoryName + '\'' +
                ", lineName='" + lineName + '\'' +
                '}';
    }
}
