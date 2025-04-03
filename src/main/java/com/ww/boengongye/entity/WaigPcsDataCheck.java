package com.ww.boengongye.entity;

import java.util.List;

public class WaigPcsDataCheck {
    List<DfQmsIpqcWaigTotalCheck> total;
    List<DfQmsIpqcWaigDetailCheck> detail;
    public Double percent;
    public WaigPcsDataCheck() {
    }

    public List<DfQmsIpqcWaigTotalCheck> getTotal() {
        return total;
    }

    public void setTotal(List<DfQmsIpqcWaigTotalCheck> total) {
        this.total = total;
    }

    public List<DfQmsIpqcWaigDetailCheck> getDetail() {
        return detail;
    }

    public void setDetail(List<DfQmsIpqcWaigDetailCheck> detail) {
        this.detail = detail;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }
}
