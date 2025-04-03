package com.ww.boengongye.entity.DTO;

import lombok.Data;

/**
 * @autor 96901
 * @date 2024/12/27
 */
@Data
public class FilterCondition {

    private String[] triggerTime;
    private String process;
    private String machineNumber;
    private String problem_detail;
    private String maintenanceSuggestion;
    private String dataSource;


    // 相应的 getter 和 setter 方法
    public String[] getTriggerTime() {
        return triggerTime;
    }


    public void setTriggerTime(String[] triggerTime) {
        this.triggerTime = triggerTime;
    }


    public String getProcess() {
        return process;
    }


    public void setProcess(String process) {
        this.process = process;
    }


    public String getMachineNumber() {
        return machineNumber;
    }


    public void setMachineNumber(String machineNumber) {
        this.machineNumber = machineNumber;
    }


    public String getProblem_detail() {
        return problem_detail;
    }


    public void setProblem_detail(String problem_detail) {
        this.problem_detail = problem_detail;
    }


    public String getMaintenanceSuggestion() {
        return maintenanceSuggestion;
    }


    public void setMaintenanceSuggestion(String maintenanceSuggestion) {
        this.maintenanceSuggestion = maintenanceSuggestion;
    }


    public String getDataSource() {
        return dataSource;
    }


    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

}