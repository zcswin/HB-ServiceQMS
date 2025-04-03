package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 管控标准状态
 * </p>
 *
 * @author zhao
 * @since 2022-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfControlStandardStatus extends Model<DfControlStandardStatus> {

    public static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 工艺路线id
     */
    public Integer routingId;

    /**
     * 管控标准id
     */
    public Integer controlStandradId;

    /**
     * 状态
     */
    public String dataStatus;

    @ApiModelProperty("工序")
    public String process;
    /**
     * 日期
     */
    @TableField(exist = false)
    public String date;

    /**
     * 总条数
     */
    @TableField(exist = false)
    public Integer totalCount;

    /**
     * 一周统计
     */
    @TableField(exist = false)
    public Integer weekNumber;

    /**
     * process
     */
    @TableField(exist = false)
    public String processCode;

    @TableField(exist = false)
    public String project;
    @TableField(exist = false)
    public String factory;
    @TableField(exist = false)
    public String linebody;
    @TableField(exist = false)
    public String processDrl;
    @TableField(exist = false)
    public Integer okCount;
    @TableField(exist = false)
    public Integer ngCount;
    @TableField(exist = false)
    public double okRate;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public Integer getOkCount() {
        return okCount;
    }

    public void setOkCount(Integer okCount) {
        this.okCount = okCount;
    }

    public Integer getNgCount() {
        return ngCount;
    }

    public void setNgCount(Integer ngCount) {
        this.ngCount = ngCount;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp updateTime;

    /**
     * 点检id
     */
    public Integer auditId;


    /**
     * 用uuid绑定同批次
     */
    private String batchId;

    /**
     * 工厂id
     */
    private String factoryId;

    /**
     * 线体id
     */
    private String lineBodyId;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 工段id
     */
    private String workshopSectionId;

    /**
     * 工站id
     */
    private String workstationId;

    /**
     * 创建人
     */
    private String createName;
    /**
     * 任务id
     */
    public Integer jobId;
    @TableField(exist = false)
    public DfAuditDetail dfAuditDetail;

    @TableField(exist = false)
    public String routingName;
    @TableField(exist = false)
    public String factoryName;
    @TableField(exist = false)
    public String workStationName;
    @TableField(exist = false)
    public String workshopSectionName;
    @TableField(exist = false)
    public String lineBodyName;

    @TableField(exist = false)
    public String ipqcNumber;
    @TableField(exist = false)
    public String projectName;

    @TableField(exist = false)
    public String createUserId;
    @TableField(exist = false)
    public String auditDetailId;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public DfControlStandardStatus() {
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

    public Integer getControlStandradId() {
        return controlStandradId;
    }

    public void setControlStandradId(Integer controlStandradId) {
        this.controlStandradId = controlStandradId;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
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

    public Integer getAuditId() {
        return auditId;
    }

    public void setAuditId(Integer auditId) {
        this.auditId = auditId;
    }


    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public String getLineBodyId() {
        return lineBodyId;
    }

    public void setLineBodyId(String lineBodyId) {
        this.lineBodyId = lineBodyId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getWorkshopSectionId() {
        return workshopSectionId;
    }

    public void setWorkshopSectionId(String workshopSectionId) {
        this.workshopSectionId = workshopSectionId;
    }

    public String getWorkstationId() {
        return workstationId;
    }

    public void setWorkstationId(String workstationId) {
        this.workstationId = workstationId;
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

    public String getLineBodyName() {
        return lineBodyName;
    }

    public void setLineBodyName(String lineBodyName) {
        this.lineBodyName = lineBodyName;
    }

    public String getIpqcNumber() {
        return ipqcNumber;
    }

    public void setIpqcNumber(String ipqcNumber) {
        this.ipqcNumber = ipqcNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }
}
