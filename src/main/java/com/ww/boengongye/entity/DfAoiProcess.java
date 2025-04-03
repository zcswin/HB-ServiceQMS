package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * AOI工序（临时）
 * </p>
 *
 * @author guangyao
 * @since 2023-08-24
 */
@ApiModel("AOI工序")
@Data
public class DfAoiProcess extends Model<DfAoiProcess> {

    private static final long serialVersionUID = 1L;

    /**
     * 工序id
     */
    @ApiModelProperty("工序id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 工序名称
     */
    @ApiModelProperty("工序名称")
    private String name;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @Override
    public String toString() {
        return "DfAoiProcess{" +
            "id=" + id +
            ", name=" + name +
            ", createTime=" + createTime +
        "}";
    }
}
