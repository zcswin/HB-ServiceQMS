package com.ww.boengongye.entity;

import lombok.Data;

import java.util.List;

/**
 * 单个用户漏检类
 */
@Data
public class DfAoiEscape {

    /**
     * 漏检员工
     */
    private String userName;

    /**
     * FQC员工被OQC检测总数
     */
    private Integer oqcNumber;

    /**
     * 漏检时间
     */
    private String escapeTime;

    /**
     * 员工漏检的top5总数
     */
    private Integer escapeTop5Number;

    /**
     * 不良top5占比集合
     */
    private List<DfAoiEscapePoint> dfAoiEscapePoints;

    /**
     * 漏检top5占比
     */
    private String escapeTop5Point;

    /**
     * 漏检总数
     */
    private Integer escapeNumber;

    /**
     * 总漏检占比
     */
    private String escapePoint;

    @Override
    public String toString() {
        return "DfAoiEscape{" +
                "userName='" + userName + '\'' +
                ", oqcNumber='" + oqcNumber + '\'' +
                ", escapeTime='" + escapeTime + '\'' +
                ", escapeTop5Number=" + escapeTop5Number +
                ", dfAoiEscapePoints=" + dfAoiEscapePoints +
                ", escapeTop5Point='" + escapeTop5Point + '\'' +
                ", escapeNumber=" + escapeNumber +
                ", escapePoint='" + escapePoint + '\'' +
                '}';
    }
}
