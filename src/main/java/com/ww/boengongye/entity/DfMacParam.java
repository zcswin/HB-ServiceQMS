package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.sql.Timestamp;
import java.time.LocalDate;

import java.time.LocalTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

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
 * @since 2022-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfMacParam extends Model<DfMacParam> {

    public static final long serialVersionUID = 1L;

    @TableField("workshopId")
    public Integer workshopId;

    public String id;

    @TableField("MachineID")
    public String MachineID;

    @TableField("MachineCode")
    public String MachineCode;

    @TableField("MachineFactory")
    public String MachineFactory;

    @TableField("StatusID")
    public String StatusID;

    @TableField("StatusMsg")
    public String StatusMsg;

    @TableField("Old_StatusID")
    public String oldStatusid;

    @TableField("Old_StatusMsg")
    public String oldStatusmsg;

    @TableField("Mode")
    public String Mode;

    @TableField("Feedrate")
    public String Feedrate;

    @TableField("SpLoad")
    public String SpLoad;

    @TableField("SvloadX")
    public String SvloadX;

    @TableField("SvloadY")
    public String SvloadY;

    @TableField("SvloadZ")
    public String SvloadZ;

    @TableField("SpindleSpeed")
    public String SpindleSpeed;

    @TableField("AbsoluteCoordinateX")
    public String AbsoluteCoordinateX;

    @TableField("AbsoluteCoordinateY")
    public String AbsoluteCoordinateY;

    @TableField("AbsoluteCoordinateZ")
    public String AbsoluteCoordinateZ;

    @TableField("MachineCoordinateX")
    public String MachineCoordinateX;

    @TableField("MachineCoordinateY")
    public String MachineCoordinateY;

    @TableField("MachineCoordinateZ")
    public String MachineCoordinateZ;

    @TableField("RelativeCoordinateX")
    public String RelativeCoordinateX;

    @TableField("RelativeCoordinateY")
    public String RelativeCoordinateY;

    @TableField("RelativeCoordinateZ")
    public String RelativeCoordinateZ;

    @TableField("NumberProgram_Main")
    public String numberprogramMain;

    @TableField("NumberProgram_Running")
    public String numberprogramRunning;

    @TableField("FileProgram_Main")
    public String fileprogramMain;

    @TableField("FileProgram_Running")
    public String fileprogramRunning;

    @TableField("ProductNum")
    public String ProductNum;

    @TableField("CuttingTime")
    public String CuttingTime;

    @TableField("PowerOnTime")
    public String PowerOnTime;

    @TableField("OperatingTime")
    public String OperatingTime;

    @TableField("OperatingTimePreProduct")
    public String OperatingTimePreProduct;

    @TableField("LubeOverLoad")
    public String LubeOverLoad;

    @TableField("CoolantOverLoad")
    public String CoolantOverLoad;

    @TableField("SpindleCoolantOverLoad")
    public String SpindleCoolantOverLoad;

    @TableField("EmergencyStop")
    public String EmergencyStop;

    @TableField("IndicatorLight")
    public String IndicatorLight;

    @TableField("WorkLight")
    public String WorkLight;

    @TableField("Lube")
    public String Lube;

    @TableField("Coolant")
    public String Coolant;

    @TableField("HolderOne")
    public String HolderOne;

    @TableField("HolderTwo")
    public String HolderTwo;

    @TableField("DoorLock")
    public String DoorLock;

    @TableField("VacuumOutput")
    public String VacuumOutput;

    @TableField("VacuumPluseOutput")
    public String VacuumPluseOutput;

    @TableField("FourthAxisLock")
    public String FourthAxisLock;

    @TableField("Renishaw")
    public String Renishaw;

    @TableField("JV")
    public String jv;

    @TableField("ROV")
    public String rov;

    @TableField("SPOV")
    public String spov;

    @TableField("CurToolNum")
    public String CurToolNum;

    @TableField("NUMOFTOOL")
    public String numoftool;

    public String quality;

    @TableField("CreateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp CreateTime;

    @TableField("CreateDate")
    public LocalDate CreateDate;

    @TableField("CreateHour")
    public LocalTime CreateHour;


    @Override
    protected Serializable pkVal() {
        return null;
    }

    public DfMacParam() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(Integer workshopId) {
        this.workshopId = workshopId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMachineID() {
        return MachineID;
    }

    public void setMachineID(String machineID) {
        MachineID = machineID;
    }

    public String getMachineCode() {
        return MachineCode;
    }

    public void setMachineCode(String machineCode) {
        MachineCode = machineCode;
    }

    public String getMachineFactory() {
        return MachineFactory;
    }

    public void setMachineFactory(String machineFactory) {
        MachineFactory = machineFactory;
    }

    public String getStatusID() {
        return StatusID;
    }

    public void setStatusID(String statusID) {
        StatusID = statusID;
    }

    public String getStatusMsg() {
        return StatusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        StatusMsg = statusMsg;
    }

    public String getOldStatusid() {
        return oldStatusid;
    }

    public void setOldStatusid(String oldStatusid) {
        this.oldStatusid = oldStatusid;
    }

    public String getOldStatusmsg() {
        return oldStatusmsg;
    }

    public void setOldStatusmsg(String oldStatusmsg) {
        this.oldStatusmsg = oldStatusmsg;
    }

    public String getMode() {
        return Mode;
    }

    public void setMode(String mode) {
        Mode = mode;
    }

    public String getFeedrate() {
        return Feedrate;
    }

    public void setFeedrate(String feedrate) {
        Feedrate = feedrate;
    }

    public String getSpLoad() {
        return SpLoad;
    }

    public void setSpLoad(String spLoad) {
        SpLoad = spLoad;
    }

    public String getSvloadX() {
        return SvloadX;
    }

    public void setSvloadX(String svloadX) {
        SvloadX = svloadX;
    }

    public String getSvloadY() {
        return SvloadY;
    }

    public void setSvloadY(String svloadY) {
        SvloadY = svloadY;
    }

    public String getSvloadZ() {
        return SvloadZ;
    }

    public void setSvloadZ(String svloadZ) {
        SvloadZ = svloadZ;
    }

    public String getSpindleSpeed() {
        return SpindleSpeed;
    }

    public void setSpindleSpeed(String spindleSpeed) {
        SpindleSpeed = spindleSpeed;
    }

    public String getAbsoluteCoordinateX() {
        return AbsoluteCoordinateX;
    }

    public void setAbsoluteCoordinateX(String absoluteCoordinateX) {
        AbsoluteCoordinateX = absoluteCoordinateX;
    }

    public String getAbsoluteCoordinateY() {
        return AbsoluteCoordinateY;
    }

    public void setAbsoluteCoordinateY(String absoluteCoordinateY) {
        AbsoluteCoordinateY = absoluteCoordinateY;
    }

    public String getAbsoluteCoordinateZ() {
        return AbsoluteCoordinateZ;
    }

    public void setAbsoluteCoordinateZ(String absoluteCoordinateZ) {
        AbsoluteCoordinateZ = absoluteCoordinateZ;
    }

    public String getMachineCoordinateX() {
        return MachineCoordinateX;
    }

    public void setMachineCoordinateX(String machineCoordinateX) {
        MachineCoordinateX = machineCoordinateX;
    }

    public String getMachineCoordinateY() {
        return MachineCoordinateY;
    }

    public void setMachineCoordinateY(String machineCoordinateY) {
        MachineCoordinateY = machineCoordinateY;
    }

    public String getMachineCoordinateZ() {
        return MachineCoordinateZ;
    }

    public void setMachineCoordinateZ(String machineCoordinateZ) {
        MachineCoordinateZ = machineCoordinateZ;
    }

    public String getRelativeCoordinateX() {
        return RelativeCoordinateX;
    }

    public void setRelativeCoordinateX(String relativeCoordinateX) {
        RelativeCoordinateX = relativeCoordinateX;
    }

    public String getRelativeCoordinateY() {
        return RelativeCoordinateY;
    }

    public void setRelativeCoordinateY(String relativeCoordinateY) {
        RelativeCoordinateY = relativeCoordinateY;
    }

    public String getRelativeCoordinateZ() {
        return RelativeCoordinateZ;
    }

    public void setRelativeCoordinateZ(String relativeCoordinateZ) {
        RelativeCoordinateZ = relativeCoordinateZ;
    }

    public String getNumberprogramMain() {
        return numberprogramMain;
    }

    public void setNumberprogramMain(String numberprogramMain) {
        this.numberprogramMain = numberprogramMain;
    }

    public String getNumberprogramRunning() {
        return numberprogramRunning;
    }

    public void setNumberprogramRunning(String numberprogramRunning) {
        this.numberprogramRunning = numberprogramRunning;
    }

    public String getFileprogramMain() {
        return fileprogramMain;
    }

    public void setFileprogramMain(String fileprogramMain) {
        this.fileprogramMain = fileprogramMain;
    }

    public String getFileprogramRunning() {
        return fileprogramRunning;
    }

    public void setFileprogramRunning(String fileprogramRunning) {
        this.fileprogramRunning = fileprogramRunning;
    }

    public String getProductNum() {
        return ProductNum;
    }

    public void setProductNum(String productNum) {
        ProductNum = productNum;
    }

    public String getCuttingTime() {
        return CuttingTime;
    }

    public void setCuttingTime(String cuttingTime) {
        CuttingTime = cuttingTime;
    }

    public String getPowerOnTime() {
        return PowerOnTime;
    }

    public void setPowerOnTime(String powerOnTime) {
        PowerOnTime = powerOnTime;
    }

    public String getOperatingTime() {
        return OperatingTime;
    }

    public void setOperatingTime(String operatingTime) {
        OperatingTime = operatingTime;
    }

    public String getOperatingTimePreProduct() {
        return OperatingTimePreProduct;
    }

    public void setOperatingTimePreProduct(String operatingTimePreProduct) {
        OperatingTimePreProduct = operatingTimePreProduct;
    }

    public String getLubeOverLoad() {
        return LubeOverLoad;
    }

    public void setLubeOverLoad(String lubeOverLoad) {
        LubeOverLoad = lubeOverLoad;
    }

    public String getCoolantOverLoad() {
        return CoolantOverLoad;
    }

    public void setCoolantOverLoad(String coolantOverLoad) {
        CoolantOverLoad = coolantOverLoad;
    }

    public String getSpindleCoolantOverLoad() {
        return SpindleCoolantOverLoad;
    }

    public void setSpindleCoolantOverLoad(String spindleCoolantOverLoad) {
        SpindleCoolantOverLoad = spindleCoolantOverLoad;
    }

    public String getEmergencyStop() {
        return EmergencyStop;
    }

    public void setEmergencyStop(String emergencyStop) {
        EmergencyStop = emergencyStop;
    }

    public String getIndicatorLight() {
        return IndicatorLight;
    }

    public void setIndicatorLight(String indicatorLight) {
        IndicatorLight = indicatorLight;
    }

    public String getWorkLight() {
        return WorkLight;
    }

    public void setWorkLight(String workLight) {
        WorkLight = workLight;
    }

    public String getLube() {
        return Lube;
    }

    public void setLube(String lube) {
        Lube = lube;
    }

    public String getCoolant() {
        return Coolant;
    }

    public void setCoolant(String coolant) {
        Coolant = coolant;
    }

    public String getHolderOne() {
        return HolderOne;
    }

    public void setHolderOne(String holderOne) {
        HolderOne = holderOne;
    }

    public String getHolderTwo() {
        return HolderTwo;
    }

    public void setHolderTwo(String holderTwo) {
        HolderTwo = holderTwo;
    }

    public String getDoorLock() {
        return DoorLock;
    }

    public void setDoorLock(String doorLock) {
        DoorLock = doorLock;
    }

    public String getVacuumOutput() {
        return VacuumOutput;
    }

    public void setVacuumOutput(String vacuumOutput) {
        VacuumOutput = vacuumOutput;
    }

    public String getVacuumPluseOutput() {
        return VacuumPluseOutput;
    }

    public void setVacuumPluseOutput(String vacuumPluseOutput) {
        VacuumPluseOutput = vacuumPluseOutput;
    }

    public String getFourthAxisLock() {
        return FourthAxisLock;
    }

    public void setFourthAxisLock(String fourthAxisLock) {
        FourthAxisLock = fourthAxisLock;
    }

    public String getRenishaw() {
        return Renishaw;
    }

    public void setRenishaw(String renishaw) {
        Renishaw = renishaw;
    }

    public String getJv() {
        return jv;
    }

    public void setJv(String jv) {
        this.jv = jv;
    }

    public String getRov() {
        return rov;
    }

    public void setRov(String rov) {
        this.rov = rov;
    }

    public String getSpov() {
        return spov;
    }

    public void setSpov(String spov) {
        this.spov = spov;
    }

    public String getCurToolNum() {
        return CurToolNum;
    }

    public void setCurToolNum(String curToolNum) {
        CurToolNum = curToolNum;
    }

    public String getNumoftool() {
        return numoftool;
    }

    public void setNumoftool(String numoftool) {
        this.numoftool = numoftool;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public Timestamp getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Timestamp createTime) {
        CreateTime = createTime;
    }

    public LocalDate getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(LocalDate createDate) {
        CreateDate = createDate;
    }

    public LocalTime getCreateHour() {
        return CreateHour;
    }

    public void setCreateHour(LocalTime createHour) {
        CreateHour = createHour;
    }
}
