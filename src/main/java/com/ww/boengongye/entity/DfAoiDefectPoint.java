package com.ww.boengongye.entity;

import lombok.Data;

/**
 * 缺陷占比
 */
@Data
public class DfAoiDefectPoint {
    /**
     * 缺陷名称
     */
    private String defectName;

    /**
     * 缺陷数量
     */
    private Integer defectNumber;

    /**
     * 缺陷占比
     */
    private String defectPoint;

    @Override
    public String toString() {
        return "DfAoiDefectPoint{" +
                "defectName='" + defectName + '\'' +
                ", defectNumber=" + defectNumber +
                ", defectPoint='" + defectPoint + '\'' +
                '}';
    }
}
