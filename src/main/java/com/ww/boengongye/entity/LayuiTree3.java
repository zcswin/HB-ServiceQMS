package com.ww.boengongye.entity;

import java.util.List;

public class LayuiTree3 {
    public String title;
    public String id;
    public boolean spread;
    public List<LayuiTree3> children;
    public boolean onSelected;
    public String parentId;
    public String userName;
    public String userCode;
    public  boolean checked;
    public LayuiTree3() {
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
    public List<LayuiTree3> getChildren() {
        return children;
    }
    public void setChildren(List<LayuiTree3> children) {
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
