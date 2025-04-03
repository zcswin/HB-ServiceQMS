package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2022-10-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfRouting extends Model<DfRouting> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 工艺路线编号
     */
    private String routingCode;

    /**
     * 工厂编号
     */
    private String factoryCode;

    /**
     * 项目编号
     */
    private String projectCode;

    /**
     * 工段编号
     */
    private String sectionCode;

    /**
     * 工站编号
     */
    private String stationCode;

    private String lineBodyCode;

    /**
     * 工艺路线名称
     */
    private String routingName;

    /**
     * 工艺路线明细
     */
    private Integer routingDetail;

    @TableField(exist = false)
    private List<String> process;

    /**
     * 1启用，0禁用
     */
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp updateTime;

    @TableField(exist = false)
    private String processName;

    @TableField(exist = false)
    private Integer order;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfRouting() {
    }

    public DfRouting(Integer id, String routingCode, String factoryCode, String projectCode, String sectionCode, String stationCode, String routingName, Integer routingDetail, List<String> process, Integer status, Timestamp createTime, Timestamp updateTime, String processName, Integer order) {
        this.id = id;
        this.routingCode = routingCode;
        this.factoryCode = factoryCode;
        this.projectCode = projectCode;
        this.sectionCode = sectionCode;
        this.stationCode = stationCode;
        this.routingName = routingName;
        this.routingDetail = routingDetail;
        this.process = process;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.processName = processName;
        this.order = order;
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

    public String getRoutingCode() {
        return routingCode;
    }

    public void setRoutingCode(String routingCode) {
        this.routingCode = routingCode;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getRoutingName() {
        return routingName;
    }

    public void setRoutingName(String routingName) {
        this.routingName = routingName;
    }

    public Integer getRoutingDetail() {
        return routingDetail;
    }

    public void setRoutingDetail(Integer routingDetail) {
        this.routingDetail = routingDetail;
    }

    public List<String> getProcess() {
        return process;
    }

    public void setProcess(List<String> process) {
        this.process = process;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
