package com.ww.boengongye.entity;

import java.util.List;

/**
 * 缺陷占比和漏检占比统计
 */
public class DfAoiDefectAndEscape {
    /**
     * 投入数量
     */
    private Integer total;

    /**
     * 抛出不良数量
     */
    private Integer defectTotal;

    /**
     * FQC中一个人检测出不良top5占比
     */
    private String defectTop5Point;

    /**
     * FQC中一个人检测出不良top5占比集合
     */
    private List<DfAoiDefectPoint> dfAoiDefectPoints;

    /**
     * FQC中一个人漏检top5占比
     */
    private String escapeTop5Point;

    /**
     * FQC中一个人漏检top5占比集合
     */
    private List<DfAoiEscapePoint> dfAoiEscapePoints;

    /**
     * QOC检测出所有漏检集合
     */
    private List<DfAoiEscape> dfAoiEscapes;



    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getDefectTotal() {
        return defectTotal;
    }

    public void setDefectTotal(Integer defectTotal) {
        this.defectTotal = defectTotal;
    }


    public List<DfAoiDefectPoint> getDfAoiDefectPoints() {
        return dfAoiDefectPoints;
    }

    public void setDfAoiDefectPoints(List<DfAoiDefectPoint> dfAoiDefectPoints) {
        this.dfAoiDefectPoints = dfAoiDefectPoints;
    }

    public String getDefectTop5Point() {
        return defectTop5Point;
    }

    public void setDefectTop5Point(String defectTop5Point) {
        this.defectTop5Point = defectTop5Point;
    }

    public String getEscapeTop5Point() {
        return escapeTop5Point;
    }

    public void setEscapeTop5Point(String escapeTop5Point) {
        this.escapeTop5Point = escapeTop5Point;
    }

    public List<DfAoiEscapePoint> getDfAoiEscapePoints() {
        return dfAoiEscapePoints;
    }

    public void setDfAoiEscapePoints(List<DfAoiEscapePoint> dfAoiEscapePoints) {
        this.dfAoiEscapePoints = dfAoiEscapePoints;
    }

    public List<DfAoiEscape> getDfAoiEscapes() {
        return dfAoiEscapes;
    }

    public void setDfAoiEscapes(List<DfAoiEscape> dfAoiEscapes) {
        this.dfAoiEscapes = dfAoiEscapes;
    }

    @Override
    public String toString() {
        return "DfAoiDefectAndEscape{" +
                "total=" + total +
                ", defectTotal=" + defectTotal +
                ", defectTop5Point='" + defectTop5Point + '\'' +
                ", dfAoiDefectPoints=" + dfAoiDefectPoints +
                ", escapeTop5Point='" + escapeTop5Point + '\'' +
                ", dfAoiEscapePoints=" + dfAoiEscapePoints +
                ", dfAoiEscapes=" + dfAoiEscapes +
                '}';
    }
}
