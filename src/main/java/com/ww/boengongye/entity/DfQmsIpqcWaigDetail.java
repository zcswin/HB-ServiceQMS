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

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2022-09-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfQmsIpqcWaigDetail extends Model<DfQmsIpqcWaigDetail> {

    public static final long serialVersionUID = 1L;

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

    public Integer  FParentId;

    public Integer  isUniwafe;


    @TableField(exist = false)
    public Integer ngNum;



    @TableField(exist = false)
    public String color;

    @TableField(exist = false)
    public String fBarcode;

    @TableField(exist = false)
    public String fMac;

    @TableField(exist = false)
    public String fBigpro;
    @TableField(exist = false)
    public String fSeq;

    @TableField(exist = false)
    public Integer count;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfQmsIpqcWaigDetail() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getfSn() {
        return fSn;
    }

    public void setfSn(String fSn) {
        this.fSn = fSn;
    }

    public String getfBigArea() {
        return fBigArea;
    }

    public void setfBigArea(String fBigArea) {
        this.fBigArea = fBigArea;
    }

    public String getfSmArea() {
        return fSmArea;
    }

    public void setfSmArea(String fSmArea) {
        this.fSmArea = fSmArea;
    }

    public String getfDefect() {
        return fDefect;
    }

    public void setfDefect(String fDefect) {
        this.fDefect = fDefect;
    }

    public String getfSort() {
        return fSort;
    }

    public void setfSort(String fSort) {
        this.fSort = fSort;
    }

    public String getfLevel() {
        return fLevel;
    }

    public void setfLevel(String fLevel) {
        this.fLevel = fLevel;
    }

    public String getfStandard() {
        return fStandard;
    }

    public void setfStandard(String fStandard) {
        this.fStandard = fStandard;
    }

    public String getfZerostandard() {
        return fZerostandard;
    }

    public void setfZerostandard(String fZerostandard) {
        this.fZerostandard = fZerostandard;
    }

    public String getfResult() {
        return fResult;
    }

    public void setfResult(String fResult) {
        this.fResult = fResult;
    }

    public String getfX() {
        return fX;
    }

    public void setfX(String fX) {
        this.fX = fX;
    }

    public String getfY() {
        return fY;
    }

    public void setfY(String fY) {
        this.fY = fY;
    }

    public Timestamp getfTime() {
        return fTime;
    }

    public void setfTime(Timestamp fTime) {
        this.fTime = fTime;
    }

    public Integer getFParentId() {
        return FParentId;
    }

    public void setFParentId(Integer FParentId) {
        this.FParentId = FParentId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public String getfProductId() {
        return fProductId;
    }

    public void setfProductId(String fProductId) {
        this.fProductId = fProductId;
    }

    public Integer getIsUniwafe() {
        return isUniwafe;
    }

    public void setIsUniwafe(Integer isUniwafe) {
        this.isUniwafe = isUniwafe;
    }

    public String getfBigpro() {
        return fBigpro;
    }

    public void setfBigpro(String fBigpro) {
        this.fBigpro = fBigpro;
    }
}
