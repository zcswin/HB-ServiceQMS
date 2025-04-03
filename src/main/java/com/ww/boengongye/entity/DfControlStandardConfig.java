package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 管控标准配置
 * </p>
 *
 * @author zhao
 * @since 2022-10-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfControlStandardConfig extends Model<DfControlStandardConfig> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 工艺id
     */
    public Integer routingId;

    /**
     * 工厂
     */
    private String factory;

    /**
     * 项目
     */
    private String project;

    /**
     * 线体
     */
    private String linebody;

    /**
     * 工序
     */
    private String process;

    /**
     * 类型
     */
    public String dataType;

    /**
     * 分类
     */
    public String dataClassify;

    /**
     * 细项
     */
    public String detail;

    /**
     * 标准值
     */
    public String standardValue;

    /**
     * 抽样频率
     */
    private String frequency;

    /**
     * 设备
     */
    private String machine;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp updateTime;
    @ApiModelProperty("上传批次号")
    private String batchCode;

    @TableField(exist = false)
    public String routingName;
    @TableField(exist = false)
    public String factoryName;
    @TableField(exist = false)
    public String workStationName;
    @TableField(exist = false)
    public String workshopSectionName;
    @TableField(exist = false)
    public String dataStatus;
    @TableField(exist = false)
    public String statusId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public DfControlStandardConfig() {
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

    public Integer getRoutingId() {
        return routingId;
    }

    public void setRoutingId(Integer routingId) {
        this.routingId = routingId;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getLinebody() {
        return linebody;
    }

    public void setLinebody(String linebody) {
        this.linebody = linebody;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataClassify() {
        return dataClassify;
    }

    public void setDataClassify(String dataClassify) {
        this.dataClassify = dataClassify;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getStandardValue() {
        return standardValue;
    }

    public void setStandardValue(String standardValue) {
        this.standardValue = standardValue;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
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

    public String getRoutingName() {
        return routingName;
    }

    public void setRoutingName(String routingName) {
        this.routingName = routingName;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getWorkStationName() {
        return workStationName;
    }

    public void setWorkStationName(String workStationName) {
        this.workStationName = workStationName;
    }

    public String getWorkshopSectionName() {
        return workshopSectionName;
    }

    public void setWorkshopSectionName(String workshopSectionName) {
        this.workshopSectionName = workshopSectionName;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }
}
