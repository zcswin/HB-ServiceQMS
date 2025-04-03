package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 机台调机记录
 * </p>
 *
 * @author liwei
 * @since 2024-05-27
 */
@ApiModel("机台调机记录")
@Data
public class DfMacChange extends Model<DfMacChange> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("机台编号")
    private String machineCode;

    private Integer statusIdCur;


    private Timestamp pubTime;

    /**
     * 设备上次状态
     */
    private Integer statusIdPre;

    /**
     * 上次状态持续时间
     */
    private Integer statusStep;

    /**
     * 告警信息
     */
    private String warningMes;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;


    private Integer isUse;

    @Override
    public String toString() {
        return "DfMacChange{" +
                "id=" + id +
                ", machineCode='" + machineCode + '\'' +
                ", statusIdCur=" + statusIdCur +
                ", pubTime=" + pubTime +
                ", statusIdPre=" + statusIdPre +
                ", statusStep=" + statusStep +
                ", warningMes='" + warningMes + '\'' +
                ", updateTime=" + updateTime +
                ", isUse=" + isUse +
                '}';
    }
}
