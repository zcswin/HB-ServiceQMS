package com.ww.boengongye.entity;

public class FlowNodes {
    public String id;
    public String type;
    public String x;
    public String y;
    public String zIndex;
    public FlowNodesProperties properties;
    public FlowNodesText text;



    public FlowNodes() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getzIndex() {
        return zIndex;
    }

    public void setzIndex(String zIndex) {
        this.zIndex = zIndex;
    }

    public FlowNodesProperties getProperties() {
        return properties;
    }

    public void setProperties(FlowNodesProperties properties) {
        this.properties = properties;
    }

    public FlowNodesText getText() {
        return text;
    }

    public void setText(FlowNodesText text) {
        this.text = text;
    }
}
