package com.ww.boengongye.entity;

import java.util.List;

public class RFIDBadGlassReportData {
    List<RFIDBadGlassReport> codeList;
    public String procedureName;
    public String productCode;
    public String type;

    public Integer dataId;
    public RFIDBadGlassReportData() {
    }

    public List<RFIDBadGlassReport> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<RFIDBadGlassReport> codeList) {
        this.codeList = codeList;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    @Override
    public String toString() {
        return "RFIDBadGlassReportData{" +
                "codeList=" + codeList +
                ", procedureName='" + procedureName + '\'' +
                ", productCode='" + productCode + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
