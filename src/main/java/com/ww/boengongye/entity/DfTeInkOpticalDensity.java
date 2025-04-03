package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 油墨密度
 * </p>
 *
 * @author zhao
 * @since 2022-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfTeInkOpticalDensity extends Model<DfTeInkOpticalDensity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer number;

    @TableField("Ink1")
    private Double Ink1;

    @TableField("Ink2")
    private Double Ink2;

    @TableField("Ink3")
    private Double Ink3;

    @TableField("Ink4")
    private Double Ink4;

    private String result;

    private LocalDateTime submitDate;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
