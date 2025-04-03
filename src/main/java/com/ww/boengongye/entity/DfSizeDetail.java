package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 尺寸数据
 * </p>
 *
 * @author zhao
 * @since 2023-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("尺寸数据")
public class DfSizeDetail extends Model<DfSizeDetail> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 机台号
     */
    public String machineCode;

    /**
     * 状态 首件/过程检
     */
    public String status;

    /**
     * 备注
     */
    public String remarks;

    /**
     * 机台状态 正常/闲置/调机/隔离
     */
    public String machineStatus;

    /**
     * 测量结果 OK/NG
     */
    public String result;

    /**
     * 测量时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp  testTime;

    /**
     * 工厂
     */
    public String factory;

    /**
     * 项目
     */
    public String project;

    /**
     * 工序
     */
    public String process;

    /**
     * 线体
     */
    public String linebody;

    /**
     * 白/夜班
     */
    public String dayOrNight;

    /**
     * 测试人
     */
    public String testMan;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;
    @TableField(exist = false)
    public String factoryName;

    public String haveJob;


    @TableField(exist = false)
    public String badStatus;


    public String checkDevCode;

    public String checkType;

    public String curTime;

    public String groupCode;

    public String checkId;

    public String itemName;

    public String macType;

    public String sn;

    public String shiftName;

    public String tester;

    public String unitCode;

    public String workShopCode;

    public String infoResult;

    @ApiModelProperty("加工时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp workTime;

    @ApiModelProperty("RFID架子编号")
    private String fixtureId;

    @ApiModelProperty("机台寿命")
    private Integer machineLife;

    @ApiModelProperty("刀具1寿命")
    private Integer knifeFirstLife;

    @ApiModelProperty("刀具2寿命")
    private Integer knifeSecondLife;

    @ApiModelProperty("刀具3寿命")
    private Integer knifeThirdLife;

    @ApiModelProperty("刀具4寿命")
    private Integer knifeFourthLife;

    @ApiModelProperty("有无换刀 (0=无换刀；1=测量异常换刀后首件；2=刀寿到期换刀后首件)")
    private Integer changeKnifeStatus;

    @ApiModelProperty("有无调机(0=无调机；1=测量异常调机后首件)")
    private Integer debugStatus;

    @ApiModelProperty("有无换班(0=无换班；1=换班后首件)")
    private Integer changeClassStatus;

    @ApiModelProperty("有无换夹治具(0=无换夹治具；1=换夹治具后首件)")
    private Integer changeClampStatus;

    @ApiModelProperty("轴号(1-4)默认1")
    private String bearing;

    @ApiModelProperty("玻璃扫码类型(0=不发送；1=发送给RFID)")
    @TableField(exist = false)
    public String errCode;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public DfSizeDetail() {
    }

    public String getInfoResult() {
        return infoResult;
    }

    public void setInfoResult(String infoResult) {
        this.infoResult = infoResult;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHaveJob() {
        return haveJob;
    }

    public void setHaveJob(String haveJob) {
        this.haveJob = haveJob;
    }

    public String getBadStatus() {
        return badStatus;
    }

    public void setBadStatus(String badStatus) {
        this.badStatus = badStatus;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMachineStatus() {
        return machineStatus;
    }

    public void setMachineStatus(String machineStatus) {
        this.machineStatus = machineStatus;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Timestamp getTestTime() {
        return testTime;
    }

    public void setTestTime(Timestamp testTime) {
        this.testTime = testTime;
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

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getLinebody() {
        return linebody;
    }

    public void setLinebody(String linebody) {
        this.linebody = linebody;
    }

    public String getDayOrNight() {
        return dayOrNight;
    }

    public void setDayOrNight(String dayOrNight) {
        this.dayOrNight = dayOrNight;
    }

    public String getTestMan() {
        return testMan;
    }

    public void setTestMan(String testMan) {
        this.testMan = testMan;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getCheckDevCode() {
        return checkDevCode;
    }

    public void setCheckDevCode(String checkDevCode) {
        this.checkDevCode = checkDevCode;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getCurTime() {
        return curTime;
    }

    public void setCurTime(String curTime) {
        this.curTime = curTime;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getMacType() {
        return macType;
    }

    public void setMacType(String macType) {
        this.macType = macType;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getTester() {
        return tester;
    }

    public void setTester(String tester) {
        this.tester = tester;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getWorkShopCode() {
        return workShopCode;
    }

    public void setWorkShopCode(String workShopCode) {
        this.workShopCode = workShopCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
}
