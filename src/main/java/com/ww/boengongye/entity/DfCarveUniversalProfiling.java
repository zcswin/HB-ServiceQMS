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
 * 精雕通⽤ 夹具靠模点检表
 * </p>
 *
 * @author zhao
 * @since 2023-08-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@Accessors(chain = true)
@ApiModel("精雕通⽤夹具靠模点检表")
public class DfCarveUniversalProfiling extends Model<DfCarveUniversalProfiling> {

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
     * 工序
     */
    @ApiModelProperty("工序")
    private String process;

    /**
     * 机台号
     */
    @ApiModelProperty("机台号")
    private String machineId;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
