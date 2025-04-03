package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2024-12-26
 */
@Data
@ApiOperation("尺寸控制关系表")
public class DfSizeContRelation extends Model<DfSizeContRelation> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * ipqc测试项名称
     */
    private String ipqcName;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    private Double qcpUsl;

    private Double qcpStandard;

    private Double qcpLsl;

    private Double innerUsl;

    private Double innerStandard;

    private Double innerLsl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp updateTime;

    /**
     * 工厂
     */
    private String factory;

    /**
     * 项目
     */
    private String project;

    /**
     * 颜色
     */
    private String color;

    /**
     * 工序
     */
    private String process;



    @ApiModelProperty("实际CPK")
    @TableField(exist = false)
    private Double cpk;
    @ApiModelProperty("内控CPK")
    @TableField(exist = false)
    private Double innerCpk;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfSizeContRelation{" +
                "id=" + id +
                ", ipqcName='" + ipqcName + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", qcpUsl=" + qcpUsl +
                ", qcpStandard=" + qcpStandard +
                ", qcpLsl=" + qcpLsl +
                ", innerUsl=" + innerUsl +
                ", innerStandard=" + innerStandard +
                ", innerLsl=" + innerLsl +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", factory='" + factory + '\'' +
                ", project='" + project + '\'' +
                ", color='" + color + '\'' +
                ", process='" + process + '\'' +
                ", cpk=" + cpk +
                ", innerCpk=" + innerCpk +
                '}';
    }
}
