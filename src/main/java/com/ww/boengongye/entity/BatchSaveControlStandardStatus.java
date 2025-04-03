package com.ww.boengongye.entity;

import java.util.List;

public class BatchSaveControlStandardStatus {
    public List<DfControlStandardStatus> datas;
    public Integer jobId;

    public BatchSaveControlStandardStatus() {
    }

    public List<DfControlStandardStatus> getDatas() {
        return datas;
    }

    public void setDatas(List<DfControlStandardStatus> datas) {
        this.datas = datas;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }
}
