package com.ww.boengongye.entity;

import java.util.List;

public class AOIUpload {

    public Integer DataType;
    public DfAoiPiece pieces;
    public List<DfAoiDefect> defect;
    public List<AOIClasses> classes;
    public List<AOIDefectImages> defectimages;

    public AOIUpload() {
    }

    public DfAoiPiece getPieces() {
        return pieces;
    }

    public void setPieces(DfAoiPiece pieces) {
        this.pieces = pieces;
    }

    public List<DfAoiDefect> getDefect() {
        return defect;
    }

    public void setDefect(List<DfAoiDefect> defect) {
        this.defect = defect;
    }

    public List<AOIClasses> getClasses() {
        return classes;
    }

    public void setClasses(List<AOIClasses> classes) {
        this.classes = classes;
    }

    public List<AOIDefectImages> getDefectimages() {
        return defectimages;
    }

    public void setDefectimages(List<AOIDefectImages> defectimages) {
        this.defectimages = defectimages;
    }

    public Integer getDataType() {
        return DataType;
    }

    public void setDataType(Integer dataType) {
        DataType = dataType;
    }
}
