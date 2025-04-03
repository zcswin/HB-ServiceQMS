package com.ww.boengongye.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuData {
    public String name;
    public String title;
    public String icon;
    public String isUrl;
    public String jump;
    public List<MenuData> list;

    public MenuData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<MenuData> getList() {
        return list;
    }

    public void setList(List<MenuData> list) {
        this.list = list;
    }

    public String getIsUrl() {
        return isUrl;
    }

    public void setIsUrl(String isUrl) {
        this.isUrl = isUrl;
    }

    public String getJump() {
        return jump;
    }

    public void setJump(String jump) {
        this.jump = jump;
    }
}
