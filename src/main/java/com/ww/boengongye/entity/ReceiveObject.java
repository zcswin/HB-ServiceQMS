package com.ww.boengongye.entity;

import java.util.List;

/**
 * txt文件接收对象
 */
public class ReceiveObject {

    /**
     *  缺陷类集合
     */
    private List<DfAoiDefectClass> classes;

    /**
     *  缺陷数据集合
     */
    private List<Defect> defect;

    /**
     * 缺陷图像集合
     */
    private List<Defectimages> defectimages;

    /**
     * AOI玻璃单片对象
     */
    private DfAoiPiece pieces;

    public List<DfAoiDefectClass> getClasses() {
        return classes;
    }

    public void setClasses(List<DfAoiDefectClass> classes) {
        this.classes = classes;
    }

    public List<Defect> getDefect() {
        return defect;
    }

    public void setDefect(List<Defect> defect) {
        this.defect = defect;
    }

    public List<Defectimages> getDefectimages() {
        return defectimages;
    }

    public void setDefectimages(List<Defectimages> defectimages) {
        this.defectimages = defectimages;
    }

    public DfAoiPiece getPieces() {
        return pieces;
    }

    public void setPieces(DfAoiPiece pieces) {
        this.pieces = pieces;
    }

    @Override
    public String toString() {
        return "ReceiveObject{" +
                "classes=" + classes +
                ", defect=" + defect +
                ", defectimages=" + defectimages +
                ", pieces=" + pieces +
                '}';
    }
}
