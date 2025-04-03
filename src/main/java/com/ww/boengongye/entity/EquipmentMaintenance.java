package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName equipment_maintenance
 */
@TableName(value ="equipment_maintenance")
@Data
public class EquipmentMaintenance implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @TableField(value = "trigger_time")
    private String triggerTime;

    /**
     * 
     */
    @TableField(value = "process")
    private String process;

    /**
     * 
     */
    @TableField(value = "machine_number")
    private String machineNumber;

    /**
     * 
     */
    @TableField(value = "problem_detail")
    private String problemDetail;

    /**
     * 
     */
    @TableField(value = "maintenance_suggestion")
    private String maintenanceSuggestion;

    /**
     * 
     */
    @TableField(value = "data_source")
    private String dataSource;

    /**
     * 
     */
    @TableField(value = "wave_graph1")
    private byte[] waveGraph1;

    /**
     * 
     */
    @TableField(value = "wave_graph2")
    private byte[] waveGraph2;





    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}