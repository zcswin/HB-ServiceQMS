package com.ww.boengongye.entity.exportNewExcel;

import java.util.List;
import java.util.Map;

public class DfExcelPortVo {
    private String fac ;
    private String fBigpro;
    private String createName;


    private List<jcDataVo> children;
    private Map<String, Integer> issuePointList;
    private List<AuditIssue> auditIssues;

    public Map<String, Integer> getIssuePointList() {
        return issuePointList;
    }

    public void setIssuePointList(Map<String, Integer> issuePointList) {
        this.issuePointList = issuePointList;
    }

    public List<AuditIssue> getAuditIssues() {
        return auditIssues;
    }

    public void setAuditIssues(List<AuditIssue> auditIssues) {
        this.auditIssues = auditIssues;
    }

    public List<jcDataVo> getChildren() {
        return children;
    }

    public void setChildren(List<jcDataVo> children) {
        this.children = children;
    }

    public String getFac() {
        return fac;
    }

    public void setFac(String fac) {
        this.fac = fac;
    }

    public String getfBigpro() {
        return fBigpro;
    }

    public void setfBigpro(String fBigpro) {
        this.fBigpro = fBigpro;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }
}
