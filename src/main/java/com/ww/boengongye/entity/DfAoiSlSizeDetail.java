package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * <p>
 * LOGO尺寸明细表
 * </p>
 *
 * @author zhao
 * @since 2023-09-18
 */
@Data
public class DfAoiSlSizeDetail extends Model<DfAoiSlSizeDetail> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private LocalDateTime createTime;

    /**
     * 检测时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp checkTime;

    /**
     * 状态:首检,巡检
     */
    private String status;

    /**
     * 机台号
     */
    private String machineCode;

    /**
     * 颜色
     */
    private String color;

    /**
     * 型号
     */
    private String model;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
