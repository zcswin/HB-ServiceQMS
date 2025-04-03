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
 * 稽核NG详细
 * </p>
 *
 * @author zhao
 * @since 2023-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfAuditDetail extends Model<DfAuditDetail> {

    private static final long serialVersionUID = 1L;
    //班次
    @TableField(exist = false)
    public String classes;
    //关闭率
    @TableField(exist = false)
    public Double exitRate;
    //及时回复率
    @TableField(exist = false)
    public Double responseRate;
    //未关闭数
    @TableField(exist = false)
    public Double unclosed;
    //超时数
    @TableField(exist = false)
    public Double timeOut;
    //问题总数
    @TableField(exist = false)
    public Integer questionNum;
    //及时回复数
    @TableField(exist = false)
    public Integer respondPromptly;
    //问题关闭数
    @TableField(exist = false)
    public Integer closeNum;

    @TableField(exist = false)
    public Integer overtimeTimes;

    @TableField(exist = false)
    public Integer totalCount;

    @TableField(exist = false)
    public double overtimeRatio;

    @TableField(exist = false)
    public String date;

    @TableField(exist = false)
    public String fFac;

    //楼层
    @TableField(exist = false)
    public String floor;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;



    @TableField(exist = false)
    private  Integer totalPoints;
    @TableField(exist = false)
    private  Integer timeoutPoints;
    @TableField(exist = false)
    private  Integer onTimePoints;
    @TableField(exist = false)
    private  double timeoutPercentage;//超时比例
    @TableField(exist = false)
    private  double onTimePercentage;//时效内比例


    @TableField(exist = false)
    private  Integer closedRecords;
    @TableField(exist = false)
    private  Integer openRecords;
    @TableField(exist = false)
    private  double closeRate;//关闭率

    @TableField(exist = false)
    private  Integer Level1;
    @TableField(exist = false)
    private  double Level1Percentage;//
    @TableField(exist = false)
    private  Integer Level2;
    @TableField(exist = false)
    private  double Level2Percentage;//
    @TableField(exist = false)
    private  Integer Level3;
    @TableField(exist = false)
    private  double Level3Percentage;//


    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 责任部门
     */
    private String department;

    /**
     * ipqc报告编号
     */
    private String ipqcNumber;

    /**
     * 问题名称
     */
    private String questionName;

    /**
     * 报告人
     */
    private String reportMan;

    /**
     * 报告时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp reportTime;

    /**
     * 管控标准
     */
    private String controlStandard;

    /**
     * 影响数量
     */
    private Double affectNum;

    /**
     * 影响机台
     */
    private String affectMac;

    /**
     * 判定等级
     */
    private String decisionLevel;

    /**
     * 发生时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp occurrenceTime;

    /**
     * 责任人
     */
    private String responsible;

    /**
     * 责任人工号
     */
    private String responsibleId;

    /**
     * 责任人2
     */
    private String responsible2;

    /**
     * 责任人工号2
     */
    private String responsibleId2;

    /**
     * 责任人3
     */
    private String responsible3;

    /**
     * 责任人工号3
     */
    private String responsibleId3;

    /**
     * 责任人4
     */
    private String responsible4;

    /**
     * 责任人工号4
     */
    private String responsibleId4;

    /**
     * 处理意见
     */
    private String handlingSug;

    /**
     * 补充备注
     */
    private String remark;

    /**
     * 父id
     */
    private Integer parentId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp updateTime;

    /**
     * 是否提供FA/FC
     */
    private String isFaca;

    /**
     * 现场实际
     */
    private String scenePractical;

    /**
     * 问题类型
     */
    private String questionType;

    /**
     * 影响类型
     */
    private String impactType;

    /**
     * 工厂id
     */
    private Integer factoryId;

    /**
     * 工序Id
     */
    private Integer procedureId;

    /**
     * 线体id
     */
    private Integer lineId;

    /**
     * 工段Id
     */
    private Integer workshopId;

    /**
     * 工站id
     */
    private Integer workstationId;

    /**
     * 项目id
     */
    private Integer projectId;

    /**
     * 型号id
     */
    private Integer modelId;

    /**
     * 问题明细
     */
    private String problemDetial;

    /**
     * 时效时间，问题发生后的4小时内
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp linesTime;

    /**
     * 被接收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp receivedTime;

    /**
     * 超时时长
     */
    private Integer timeoutDuration;

    /**
     * 是否关闭
     */
    private String close;

    /**
     * 关闭时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp closeTime;

    /**
     * 工序溯源id
     */
    private Integer rfid;

    /**
     * NG/OK
     */
    private String ngOrOk;

    /**
     * 创建人
     */
    private String createName;

    /**
     * 创建人id
     */
    private String createUserId;

    private Integer requestId;

    private String lcbh;

    private String departmentLiable;

    private String handingOption;

    private String fa;

    private String ca;

    private String eventlevel1;

    private String eventlevel2;

    private String eventlevel3;

    private String eventlevel4;

    private String kpi;

    private String oaUuid;

    private String mesUuid;

    private String isEnabled;

    private String jdclsj2;

    private String jdclsj3;

    private String jdclsj4;

    private String jdclsj5;

    private String sqr;

    private String newRequestname;

    private String bzfa;

    private String bzca;

    private String factory;

    private String line;

    private String project;

    private String workshop;

    private String workstation;

    private String process;

    private String ipqcUrl;

    private String repUserCode;

    private String bzfaz;

    private String bzcaz;

    private String controlStandardId;

    private String oaNum;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp endTime;

    private String dataType;
    private String macCode;
    /**
     * 刀具号
     */
    private String nNumTool;
    /**
     * 刀具刀柄编号
     */
    private String toolCode;
    @ApiModelProperty("玻璃码")
    private String sn;

    @ApiModelProperty("NG阶段1/2")
    private String ngPhase;

    @TableField(exist = false)
    public String flowLevelName;

    @TableField(exist = false)
    public String shift;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp   startTimeout;
    @TableField(exist = false)
    public String  overtimeStatus;
    @TableField(exist = false)
    public String nowLevelUser;
    @TableField(exist = false)
    public String nowLevelUserName;

    @TableField(exist = false)
    public String fLine;

    @TableField(exist = false)
    public String fMac;


    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp level1ReadTime;
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp level2ReadTime;
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp level3ReadTime;

    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp level1AffirmTime;
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp level2AffirmTime;
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp level3AffirmTime;

    @TableField(exist = false)
    public Integer readTimeMax;

    @TableField(exist = false)
    public Integer disposeTimeMax;

    @TableField(exist = false)
    public String status;
    @TableField(exist = false)
    public String machineCode;

    @TableField(exist = false)
    public String initiatorAffirmTime;
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp  faCaReadTime;

    //设备信息
    //工序
    @TableField(exist = false)
    public String processCode;
    //负责人
    @TableField(exist = false)
    public String personName;
    //IP地址
    @TableField(exist = false)
    public String devIp;
    //设备名称
    @TableField(exist = false)
    public String macName;
    //负责人通讯号
    @TableField(exist = false)
    public String personCode;

    //显示当前责任人
    @TableField(exist = false)
    public String showApprover;

    //导出显示的责任人
    @TableField(exist = false)
    public String decisionLevelStr;

    //导出显示的进行时长
    @TableField(exist = false)
    public String useTime;
    //导出显示的超时时长
    @TableField(exist = false)
    public String overTime;

    //颜色
    private String color;

    //最大重复次数
    private Integer numberOfRepetitions;

    @TableField(exist = false)
    @ApiModelProperty("稽核事项")
    private String detail;

    public String getToolCode() {
        return toolCode;
    }

    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }

    public String getnNumTool() {
        return nNumTool;
    }

    public void setnNumTool(String nNumTool) {
        this.nNumTool = nNumTool;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getNumberOfRepetitions() {
        return numberOfRepetitions;
    }

    public void setNumberOfRepetitions(Integer numberOfRepetitions) {
        this.numberOfRepetitions = numberOfRepetitions;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getIpqcNumber() {
        return ipqcNumber;
    }

    public void setIpqcNumber(String ipqcNumber) {
        this.ipqcNumber = ipqcNumber;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getReportMan() {
        return reportMan;
    }

    public void setReportMan(String reportMan) {
        this.reportMan = reportMan;
    }

    public Timestamp getReportTime() {
        return reportTime;
    }

    public void setReportTime(Timestamp reportTime) {
        this.reportTime = reportTime;
    }

    public String getControlStandard() {
        return controlStandard;
    }

    public void setControlStandard(String controlStandard) {
        this.controlStandard = controlStandard;
    }

    public Double getAffectNum() {
        return affectNum;
    }

    public void setAffectNum(Double affectNum) {
        this.affectNum = affectNum;
    }

    public String getAffectMac() {
        return affectMac;
    }

    public void setAffectMac(String affectMac) {
        this.affectMac = affectMac;
    }

    public String getDecisionLevel() {
        return decisionLevel;
    }

    public void setDecisionLevel(String decisionLevel) {
        this.decisionLevel = decisionLevel;
    }

    public Timestamp getOccurrenceTime() {
        return occurrenceTime;
    }

    public void setOccurrenceTime(Timestamp occurrenceTime) {
        this.occurrenceTime = occurrenceTime;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getResponsibleId() {
        return responsibleId;
    }

    public void setResponsibleId(String responsibleId) {
        this.responsibleId = responsibleId;
    }

    public String getResponsible2() {
        return responsible2;
    }

    public void setResponsible2(String responsible2) {
        this.responsible2 = responsible2;
    }

    public String getResponsibleId2() {
        return responsibleId2;
    }

    public void setResponsibleId2(String responsibleId2) {
        this.responsibleId2 = responsibleId2;
    }

    public String getResponsible3() {
        return responsible3;
    }

    public void setResponsible3(String responsible3) {
        this.responsible3 = responsible3;
    }

    public String getResponsibleId3() {
        return responsibleId3;
    }

    public void setResponsibleId3(String responsibleId3) {
        this.responsibleId3 = responsibleId3;
    }

    public String getResponsible4() {
        return responsible4;
    }

    public void setResponsible4(String responsible4) {
        this.responsible4 = responsible4;
    }

    public String getResponsibleId4() {
        return responsibleId4;
    }

    public void setResponsibleId4(String responsibleId4) {
        this.responsibleId4 = responsibleId4;
    }

    public String getHandlingSug() {
        return handlingSug;
    }

    public void setHandlingSug(String handlingSug) {
        this.handlingSug = handlingSug;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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

    public String getIsFaca() {
        return isFaca;
    }

    public void setIsFaca(String isFaca) {
        this.isFaca = isFaca;
    }

    public String getScenePractical() {
        return scenePractical;
    }

    public void setScenePractical(String scenePractical) {
        this.scenePractical = scenePractical;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getImpactType() {
        return impactType;
    }

    public void setImpactType(String impactType) {
        this.impactType = impactType;
    }

    public Integer getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Integer factoryId) {
        this.factoryId = factoryId;
    }

    public Integer getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(Integer procedureId) {
        this.procedureId = procedureId;
    }

    public Integer getLineId() {
        return lineId;
    }

    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

    public Integer getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(Integer workshopId) {
        this.workshopId = workshopId;
    }

    public Integer getWorkstationId() {
        return workstationId;
    }

    public void setWorkstationId(Integer workstationId) {
        this.workstationId = workstationId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getProblemDetial() {
        return problemDetial;
    }

    public void setProblemDetial(String problemDetial) {
        this.problemDetial = problemDetial;
    }

    public Timestamp getLinesTime() {
        return linesTime;
    }

    public void setLinesTime(Timestamp linesTime) {
        this.linesTime = linesTime;
    }

    public Timestamp getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Timestamp receivedTime) {
        this.receivedTime = receivedTime;
    }

    public Integer getTimeoutDuration() {
        return timeoutDuration;
    }

    public void setTimeoutDuration(Integer timeoutDuration) {
        this.timeoutDuration = timeoutDuration;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public Timestamp getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Timestamp closeTime) {
        this.closeTime = closeTime;
    }

    public Integer getRfid() {
        return rfid;
    }

    public void setRfid(Integer rfid) {
        this.rfid = rfid;
    }

    public String getNgOrOk() {
        return ngOrOk;
    }

    public void setNgOrOk(String ngOrOk) {
        this.ngOrOk = ngOrOk;
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

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public String getLcbh() {
        return lcbh;
    }

    public void setLcbh(String lcbh) {
        this.lcbh = lcbh;
    }

    public String getDepartmentLiable() {
        return departmentLiable;
    }

    public void setDepartmentLiable(String departmentLiable) {
        this.departmentLiable = departmentLiable;
    }

    public String getHandingOption() {
        return handingOption;
    }

    public void setHandingOption(String handingOption) {
        this.handingOption = handingOption;
    }

    public String getFa() {
        return fa;
    }

    public void setFa(String fa) {
        this.fa = fa;
    }

    public String getCa() {
        return ca;
    }

    public void setCa(String ca) {
        this.ca = ca;
    }

    public String getEventlevel1() {
        return eventlevel1;
    }

    public void setEventlevel1(String eventlevel1) {
        this.eventlevel1 = eventlevel1;
    }

    public String getEventlevel2() {
        return eventlevel2;
    }

    public void setEventlevel2(String eventlevel2) {
        this.eventlevel2 = eventlevel2;
    }

    public String getEventlevel3() {
        return eventlevel3;
    }

    public void setEventlevel3(String eventlevel3) {
        this.eventlevel3 = eventlevel3;
    }

    public String getEventlevel4() {
        return eventlevel4;
    }

    public void setEventlevel4(String eventlevel4) {
        this.eventlevel4 = eventlevel4;
    }

    public String getKpi() {
        return kpi;
    }

    public void setKpi(String kpi) {
        this.kpi = kpi;
    }

    public String getOaUuid() {
        return oaUuid;
    }

    public void setOaUuid(String oaUuid) {
        this.oaUuid = oaUuid;
    }

    public String getMesUuid() {
        return mesUuid;
    }

    public void setMesUuid(String mesUuid) {
        this.mesUuid = mesUuid;
    }

    public String getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(String isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getJdclsj2() {
        return jdclsj2;
    }

    public void setJdclsj2(String jdclsj2) {
        this.jdclsj2 = jdclsj2;
    }

    public String getJdclsj3() {
        return jdclsj3;
    }

    public void setJdclsj3(String jdclsj3) {
        this.jdclsj3 = jdclsj3;
    }

    public String getJdclsj4() {
        return jdclsj4;
    }

    public void setJdclsj4(String jdclsj4) {
        this.jdclsj4 = jdclsj4;
    }

    public String getJdclsj5() {
        return jdclsj5;
    }

    public void setJdclsj5(String jdclsj5) {
        this.jdclsj5 = jdclsj5;
    }

    public String getSqr() {
        return sqr;
    }

    public void setSqr(String sqr) {
        this.sqr = sqr;
    }

    public String getNewRequestname() {
        return newRequestname;
    }

    public void setNewRequestname(String newRequestname) {
        this.newRequestname = newRequestname;
    }

    public String getBzfa() {
        return bzfa;
    }

    public void setBzfa(String bzfa) {
        this.bzfa = bzfa;
    }

    public String getBzca() {
        return bzca;
    }

    public void setBzca(String bzca) {
        this.bzca = bzca;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getWorkshop() {
        return workshop;
    }

    public void setWorkshop(String workshop) {
        this.workshop = workshop;
    }

    public String getWorkstation() {
        return workstation;
    }

    public void setWorkstation(String workstation) {
        this.workstation = workstation;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getIpqcUrl() {
        return ipqcUrl;
    }

    public void setIpqcUrl(String ipqcUrl) {
        this.ipqcUrl = ipqcUrl;
    }

    public String getRepUserCode() {
        return repUserCode;
    }

    public void setRepUserCode(String repUserCode) {
        this.repUserCode = repUserCode;
    }

    public String getBzfaz() {
        return bzfaz;
    }

    public void setBzfaz(String bzfaz) {
        this.bzfaz = bzfaz;
    }

    public String getBzcaz() {
        return bzcaz;
    }

    public void setBzcaz(String bzcaz) {
        this.bzcaz = bzcaz;
    }

    public String getControlStandardId() {
        return controlStandardId;
    }

    public void setControlStandardId(String controlStandardId) {
        this.controlStandardId = controlStandardId;
    }

    public String getOaNum() {
        return oaNum;
    }

    public void setOaNum(String oaNum) {
        this.oaNum = oaNum;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getInitiatorAffirmTime() {
        return initiatorAffirmTime;
    }

    public void setInitiatorAffirmTime(String initiatorAffirmTime) {
        this.initiatorAffirmTime = initiatorAffirmTime;
    }

    public String getMacCode() {
        return macCode;
    }

    public void setMacCode(String macCode) {
        this.macCode = macCode;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
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

    public String getMacName() {
        return macName;
    }

    public void setMacName(String macName) {
        this.macName = macName;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    public String getShowApprover() {
        return showApprover;
    }

    public void setShowApprover(String showApprover) {
        this.showApprover = showApprover;
    }

    public String getDecisionLevelStr() {
        return decisionLevelStr;
    }

    public void setDecisionLevelStr(String decisionLevelStr) {
        this.decisionLevelStr = decisionLevelStr;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getOverTime() {
        return overTime;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
