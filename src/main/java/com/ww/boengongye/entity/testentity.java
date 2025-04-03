package com.ww.boengongye.entity;

public class testentity {


        private String machinecode;
        private String runMode_cur;
        private int statusid_cur;
        private int statusid_pre;
        private int statusSstep;
        private int type_data;
        private String file_prog_main;
        private int num_prog_main;
        private long pub_time;

    public testentity() {
    }

    public String getMachinecode() {
        return machinecode;
    }

    public void setMachinecode(String machinecode) {
        this.machinecode = machinecode;
    }

    public String getRunMode_cur() {
        return runMode_cur;
    }

    public void setRunMode_cur(String runMode_cur) {
        this.runMode_cur = runMode_cur;
    }

    public int getStatusid_cur() {
        return statusid_cur;
    }

    public void setStatusid_cur(int statusid_cur) {
        this.statusid_cur = statusid_cur;
    }

    public int getStatusid_pre() {
        return statusid_pre;
    }

    public void setStatusid_pre(int statusid_pre) {
        this.statusid_pre = statusid_pre;
    }

    public int getStatusSstep() {
        return statusSstep;
    }

    public void setStatusSstep(int statusSstep) {
        this.statusSstep = statusSstep;
    }

    public int getType_data() {
        return type_data;
    }

    public void setType_data(int type_data) {
        this.type_data = type_data;
    }

    public String getFile_prog_main() {
        return file_prog_main;
    }

    public void setFile_prog_main(String file_prog_main) {
        this.file_prog_main = file_prog_main;
    }

    public int getNum_prog_main() {
        return num_prog_main;
    }

    public void setNum_prog_main(int num_prog_main) {
        this.num_prog_main = num_prog_main;
    }

    public long getPub_time() {
        return pub_time;
    }

    public void setPub_time(long pub_time) {
        this.pub_time = pub_time;
    }
}
