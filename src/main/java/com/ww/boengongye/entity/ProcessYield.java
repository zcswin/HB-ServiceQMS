package com.ww.boengongye.entity;

import lombok.Data;

import java.util.List;

@Data
public class ProcessYield {
    private String project;
    public List<String> process;
    public String startTime;
    public String endTime;

    public ProcessYield() {
    }

    public List<String> getProcess() {
        return process;
    }

    public void setProcess(List<String> process) {
        this.process = process;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
