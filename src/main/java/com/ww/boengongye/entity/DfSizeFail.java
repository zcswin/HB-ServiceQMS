package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
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
 * @since 2023-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfSizeFail extends Model<DfSizeFail> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String fai;

    /**
     * 不良类型
     */
    private String badType;

    /**
     * 标准规格
     */
    private String standard;

    /**
     * 实际测量值
     */
    private Double practical;

    /**
     * 差值
     */
    private Double diffValue;

    /**
     * 不良情况
     */
    private String badCondition;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    private String createName;
    private Integer parentId;
    /**
     * 公差
     */
    private String tolerance;
    @ApiModelProperty("数据类型check/warm(风险品,绑定稽查单id)")
    private String type;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfSizeFail() {
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

    public String getFai() {
        return fai;
    }

    public void setFai(String fai) {
        this.fai = fai;
    }

    public String getBadType() {
        return badType;
    }

    public void setBadType(String badType) {
        this.badType = badType;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }


    public String getBadCondition() {
        return badCondition;
    }

    public void setBadCondition(String badCondition) {
        this.badCondition = badCondition;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Double getPractical() {
        return practical;
    }

    public void setPractical(Double practical) {
        this.practical = practical;
    }

    public Double getDiffValue() {
        return diffValue;
    }

    public void setDiffValue(Double diffValue) {
        this.diffValue = diffValue;
    }

    public String getTolerance() {
        return tolerance;
    }

    public void setTolerance(String tolerance) {
        this.tolerance = tolerance;
    }
}
