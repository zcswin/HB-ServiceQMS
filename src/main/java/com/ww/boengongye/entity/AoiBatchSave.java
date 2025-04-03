package com.ww.boengongye.entity;

import java.util.List;

public class AoiBatchSave {
    public DfAoiBatch batch;
    public List<DfAoiBatchDetail> details;

    public AoiBatchSave() {
    }

    public DfAoiBatch getBatch() {
        return batch;
    }

    public void setBatch(DfAoiBatch batch) {
        this.batch = batch;
    }

    public List<DfAoiBatchDetail> getDetails() {
        return details;
    }

    public void setDetails(List<DfAoiBatchDetail> details) {
        this.details = details;
    }
}
