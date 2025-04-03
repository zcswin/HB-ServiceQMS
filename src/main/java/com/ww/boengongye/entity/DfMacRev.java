package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2022-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfMacRev extends Model<DfMacRev> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    @TableField("MachineCode")
    public String machinecode;

    @TableField("Tool_Index")
    public Integer tool_index;

    @TableField("Type_Data")
    public Integer type_data;
    @TableField("feedrate")
    public Double feedrate;
    @TableField("override_feedrate")
    public Integer override_feedrate;
    @TableField("override_rapid")
    public Integer override_rapid;
    @TableField("override_spindle")
    public Integer override_spindle;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("pub_time")
    public int pub_time;

    @TableField("speed_spindle")
    public Double speed_spindle;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("create_time")
    public Timestamp create_time;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public DfMacRev() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMachinecode() {
        return machinecode;
    }

    public void setMachinecode(String machinecode) {
        this.machinecode = machinecode;
    }

    public Integer getTool_index() {
        return tool_index;
    }

    public void setTool_index(Integer tool_index) {
        this.tool_index = tool_index;
    }

    public Integer getType_data() {
        return type_data;
    }

    public void setType_data(Integer type_data) {
        this.type_data = type_data;
    }

    public Double getFeedrate() {
        return feedrate;
    }

    public void setFeedrate(Double feedrate) {
        this.feedrate = feedrate;
    }

    public Integer getOverride_feedrate() {
        return override_feedrate;
    }

    public void setOverride_feedrate(Integer override_feedrate) {
        this.override_feedrate = override_feedrate;
    }

    public Integer getOverride_rapid() {
        return override_rapid;
    }

    public void setOverride_rapid(Integer override_rapid) {
        this.override_rapid = override_rapid;
    }

    public Integer getOverride_spindle() {
        return override_spindle;
    }

    public void setOverride_spindle(Integer override_spindle) {
        this.override_spindle = override_spindle;
    }

    public int getPub_time() {
        return pub_time;
    }

    public void setPub_time(int pub_time) {
        this.pub_time = pub_time;
    }

    public Double getSpeed_spindle() {
        return speed_spindle;
    }

    public void setSpeed_spindle(Double speed_spindle) {
        this.speed_spindle = speed_spindle;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }
}
