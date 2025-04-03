package com.ww.boengongye.entity.scada;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class GrindingLife {

    @TableField("Type_Data")
    private Integer typeData;


    @TableField("MachineCode")
    private String machineCode;


    @TableField("nIndexTool")
    private Integer nIndexTool;


    @TableField("nStatusTool")
    private Integer nStatusTool;


    @TableField("DT_Chanage")
    private String dTChanage;


    @TableField("DT_Abandon")
    private String dTAbandon;

    @TableField("ToolCode")
    private String toolCode;

    @TableField("ToolSpecCode")
    private String toolSpecCode;

    @TableField("nTotal_UsagePro")
    private Integer nTotalUsagePro;

    @TableField("nTotal_UsageSec")
    private Integer nTotalUsageSec;

    @TableField("pub_time")
    private long pubTime;



}
