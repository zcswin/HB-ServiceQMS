package com.ww.boengongye.entity;

import java.util.List;

public class WaigPcsData {
    List<DfQmsIpqcWaigTotal> total;
    List<DfQmsIpqcWaigDetail> detail;
    public Double percent;
    public WaigPcsData() {
    }

    public List<DfQmsIpqcWaigTotal> getTotal() {
        return total;
    }

    public void setTotal(List<DfQmsIpqcWaigTotal> total) {
        this.total = total;
    }

    public List<DfQmsIpqcWaigDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<DfQmsIpqcWaigDetail> detail) {
        this.detail = detail;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }
}
