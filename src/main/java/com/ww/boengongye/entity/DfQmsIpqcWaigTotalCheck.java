package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2024-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfQmsIpqcWaigTotalCheck extends Model<DfQmsIpqcWaigTotalCheck> {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DfQmsIpqcWaigTotalCheck that = (DfQmsIpqcWaigTotalCheck) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(fSn, that.fSn) &&
                Objects.equals(fBigpro, that.fBigpro) &&
                Objects.equals(fBarcode, that.fBarcode) &&
                Objects.equals(fMac, that.fMac) &&
                Objects.equals(fFac, that.fFac) &&
                Objects.equals(fBigseq, that.fBigseq) &&
                Objects.equals(fSeq, that.fSeq) &&
                Objects.equals(fType, that.fType) &&
                Objects.equals(fStage, that.fStage) &&
                Objects.equals(fLine, that.fLine) &&
                Objects.equals(fColor, that.fColor) &&
                Objects.equals(fTestCategory, that.fTestCategory) &&
                Objects.equals(fTestType, that.fTestType) &&
                Objects.equals(fTestMan, that.fTestMan) &&
                Objects.equals(fResearch, that.fResearch) &&
                Objects.equals(fTime, that.fTime) &&
                Objects.equals(fUser, that.fUser) &&
                Objects.equals(fContFrequ, that.fContFrequ) &&
                Objects.equals(detailList, that.detailList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fSn, fBigpro, fBarcode, fMac, fFac, fBigseq, fSeq, fType, fStage, fLine, fColor, fTestCategory, fTestType, fTestMan, fResearch, fTime, fUser, fContFrequ, detailList);
    }

    public static final long serialVersionUID = 1L;

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

    public String confirmor;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp fTime;

    public String fUser;


    public String createName;
    public String createUserId;
    public String status;
    /**
     * 管控频率
     */
    private Integer fContFrequ;
    /**
     * 影响数量
     */

    private Integer affectCount;
    private Integer spotCheckCount;
    private String type;
    /**
     * 抽检机台
     */
    private String checkMachine;
    /**
     * 抽检工序
     */
    private String checkProcess;


    @TableField(exist = false)
    List<DfQmsIpqcWaigDetailCheck> detailList;

    @TableField(exist = false)
    public DfAuditDetail dfAuditDetail;

    @TableField(exist = false)
    public String badStatus;

    @TableField(exist = false)
    public String badPosition;

    @TableField(exist = false)
    public String shift;

    @TableField(exist = false)
    public Integer cpkTotal;

    @TableField(exist = false)
    public Integer cpkNg;

    @TableField(exist = false)
    public Double faiStartupRate;

    @TableField(exist = false)
    public Double cpkPassRate;

    @TableField(exist = false)
    public Integer faiCount;

    @TableField(exist = false)
    public Integer machineNum;

    @TableField(exist = false)
    public int sort;

    @TableField(exist = false)
    public Double  ngRate;


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
    protected Serializable pkVal() {
        return this.id;
    }

    public DfQmsIpqcWaigTotalCheck() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getfContFrequ() {
        return fContFrequ;
    }

    public void setfContFrequ(Integer fContFrequ) {
        this.fContFrequ = fContFrequ;
    }

    public String getfSort() {
        return fSort;
    }

    public void setfSort(String fSort) {
        this.fSort = fSort;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOkNum() {
        return okNum;
    }

    public void setOkNum(String okNum) {
        this.okNum = okNum;
    }

    public String getNgNum() {
        return ngNum;
    }

    public void setNgNum(String ngNum) {
        this.ngNum = ngNum;
    }

    public String getOkRate() {
        return okRate;
    }

    public void setOkRate(String okRate) {
        this.okRate = okRate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getfSn() {
        return fSn;
    }

    public void setfSn(Integer fSn) {
        this.fSn = fSn;
    }

    public String getfBigpro() {
        return fBigpro;
    }

    public void setfBigpro(String fBigpro) {
        this.fBigpro = fBigpro;
    }

    public String getfBarcode() {
        return fBarcode;
    }

    public void setfBarcode(String fBarcode) {
        this.fBarcode = fBarcode;
    }

    public String getfMac() {
        return fMac;
    }

    public void setfMac(String fMac) {
        this.fMac = fMac;
    }

    public String getfFac() {
        return fFac;
    }

    public void setfFac(String fFac) {
        this.fFac = fFac;
    }

    public String getfBigseq() {
        return fBigseq;
    }

    public void setfBigseq(String fBigseq) {
        this.fBigseq = fBigseq;
    }

    public String getfSeq() {
        return fSeq;
    }

    public void setfSeq(String fSeq) {
        this.fSeq = fSeq;
    }

    public String getfType() {
        return fType;
    }

    public void setfType(String fType) {
        this.fType = fType;
    }

    public String getfStage() {
        return fStage;
    }

    public void setfStage(String fStage) {
        this.fStage = fStage;
    }

    public String getfLine() {
        return fLine;
    }

    public void setfLine(String fLine) {
        this.fLine = fLine;
    }

    public String getfColor() {
        return fColor;
    }

    public void setfColor(String fColor) {
        this.fColor = fColor;
    }

    public String getfTestCategory() {
        return fTestCategory;
    }

    public void setfTestCategory(String fTestCategory) {
        this.fTestCategory = fTestCategory;
    }

    public String getfTestType() {
        return fTestType;
    }

    public void setfTestType(String fTestType) {
        this.fTestType = fTestType;
    }

    public String getfTestMan() {
        return fTestMan;
    }

    public void setfTestMan(String fTestMan) {
        this.fTestMan = fTestMan;
    }

    public String getfResearch() {
        return fResearch;
    }

    public void setfResearch(String fResearch) {
        this.fResearch = fResearch;
    }

    public Timestamp getfTime() {
        return fTime;
    }

    public void setfTime(Timestamp fTime) {
        this.fTime = fTime;
    }

    public String getfUser() {
        return fUser;
    }

    public void setfUser(String fUser) {
        this.fUser = fUser;
    }

    public List<DfQmsIpqcWaigDetailCheck> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<DfQmsIpqcWaigDetailCheck> detailList) {
        this.detailList = detailList;
    }



    public DfAuditDetail getDfAuditDetail() {
        return dfAuditDetail;
    }

    public void setDfAuditDetail(DfAuditDetail dfAuditDetail) {
        this.dfAuditDetail = dfAuditDetail;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBadStatus() {
        return badStatus;
    }

    public void setBadStatus(String badStatus) {
        this.badStatus = badStatus;
    }

    public Integer getAffectCount() {
        return affectCount;
    }

    public void setAffectCount(Integer affectCount) {
        this.affectCount = affectCount;
    }

    public String getBadPosition() {
        return badPosition;
    }

    public void setBadPosition(String badPosition) {
        this.badPosition = badPosition;
    }

    public Integer getSpotCheckCount() {
        return spotCheckCount;
    }

    public void setSpotCheckCount(Integer spotCheckCount) {
        this.spotCheckCount = spotCheckCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
