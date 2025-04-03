package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 设备
 * </p>
 *
 * @author zhao
 * @since 2022-11-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("设备")
public class DfMachine extends Model<DfMachine> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    public String code;

    public String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    /**
     * 工厂编号
     */
    public String factoryCode;

    /**
     * 车间编号
     */
    public String workshopCode;

    /**
     * 工序
     */
    public String processCode;

    /**
     * 是否3d显示
     */
    public Integer showThree;

    /**
     * 负责人
     */
    public String personName;

    /**
     * ip
     */
    public String devIp;

    /**
     * 设备型号
     */
    public String unitType;

    /**
     * 负责人通讯号
     */
    public String personCode;

    @ApiModelProperty("楼层")
    private String floor;

    @ApiModelProperty("入厂时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp joinTime;

    @ApiModelProperty(" 机台寿命")
    private Integer machineLife;
    /**
     * 项目
     */
    public String project;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public DfMachine() {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getWorkshopCode() {
        return workshopCode;
    }

    public void setWorkshopCode(String workshopCode) {
        this.workshopCode = workshopCode;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public Integer getShowThree() {
        return showThree;
    }

    public void setShowThree(Integer showThree) {
        this.showThree = showThree;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getDevIp() {
        return devIp;
    }

    public void setDevIp(String devIp) {
        this.devIp = devIp;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
