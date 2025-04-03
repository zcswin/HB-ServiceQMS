package com.ww.boengongye.entity;

import lombok.Data;

import java.util.List;
@Data
public class DynamicIpqcMacData {
    public String machineCode;
    /**
     * 当前样本均值计算总和
     */
    public Double avgTotal;
    /**
     * 当前样本均值计算片数
     */
    public Integer avgCount;

    /**
     * 西格玛值
     */
    private Double sigma;

    /**
     * 抽检总数
     */
    public Integer totalCount;

    /**
     * ng总数
     */
    public Integer ngCount;

    /**
     * 更新时间yyyy-MM-dd hh:mm:ss
     */
    public String updateTimeStr;

    /**
     * 更新时间
     */
    public long updateTime;

    public String itemName;
    public Double lsl;
    public Double usl;
    public Double standard;
    public List<Double> values;


    /**
     * 连续同一侧数,规则2
     */
    public Integer zugammenCount;

    /**
     * 连续两个在2西格玛外,规则5
     */
    public Integer twoPointOverTwo;

    /**
     * 连续四个在1西格玛外,规则6
     */
    public Integer fourPointOverOne;


//    public List<DynamicIpqcMacDataDetail> details;

    /**
     * 上一次的测试值
     */

    public Double lastValue;

    /**
     * 上一次的总体标准差
     */
    public Double lastSigma;

    /**
     * 上一次的均值
     */
    public Double lastAvg;


    /**
     * 持续NG数
     */
    public Integer continuousNgCount;

    /**
     * 持续测量数
     */
    public Integer continuousCount;
}
