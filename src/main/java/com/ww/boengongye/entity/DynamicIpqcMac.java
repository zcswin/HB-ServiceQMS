package com.ww.boengongye.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Value;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DynamicIpqcMac {
    /**
     * 当前抽检规则名称
     */
    public String ruleName;
    /**
     * 机台号
     */
    public String machineCode;
    /**
     * 抽检频率
     */
    public Double  frequency;
    /**
     * 当前规则抽检次数
     */
    public Integer nowCount;
    /**
     * 更新时间yyyy-MM-dd hh:mm:ss
     */
    public String updateTimeStr;

    /**
     * 更新时间
     */
    public Long updateTime;

    /**
     * 规则指定更新次数
     */

    public Integer specifiedCount;

    /**
     * 当前规则通知发送次数
     */
    public Integer senMesCount;

    /**
     * 抽检总数
     */
    public Integer totalCount;

    /**
     * 超限数
     */
    public Integer ngCount;

    /**
     * 连续同一侧数,规则2
     */
    public Integer zugammenCount;

    /**
     * 连续两个在2西格玛外,规则5
     */
    public Integer twoPointOverTwo;

    /**
     * 连续两个在2西格玛外,规则6
     */
    public Integer fourPointOverOne;

    /**
     * cpk连续大于1数量
     */
    public Integer cpkCount;

    /**
     * spc1,2,5,6 ok连续数量
     */
    public Integer spcOkCount;

    /**
     * 外观连续ok数
     */
    public Integer appearanceOkCount;

    /**
     * 尺寸连续ok数
     */
    public Integer sizeOkCount;

    /**
     * 通知推送时间
     */
    public String lastSendMesTime;

    /**
    *机台状态
     */
    public String status;

    /**
     *抽检状态
     */
    public String checkStatus;
    /**
     *操作状态
     */
    public String operationStatus;

    /**
     * 操作时间
     */
    public String lastOperationTime;

    /**
     *持续时间
     */
    public Double   durationTime;

    /**
     *下一次抽检时间
     */
    public String   nextCheckTime;

    /**
     *颜色
     */
    public String   color;

    /**
     *前端样式
     */
    public String  webStyle;



    /**
     *统计样式
     */
    public String  infoStatus;

    /**
     *统计样式时间
     */
    public String  infoStatusTime;



    @Override
    public String toString() {
        return "DynamicIpqcMac{" +
                "ruleName='" + ruleName + '\'' +
                ", machineCode='" + machineCode + '\'' +
                ", frequency=" + frequency +
                ", nowCount=" + nowCount +
                ", updateTimeStr='" + updateTimeStr + '\'' +
                ", updateTime=" + updateTime +
                ", specifiedCount=" + specifiedCount +
                ", senMesCount=" + senMesCount +
                ", totalCount=" + totalCount +
                ", ngCount=" + ngCount +
                ", zugammenCount=" + zugammenCount +
                ", twoPointOverTwo=" + twoPointOverTwo +
                ", fourPointOverOne=" + fourPointOverOne +
                ", cpkCount=" + cpkCount +
                ", spcOkCount=" + spcOkCount +
                ", appearanceOkCount=" + appearanceOkCount +
                ", sizeOkCount=" + sizeOkCount +
                ", lastSendMesTime='" + lastSendMesTime + '\'' +
                '}';
    }
}
