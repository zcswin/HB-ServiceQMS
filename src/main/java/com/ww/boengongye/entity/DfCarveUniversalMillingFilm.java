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
 * 精雕通用 夹具铣膜记录表
 * </p>
 *
 * @author zhao
 * @since 2023-08-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@Accessors(chain = true)
@ApiModel("精雕通用夹具铣膜记录表")
public class DfCarveUniversalMillingFilm extends Model<DfCarveUniversalMillingFilm> {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("序号")
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
     * 平坦度
     */
    @ApiModelProperty("平坦度")
    private String flatness;

    /**
     * 确认人
     */
    @ApiModelProperty("确认人")
    private String confirmUser;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
