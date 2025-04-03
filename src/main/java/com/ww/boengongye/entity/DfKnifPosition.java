package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 刀具号
 * </p>
 *
 * @authao
 * @since 2023-09-07
 */
@ApiModel("刀具号")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfKnifPosition extends Model<DfKnifPosition> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 创建时间
     */

    @ApiModelProperty("创建时间")
    private String createTime;

    /**
     * 工序
     */
    @ApiModelProperty("工序")
    private String process;

    /**
     * 刀具号
     */
    @ApiModelProperty("刀具号")
    private String knifPositon;

    /**
     * 工厂
     */
    @ApiModelProperty("工厂")
    private String factory;

    /**
     * 项目
     */
    @ApiModelProperty("项目")
    private String project;

    /**
     * 线体
     */
    @ApiModelProperty("线体")
    private String lineBody;

    /**
     * 颜色
     */
    @ApiModelProperty("颜色")
    private String color;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
