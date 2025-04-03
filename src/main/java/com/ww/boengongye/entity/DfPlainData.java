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
 * 明码信息表
 * </p>
 *
 * @author zhao
 * @since 2023-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfPlainData extends Model<DfPlainData> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String factoryCode;

    private String processCode;

    private String machineCode;

    /**
     * 操作步骤
     */
    private String operatingStep;

    /**
     * 操作人
     */
    private String createMan;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    /**
     * 载具编号
     */
    private String carrierCode;

    /**
     * 明码编号
     */
    private String plainCode;

    /**
     * 工单id
     */
    private Integer workOrderId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
