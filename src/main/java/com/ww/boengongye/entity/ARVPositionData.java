package com.ww.boengongye.entity;

import com.google.gson.annotations.SerializedName;

public class ARVPositionData {
    @SerializedName("Type_Data")
    public Integer type_Data;

    @SerializedName("ActionType")
    public String  actionType;

    @SerializedName("CareCode")
    public String  careCode;

    @SerializedName("MachineCode")
    public String  machineCode;

    @SerializedName("MaterialRackIndex")
    public String  materialRackIndex;

    @SerializedName("OrderCode")
    public String  orderCode;


    @SerializedName("Result_Msg")
    public String  result_Msg;

    @SerializedName("nType_Event")
    public String  nType_Event;


    @SerializedName("nType_RCS")
    public String  nType_RCS;


    @SerializedName("pub_time")
    public long  pub_time;

    @SerializedName("CarCode")
    public String  carCode;

    @SerializedName("CarStatus")
    public String  carStatus;

    @SerializedName("CarMsa")
    public String  carMsa;

    @SerializedName("PubTime")
    public String  pubTime;

    @SerializedName("data")
    public ARVPositionDetail  data;

    @SerializedName("x_position")
    public String  x_position;

    @SerializedName("y_position")
    public String  y_position;

    @SerializedName("position_x")
    public String  position_x;

    @SerializedName("position_y")
    public String  position_y;

    public Double  x;
    public Double  y;

    public ARVPositionData() {
    }

    public Integer getType_Data() {
        return type_Data;
    }

    public void setType_Data(Integer type_Data) {
        this.type_Data = type_Data;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getCareCode() {
        return careCode;
    }

    public void setCareCode(String careCode) {
        this.careCode = careCode;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getMaterialRackIndex() {
        return materialRackIndex;
    }

    public void setMaterialRackIndex(String materialRackIndex) {
        this.materialRackIndex = materialRackIndex;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getResult_Msg() {
        return result_Msg;
    }

    public void setResult_Msg(String result_Msg) {
        this.result_Msg = result_Msg;
    }

    public String getnType_Event() {
        return nType_Event;
    }

    public void setnType_Event(String nType_Event) {
        this.nType_Event = nType_Event;
    }

    public String getnType_RCS() {
        return nType_RCS;
    }

    public void setnType_RCS(String nType_RCS) {
        this.nType_RCS = nType_RCS;
    }

    public long getPub_time() {
        return pub_time;
    }

    public void setPub_time(long pub_time) {
        this.pub_time = pub_time;
    }

    public String getCarCode() {
        return carCode;
    }

    public void setCarCode(String carCode) {
        this.carCode = carCode;
    }

    public String getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(String carStatus) {
        this.carStatus = carStatus;
    }

    public String getCarMsa() {
        return carMsa;
    }

    public void setCarMsa(String carMsa) {
        this.carMsa = carMsa;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public ARVPositionDetail getData() {
        return data;
    }

    public void setData(ARVPositionDetail data) {
        this.data = data;
    }

    public String getX_position() {
        return x_position;
    }

    public void setX_position(String x_position) {
        this.x_position = x_position;
    }

    public String getY_position() {
        return y_position;
    }

    public void setY_position(String y_position) {
        this.y_position = y_position;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public String getPosition_x() {
        return position_x;
    }

    public void setPosition_x(String position_x) {
        this.position_x = position_x;
    }

    public String getPosition_y() {
        return position_y;
    }

    public void setPosition_y(String position_y) {
        this.position_y = position_y;
    }
}
