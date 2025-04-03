package com.ww.boengongye.entity;

import java.util.List;

public class FlowAllData {
    public List<FlowNodes> nodes;
    public List<FlowEdges> edges;
    public DfFlow flow;

    public FlowAllData() {
    }

    public List<FlowNodes> getNodes() {
        return nodes;
    }

    public void setNodes(List<FlowNodes> nodes) {
        this.nodes = nodes;
    }

    public List<FlowEdges> getEdges() {
        return edges;
    }

    public void setEdges(List<FlowEdges> edges) {
        this.edges = edges;
    }

    public DfFlow getFlow() {
        return flow;
    }

    public void setFlow(DfFlow flow) {
        this.flow = flow;
    }

    @Override
    public String toString() {
        return "FlowAllData{" +
                "nodes=" + nodes +
                ", edges=" + edges +
                ", flow=" + flow +
                '}';
    }
}
