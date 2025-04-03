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
 * AOI颜色表（临时）
 * </p>
 *
 * @author guangyao
 * @since 2023-08-24
 */
@Data
@ApiModel("AOI颜色表")
public class DfAoiColour extends Model<DfAoiColour> {

    private static final long serialVersionUID = 1L;

    /**
     * AOI颜色id
     */
    @ApiModelProperty("AOI颜色id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 颜色名称
     */
    @ApiModelProperty("颜色名称")
    private String name;

    /**
     * 颜色编号
     */
    @ApiModelProperty("颜色编号")
    private String code;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfAoiColour{" +
            "id=" + id +
            ", name=" + name +
            ", code=" + code +
            ", createTime=" + createTime +
        "}";
    }
}
