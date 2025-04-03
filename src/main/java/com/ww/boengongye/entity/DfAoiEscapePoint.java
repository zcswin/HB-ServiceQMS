package com.ww.boengongye.entity;

import lombok.Data;

/**
 * 漏检占比
 */
@Data
public class DfAoiEscapePoint {

    /**
     * 漏检缺陷名
     */
    private String escapeName;

    /**
     * 员工投入玻璃被OQC检测数量
     */
    private Integer oqcNumber;

    /**
     * 漏检缺陷数量
     */
    private Integer escapeNumber;

    /**
     *漏检缺陷占比
     */
    private String escapePoint;


    @Override
    public String toString() {
        return "DfAoiEscapePoint{" +
                "escapeName='" + escapeName + '\'' +
                ", escapeNumber=" + escapeNumber +
                ", escapePoint='" + escapePoint + '\'' +
                '}';
    }
}
