package com.ww.boengongye.entity;

import java.util.List;

public class AGVPosition {
    public Double odom;
    public String payLoad;
    public String devicePosition;
    public Integer extraStateFirst;
    public Integer pauseFlag;
    public Integer totalRunningTime;
    public String deviceCode;
    public String battery;
    public String deviceName;
    public Integer speed;
    public Integer deviceStatus;
    public Integer extraStateSecond;
    public Integer oritation;
    public List<Integer> devicePostionRec;
    public String state;

    public AGVPosition() {
    }

    public Double getOdom() {
        return odom;
    }

    public void setOdom(Double odom) {
        this.odom = odom;
    }

    public String getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(String payLoad) {
        this.payLoad = payLoad;
    }

    public String getDevicePosition() {
        return devicePosition;
    }

    public void setDevicePosition(String devicePosition) {
        this.devicePosition = devicePosition;
    }

    public Integer getExtraStateFirst() {
        return extraStateFirst;
    }

    public void setExtraStateFirst(Integer extraStateFirst) {
        this.extraStateFirst = extraStateFirst;
    }

    public Integer getPauseFlag() {
        return pauseFlag;
    }

    public void setPauseFlag(Integer pauseFlag) {
        this.pauseFlag = pauseFlag;
    }

    public Integer getTotalRunningTime() {
        return totalRunningTime;
    }

    public void setTotalRunningTime(Integer totalRunningTime) {
        this.totalRunningTime = totalRunningTime;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(Integer deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public Integer getExtraStateSecond() {
        return extraStateSecond;
    }

    public void setExtraStateSecond(Integer extraStateSecond) {
        this.extraStateSecond = extraStateSecond;
    }

    public Integer getOritation() {
        return oritation;
    }

    public void setOritation(Integer oritation) {
        this.oritation = oritation;
    }

    public List<Integer> getDevicePostionRec() {
        return devicePostionRec;
    }

    public void setDevicePostionRec(List<Integer> devicePostionRec) {
        this.devicePostionRec = devicePostionRec;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
