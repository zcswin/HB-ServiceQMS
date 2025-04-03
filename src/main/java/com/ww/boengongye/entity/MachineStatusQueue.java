package com.ww.boengongye.entity;

import com.google.gson.annotations.SerializedName;

public class MachineStatusQueue {
    @SerializedName("MachineCode")
    public String machineCode;
     @SerializedName("RunMode_Cur")
    public String runModeCur;
     @SerializedName("StatusID_Cur")
    public Integer statusIdCur;
     @SerializedName("StatusID_Pre")
    public Integer statusIdPre;
     @SerializedName("StatusStep")
    public Integer statusStep;
     @SerializedName("Type_Data")
    public String typeData;
     @SerializedName("file_prog_main")
    public String fileProgMain;
    @SerializedName("num_prog_main")
    public String numProgMain;
       @SerializedName("pub_time")
    public String pubTime;

    public MachineStatusQueue() {
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getRunModeCur() {
        return runModeCur;
    }

    public void setRunModeCur(String runModeCur) {
        this.runModeCur = runModeCur;
    }

    public Integer getStatusIdCur() {
        return statusIdCur;
    }

    public void setStatusIdCur(Integer statusIdCur) {
        this.statusIdCur = statusIdCur;
    }

    public Integer getStatusIdPre() {
        return statusIdPre;
    }

    public void setStatusIdPre(Integer statusIdPre) {
        this.statusIdPre = statusIdPre;
    }

    public Integer getStatusStep() {
        return statusStep;
    }

    public void setStatusStep(Integer statusStep) {
        this.statusStep = statusStep;
    }

    public String getTypeData() {
        return typeData;
    }

    public void setTypeData(String typeData) {
        this.typeData = typeData;
    }

    public String getFileProgMain() {
        return fileProgMain;
    }

    public void setFileProgMain(String fileProgMain) {
        this.fileProgMain = fileProgMain;
    }

    public String getNumProgMain() {
        return numProgMain;
    }

    public void setNumProgMain(String numProgMain) {
        this.numProgMain = numProgMain;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }


}
