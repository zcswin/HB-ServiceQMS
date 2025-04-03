package com.ww.boengongye.entity;

import lombok.Data;

/**
 * 产出占比
 */
@Data
public class DfAoiOutputPoint {
    /**
     * 投入数量
     */
    private Integer inputNumber;

    /**
     * 产出数量
     */
    private Integer outputNumer;


    /**
     * 投入时间
     */
    private String inputTime;

    /**
     * 产出占比
     */
    private String passPoint;

    @Override
    public String toString() {
        return "DfAoiOutputPoint{" +
                "inputNumber=" + inputNumber +
                ", outputNumer=" + outputNumer +
                ", passPoint='" + passPoint + '\'' +
                '}';
    }
}
