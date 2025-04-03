package com.ww.boengongye.entity;

import lombok.Data;

import java.util.List;
@Data
public class DynamicIpqcMacDataDetail {
    public String itemName;
    public Double lsl;
    public Double usl;
    public Double standard;
    public List<Double> values;
}
