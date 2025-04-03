package com.ww.boengongye.entity.exportExcelUpdate;

import java.util.List;

public class DfQmsIpqcWaigTotalVo {

    private String data;

    private String fseq;

    private String fFac;

    private String fmac;

    private String fsort;

    private String fBigpro;

    private List<Banbie> bbList;
    private List<NotProject> notProjectList;


    private List<DfQmsIpqcWaigTotalBo> testBOs;

    public String getFsort() {
        return fsort;
    }

    public void setFsort(String fsort) {
        this.fsort = fsort;
    }

    public List<Banbie> getBbList() {
        return bbList;
    }

    public void setBbList(List<Banbie> bbList) {
        this.bbList = bbList;
    }

    public List<NotProject> getNotProjectList() {
        return notProjectList;
    }

    public void setNotProjectList(List<NotProject> notProjectList) {
        this.notProjectList = notProjectList;
    }

    public String getData() {
        return data;
    }

    public String getFmac() {
        return fmac;
    }

    public String getFseq() {
        return fseq;
    }

    public void setFseq(String fseq) {
        this.fseq = fseq;
    }

    public void setFmac(String fmac) {
        this.fmac = fmac;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getfFac() {
        return fFac;
    }

    public void setfFac(String fFac) {
        this.fFac = fFac;
    }

    public String getfBigpro() {
        return fBigpro;
    }

    public void setfBigpro(String fBigpro) {
        this.fBigpro = fBigpro;
    }

    public List<DfQmsIpqcWaigTotalBo> getTestBOs() {
        return testBOs;
    }

    public void setTestBOs(List<DfQmsIpqcWaigTotalBo> testBOs) {
        this.testBOs = testBOs;
    }
}
