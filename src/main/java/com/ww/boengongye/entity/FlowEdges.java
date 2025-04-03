package com.ww.boengongye.entity;

import java.util.List;

public class FlowEdges {

    public String id;
    public String type;
    public String sourceNodeId;
    public String targetNodeId;
    public String zIndex;
    public FlowNodesProperties properties;
    public FlowPoint  startPoint;
    public FlowPoint  endPoint;
    public List<FlowPoint> pointsList;

    public FlowEdges() {
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

    public String getSourceNodeId() {
        return sourceNodeId;
    }

    public void setSourceNodeId(String sourceNodeId) {
        this.sourceNodeId = sourceNodeId;
    }

    public String getTargetNodeId() {
        return targetNodeId;
    }

    public void setTargetNodeId(String targetNodeId) {
        this.targetNodeId = targetNodeId;
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

    public FlowPoint getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(FlowPoint startPoint) {
        this.startPoint = startPoint;
    }

    public FlowPoint getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(FlowPoint endPoint) {
        this.endPoint = endPoint;
    }

    public List<FlowPoint> getPointsList() {
        return pointsList;
    }

    public void setPointsList(List<FlowPoint> pointsList) {
        this.pointsList = pointsList;
    }
}
