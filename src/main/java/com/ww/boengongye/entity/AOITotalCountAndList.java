package com.ww.boengongye.entity;


import java.util.List;

public class AOITotalCountAndList {
    public Integer totalCount;
    public Integer ngCount;
    public List<DfAoiDefect> list;

    public AOITotalCountAndList() {
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getNgCount() {
        return ngCount;
    }

    public void setNgCount(Integer ngCount) {
        this.ngCount = ngCount;
    }

    public List<DfAoiDefect> getList() {
        return list;
    }

    public void setList(List<DfAoiDefect> list) {
        this.list = list;
    }
}
