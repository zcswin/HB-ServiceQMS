package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2022-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfTeCompressiveStrength extends Model<DfTeCompressiveStrength> {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    private Integer no;

    private LocalDate createDate;

    @TableField("DOL")
    private Double dol;

    @TableField("DOC")
    private Double doc;

    @TableField("CS")
    private Double cs;

    @TableField("CSK")
    private Double csk;

    @TableField("CT")
    private Double ct;


    @Override
    protected Serializable pkVal() {
        return null;
    }

    public DfTeCompressiveStrength() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public Double getDol() {
        return dol;
    }

    public void setDol(Double dol) {
        this.dol = dol;
    }

    public Double getDoc() {
        return doc;
    }

    public void setDoc(Double doc) {
        this.doc = doc;
    }

    public Double getCs() {
        return cs;
    }

    public void setCs(Double cs) {
        this.cs = cs;
    }

    public Double getCsk() {
        return csk;
    }

    public void setCsk(Double csk) {
        this.csk = csk;
    }

    public Double getCt() {
        return ct;
    }

    public void setCt(Double ct) {
        this.ct = ct;
    }
}
