package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 工单
 * </p>
 *
 * @author zhao
 * @since 2023-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfWorkOrder extends Model<DfWorkOrder> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 工单编号
     */
    private String workOrderCode;

    /**
     * 工厂
     */
    private String factoryCode;

    /**
     * 工序
     */
    private String processCode;

    /**
     * 项目
     */
    private String projectCode;

    /**
     * 线体
     */
    private String linebodyCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
