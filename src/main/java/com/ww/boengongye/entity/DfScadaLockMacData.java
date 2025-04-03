package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * SCADA锁机/解锁记录
 * </p>
 *
 * @author zhao
 * @since 2025-01-10
 */
@ApiModel("SCADA锁机/解锁记录")
@Data
public class DfScadaLockMacData extends Model<DfScadaLockMacData> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 机台号
     */
    @ApiModelProperty("机台号")
    private String machineCode;

    /**
     * 机台状态
     */
    @ApiModelProperty("机台状态")
    private String macStatus;

    /**
     * 记录
     */
    @ApiModelProperty("记录")
    private String logData;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfScadaLockMacData{" +
                "id=" + id +
                ", machineCode='" + machineCode + '\'' +
                ", macStatus='" + macStatus + '\'' +
                ", logData='" + logData + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
