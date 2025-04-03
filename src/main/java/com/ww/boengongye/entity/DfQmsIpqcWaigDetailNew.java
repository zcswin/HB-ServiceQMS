package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author guangyao
 * @since 2023-11-29
 */
@Data
public class DfQmsIpqcWaigDetailNew extends Model<DfQmsIpqcWaigDetailNew> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    public String fSn;

    public String fBigArea;

    public String fSmArea;

    public String fDefect;

    public String fSort;

    public String fLevel;

    public String fStandard;

    public String fZerostandard;

    public String fResult;

    public String fX;

    public String fY;
    public String fProductId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp fTime;

    public Integer fParentId;

    public Integer  isUniwafe;


    @TableField(exist = false)
    public String color;

    @TableField(exist = false)
    public String fBarcode;

    @TableField(exist = false)
    public String fMac;

    @TableField(exist = false)
    public String fBigpro;


    @Override
    public String toString() {
        return "DfQmsIpqcWaigDetailNew{" +
            "id=" + id +
            ", fSn=" + fSn +
            ", fBigArea=" + fBigArea +
            ", fSmArea=" + fSmArea +
            ", fDefect=" + fDefect +
            ", fSort=" + fSort +
            ", fLevel=" + fLevel +
            ", fStandard=" + fStandard +
            ", fZerostandard=" + fZerostandard +
            ", fResult=" + fResult +
            ", fX=" + fX +
            ", fY=" + fY +
            ", fTime=" + fTime +
            ", fParentId=" + fParentId +
            ", fProductId=" + fProductId +
            ", isUniwafe=" + isUniwafe +
        "}";
    }
}
