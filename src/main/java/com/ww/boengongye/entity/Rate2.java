package com.ww.boengongye.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rate2 {
    private String process;
    private String itemName;
    private Double okRate;
    private Double itemNgRate;

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getOkRate() {
        return okRate;
    }

    public void setOkRate(Double okRate) {
        this.okRate = okRate;
    }

    public Double getItemNgRate() {
        return itemNgRate;
    }

    public void setItemNgRate(Double itemNgRate) {
        this.itemNgRate = itemNgRate;
    }
}
