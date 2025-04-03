package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2023-08-28
 */
@Data
@ApiModel("LEAD")
public class DfLeadDetail extends Model<DfLeadDetail> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 检测时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp checkTime;

    /**
     * 条码
     */
    private String sn;

    /**
     * 机台号
     */
    private String machineCode;

    /**
     * 工位
     */
    private String workPosition;

    /**
     * 1是ok,2是尺寸ng,3是外观ng
     */
    private Integer bin;

    /**
     * 结果
     */
    private String result;

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
     * 1:一次  2:重复
     */
    private Integer checkType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @ApiModelProperty("楼层")
    private String floor;

    @Override
    public String toString() {
        return "DfLeadDetail{" +
                "id=" + id +
                ", checkTime=" + checkTime +
                ", sn='" + sn + '\'' +
                ", machineCode='" + machineCode + '\'' +
                ", workPosition='" + workPosition + '\'' +
                ", bin=" + bin +
                ", result='" + result + '\'' +
                ", factory='" + factory + '\'' +
                ", project='" + project + '\'' +
                ", color='" + color + '\'' +
                ", checkType=" + checkType +
                ", createTime=" + createTime +
                ", floor='" + floor + '\'' +
                '}';
    }
}
