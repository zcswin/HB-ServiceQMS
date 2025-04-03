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
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author guangyao
 * @since 2023-11-29
 */
@Data
public class DfQmsIpqcWaigTotalNew extends Model<DfQmsIpqcWaigTotalNew> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    public Integer fSn;

    public String fBigpro;

    public String fBarcode;

    public String fMac;

    public String fFac;

    public String fBigseq;

    public String fSeq;

    public String fType;

    public String fStage;

    public String fLine;

    public String fColor;

    public String fTestCategory;

    public String fTestType;

    public String fTestMan;

    public String fResearch;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp fTime;

    public String fUser;


    public String createName;
    public String createUserId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    public String status;
    /**
     * 管控频率
     */
    private Integer fContFrequ;
    /**
     * 影响数量
     */

    private Integer affectCount;

    /**
     * 1
     */
    private Integer categoryId;

    private Integer spotCheckCount;
    private String type;
    /**
     * 确认人
     */
    private String confirmor;



    @TableField(exist = false)
    List<DfQmsIpqcWaigDetailNew> detailList;

    @TableField(exist = false)
    public DfAuditDetail dfAuditDetail;

    @TableField(exist = false)
    public String badStatus;

    @TableField(exist = false)
    public String badPosition;

    @TableField(exist = false)
    public String fSort;

    @TableField(exist = false)
    public String num;

    @TableField(exist = false)
    public String date;

    @TableField(exist = false)
    public String okNum;

    @TableField(exist = false)
    public String ngNum;

    @TableField(exist = false)
    public String okRate;

    @Override
    public String toString() {
        return "DfQmsIpqcWaigTotalNew{" +
            "id=" + id +
            ", fSn=" + fSn +
            ", fBigpro=" + fBigpro +
            ", fBarcode=" + fBarcode +
            ", fMac=" + fMac +
            ", fFac=" + fFac +
            ", fBigseq=" + fBigseq +
            ", fSeq=" + fSeq +
            ", fType=" + fType +
            ", fStage=" + fStage +
            ", fLine=" + fLine +
            ", fColor=" + fColor +
            ", fTestCategory=" + fTestCategory +
            ", fTestType=" + fTestType +
            ", fTestMan=" + fTestMan +
            ", fResearch=" + fResearch +
            ", fTime=" + fTime +
            ", fUser=" + fUser +
            ", fContFrequ=" + fContFrequ +
            ", createName=" + createName +
            ", createUserId=" + createUserId +
            ", createTime=" + createTime +
            ", status=" + status +
            ", affectCount=" + affectCount +
            ", categoryId=" + categoryId +
            ", spotCheckCount=" + spotCheckCount +
            ", type=" + type +
        "}";
    }
}
