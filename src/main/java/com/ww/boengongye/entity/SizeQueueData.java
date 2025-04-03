package com.ww.boengongye.entity;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;
import java.util.List;

public class SizeQueueData {
    @SerializedName("DataType")
    public Integer dataType;
    @SerializedName("CheckDevCode")
    public String checkDevCode;
     @SerializedName("CheckResult")
    public String checkResult;
     @SerializedName("CheckTime")
    public String checkTime;
     @SerializedName("CheckType")
    public String checkType;
     @SerializedName("CurrentTime")
    public String currentTime;
     @SerializedName("FactoryCode")
    public String factoryCode;
     @SerializedName("GroupCode")
    public String groupCode;
     @SerializedName("ID")
    public String id;
     @SerializedName("ItemName")
    public String itemName;
     @SerializedName("MacType")
    public String macType;
     @SerializedName("MachineCode")
    public String machineCode;
 @SerializedName("ProcessNO")
    public String processNO;
 @SerializedName("Remark")
    public String remark;
 @SerializedName("SN")
    public String sn;
 @SerializedName("ShiftName")
    public String shiftName;
 @SerializedName("Tester")
    public String tester;

    @SerializedName("UnitCode")
    public String unitCode;
    @SerializedName("WorkShopCode")
    public String workShopCode;
    @SerializedName("CheckItemInfos")
public List<DfSizeCheckItemInfos> checkItemInfos;
    //防呆时间
    public String foolProofingTime;

    //NG阶段
    public String ngPhase;
    @SerializedName("errCode")
    public String errCode;

    @ApiModelProperty("任务单id")
    public Integer auditId;

    public SizeQueueData() {
    }

    public String getCheckDevCode() {
        return checkDevCode;
    }

    public void setCheckDevCode(String checkDevCode) {
        this.checkDevCode = checkDevCode;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getProcessNO() {
        return processNO;
    }

    public void setProcessNO(String processNO) {
        this.processNO = processNO;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public List<DfSizeCheckItemInfos> getCheckItemInfos() {
        return checkItemInfos;
    }

    public void setCheckItemInfos(List<DfSizeCheckItemInfos> checkItemInfos) {
        this.checkItemInfos = checkItemInfos;
    }

    public String getFoolProofingTime() {
        return foolProofingTime;
    }

    public void setFoolProofingTime(String foolProofingTime) {
        this.foolProofingTime = foolProofingTime;
    }


    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getNgPhase() {
        return ngPhase;
    }

    public void setNgPhase(String ngPhase) {
        this.ngPhase = ngPhase;
    }

    public Integer getAuditId() {
        return auditId;
    }

    public void setAuditId(Integer auditId) {
        this.auditId = auditId;
    }

    @Override
    public String toString() {
        return "SizeQueueData{" +
                "checkDevCode='" + checkDevCode + '\'' +
                ", checkResult='" + checkResult + '\'' +
                ", checkTime='" + checkTime + '\'' +
                ", checkType='" + checkType + '\'' +
                ", currentTime='" + currentTime + '\'' +
                ", factoryCode='" + factoryCode + '\'' +
                ", groupCode='" + groupCode + '\'' +
                ", id='" + id + '\'' +
                ", itemName='" + itemName + '\'' +
                ", macType='" + macType + '\'' +
                ", machineCode='" + machineCode + '\'' +
                ", processNO='" + processNO + '\'' +
                ", remark='" + remark + '\'' +
                ", sn='" + sn + '\'' +
                ", shiftName='" + shiftName + '\'' +
                ", tester='" + tester + '\'' +
                ", unitCode='" + unitCode + '\'' +
                ", workShopCode='" + workShopCode + '\'' +
                ", checkItemInfos=" + checkItemInfos +
                '}';
    }
}
