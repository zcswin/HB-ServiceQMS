package com.ww.boengongye.entity.exportNewExcel;

public class AuditIssue {
    private Integer totalIssues;
    private Integer issuePoint;
    private double issueRatio;

    private Integer totalPoints;//问题点数
    private Integer onTimePoints;//时效内
    private double onTimePercentage;//时效内比例
    private Integer timeoutPoints;//超时处理
    private double timeoutPercentage;//超时比例

    private Integer targetCount;
    private Integer closeRecords;//
    private Integer openRecords;//
    private double closeRate;//



    private  Integer Level1;
    private  double Level1Percentage;//
    private  Integer Level2;
    private  double Level2Percentage;//
    private  Integer Level3;
    private  double Level3Percentage;//

    public Integer getLevel1() {
        return Level1;
    }

    public void setLevel1(Integer level1) {
        Level1 = level1;
    }

    public double getLevel1Percentage() {
        return Level1Percentage;
    }

    public void setLevel1Percentage(double level1Percentage) {
        Level1Percentage = level1Percentage;
    }

    public Integer getLevel2() {
        return Level2;
    }

    public void setLevel2(Integer level2) {
        Level2 = level2;
    }

    public double getLevel2Percentage() {
        return Level2Percentage;
    }

    public void setLevel2Percentage(double level2Percentage) {
        Level2Percentage = level2Percentage;
    }

    public Integer getLevel3() {
        return Level3;
    }

    public void setLevel3(Integer level3) {
        Level3 = level3;
    }

    public double getLevel3Percentage() {
        return Level3Percentage;
    }

    public void setLevel3Percentage(double level3Percentage) {
        Level3Percentage = level3Percentage;
    }

    public Integer getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(Integer targetCount) {
        this.targetCount = targetCount;
    }

    public Integer getCloseRecords() {
        return closeRecords;
    }

    public void setCloseRecords(Integer closeRecords) {
        this.closeRecords = closeRecords;
    }

    public Integer getOpenRecords() {
        return openRecords;
    }

    public void setOpenRecords(Integer openRecords) {
        this.openRecords = openRecords;
    }

    public double getCloseRate() {
        return closeRate;
    }

    public void setCloseRate(double closeRate) {
        this.closeRate = closeRate;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getOnTimePoints() {
        return onTimePoints;
    }

    public void setOnTimePoints(Integer onTimePoints) {
        this.onTimePoints = onTimePoints;
    }

    public double getOnTimePercentage() {
        return onTimePercentage;
    }

    public void setOnTimePercentage(double onTimePercentage) {
        this.onTimePercentage = onTimePercentage;
    }

    public Integer getTimeoutPoints() {
        return timeoutPoints;
    }

    public void setTimeoutPoints(Integer timeoutPoints) {
        this.timeoutPoints = timeoutPoints;
    }

    public double getTimeoutPercentage() {
        return timeoutPercentage;
    }

    public void setTimeoutPercentage(double timeoutPercentage) {
        this.timeoutPercentage = timeoutPercentage;
    }

    public Integer getIssuePoint() {
        return issuePoint;
    }

    public void setIssuePoint(Integer issuePoint) {
        this.issuePoint = issuePoint;
    }

    public Integer getTotalIssues() {
        return totalIssues;
    }

    public void setTotalIssues(Integer totalIssues) {
        this.totalIssues = totalIssues;
    }

    public double getIssueRatio() {
        return issueRatio;
    }

    public void setIssueRatio(double issueRatio) {
        this.issueRatio = issueRatio;
    }
}
