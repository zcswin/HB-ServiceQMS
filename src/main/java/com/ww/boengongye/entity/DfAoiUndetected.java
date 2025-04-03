package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * AOI漏检记录表
 * </p>
 *
 * @author zhao
 * @since 2023-08-10
 */
public class DfAoiUndetected extends Model<DfAoiUndetected> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 明码
     */
    private String barcode;

    /**
     * 缺陷id
     */
    private String defectid;

    /**
     * FQC漏检账号
     */
    private String fqcUser;

    /**
     * OQC检测账号
     */
    private String oqcUse;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getDefectid() {
        return defectid;
    }

    public void setDefectid(String defectid) {
        this.defectid = defectid;
    }
    public String getFqcUser() {
        return fqcUser;
    }

    public void setFqcUser(String fqcUser) {
        this.fqcUser = fqcUser;
    }
    public String getOqcUse() {
        return oqcUse;
    }

    public void setOqcUse(String oqcUse) {
        this.oqcUse = oqcUse;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfAoiUndetected{" +
                "id=" + id +
                ", barcode='" + barcode + '\'' +
                ", defectid='" + defectid + '\'' +
                ", fqcUser='" + fqcUser + '\'' +
                ", oqcUse='" + oqcUse + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
