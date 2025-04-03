package com.ww.boengongye.entity;

import java.util.List;

public class LayuiTree2 {
    public String title;
    public String id;
    public boolean spread;
    public List<LayuiTree2> children;
    public boolean onSelected;
    public String parentId;
    public String departmentCode;
    public String managerId;
    public boolean  disabled;
    public LayuiTree2() {
        super();
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public List<LayuiTree2> getChildren() {
        return children;
    }
    public void setChildren(List<LayuiTree2> children) {
        this.children = children;
    }
    public boolean isSpread() {
        return spread;
    }
    public void setSpread(boolean spread) {
        this.spread = spread;
    }

    public boolean isOnSelected() {
        return onSelected;
    }

    public void setOnSelected(boolean onSelected) {
        this.onSelected = onSelected;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
