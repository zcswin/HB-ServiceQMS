package com.ww.boengongye.entity;

import java.util.List;

public class LayuiTree {
    public String title;
    public Integer id;
    public boolean spread;
    public List<LayuiTree> children;
    public boolean onSelected;
    public Integer parentId;
    public LayuiTree() {
        super();
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public List<LayuiTree> getChildren() {
        return children;
    }
    public void setChildren(List<LayuiTree> children) {
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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
