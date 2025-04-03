package com.ww.boengongye.entity.exportNewExcel;

public class jcDataVo {
    private Integer targetCount;
    private Integer totalAuditCount;
    private Integer executedCount;
    private Integer notExecutedCount;
    private double executionRate;

    public Integer getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(Integer targetCount) {
        this.targetCount = targetCount;
    }

    public Integer getTotalAuditCount() {
        return totalAuditCount;
    }

    public void setTotalAuditCount(Integer totalAuditCount) {
        this.totalAuditCount = totalAuditCount;
    }


    public Integer getExecutedCount() {
        return executedCount;
    }

    public void setExecutedCount(Integer executedCount) {
        this.executedCount = executedCount;
    }

    public Integer getNotExecutedCount() {
        return notExecutedCount;
    }

    public void setNotExecutedCount(Integer notExecutedCount) {
        this.notExecutedCount = notExecutedCount;
    }

    public double getExecutionRate() {
        return executionRate;
    }

    public void setExecutionRate(double executionRate) {
        this.executionRate = executionRate;
    }
}
