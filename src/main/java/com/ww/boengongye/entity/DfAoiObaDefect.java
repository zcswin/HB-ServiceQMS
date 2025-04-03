package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * AOi OBA缺陷
 * </p>
 *
 * @author guangyao
 * @since 2023-09-07
 */
@Data
@ApiModel("AOi OBA缺陷表")
public class DfAoiObaDefect extends Model<DfAoiObaDefect> {

    private static final long serialVersionUID = 1L;

    /**
     * OBA缺陷id
     */
    @ApiModelProperty("OBA缺陷id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
     * 批次号
     */
    @ApiModelProperty("批次号")
    private String batch;

    @Override
    public String toString() {
        return "DfAoiObaDefect{" +
            "id=" + id +
            ", defectName=" + defectName +
            ", defectNumber=" + defectNumber +
            ", batch=" + batch +
        "}";
    }
}
