package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 风险品信息表
 * </p>
 *
 * @author zhao
 * @since 2023-08-11
 */
@ApiModel("风险品信息表")
public class DfRfidRiskDetail extends Model<DfRfidRiskDetail> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty("风险单id")
    private Integer parentId;

    @ApiModelProperty("明码信息")
    private String barCode;
    @ApiModelProperty("工序")
    private String processName;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("载具信息")
    private String carrier;

    @ApiModelProperty("检测结果")
    private String result;
    @ApiModelProperty("位置信息")
    @TableField(exist = false)
    private String position;

    @ApiModelProperty("轴信息")
    @TableField(exist = false)
    private String address;
    //关联的结果
    @TableField(exist = false)
    @ApiModelProperty("关联的结果")
    private String result2;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getResult2() {
        return result2;
    }

    public void setResult2(String result2) {
        this.result2 = result2;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "DfRfidRiskDetail{" +
            "id=" + id +
            ", parentId=" + parentId +
            ", barCode=" + barCode +
            ", processName=" + processName +
            ", type=" + type +
            ", carrier=" + carrier +
            ", result=" + result +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
