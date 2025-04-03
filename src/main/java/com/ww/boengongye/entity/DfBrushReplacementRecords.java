package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 毛刷更换记录表
 * </p>
 *
 * @author zhao
 * @since 2023-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@Accessors(chain = true)
@ApiModel("毛刷更换记录表")
public class DfBrushReplacementRecords extends Model<DfBrushReplacementRecords> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 厂id
     */@ApiModelProperty("厂id")
    private String factoryId;

    /**
     * 车间
     */
    @ApiModelProperty("工序")
    private String process;

    /**
     * 日期
     */@ApiModelProperty("日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp date;

    /**
     * 班次(白班/夜班)
     */@ApiModelProperty("班次(白班)")
    private String classes;

    /**
     * 更换时间
     */@ApiModelProperty("更换时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp replaceTime;

    /**
     * 数量
     */@ApiModelProperty("数量")
    private Integer amount;

    /**
     * 更换人
     */@ApiModelProperty("更换人")
    private String replaceUser;

    /**
     * 确认人
     */@ApiModelProperty("确认人")
    private String confirmer;

    /**
     * 备注
     */@ApiModelProperty("备注")
    private String remark;


    /**
     * 厚度
     */@ApiModelProperty("厚度")
     private Double thick;

    /**
     * 长度
     */@ApiModelProperty("长度")
    private Double length;

    /**
     * 机台id
     */@ApiModelProperty("机台id")
    private String  machineId;

    @ApiModelProperty("颜色")
     private String color;

    @ApiModelProperty("项目")
     private String project;

    @ApiModelProperty("批次")
     private Integer batch;

    @TableField(exist = false)
     private String ok;
    @TableField(exist = false)
     private String ng;
    @TableField(exist = false)
     private String thickNg;
    @TableField(exist = false)
     private String lengthNg;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
