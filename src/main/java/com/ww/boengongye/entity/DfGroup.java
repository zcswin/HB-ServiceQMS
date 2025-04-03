package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2023-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfGroup extends Model<DfGroup> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 工厂
     */
    private String factory;

    /**
     * 工序
     */
    private String process;

    /**
     * 线体
     */
    private String linebody;

    /**
     * 单月工作 白/夜班
     */
    private String singleMonthWork;

    /**
     * 双月工作 白/夜班
     */
    private String doubleMonthWork;

    /**
     * 机台号
     */
    private String machineCode;

    /**
     * 组别
     */
    private String fGroup;

    /**
     * 负责人
     */
    private String respon;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp updateTime;

    private Integer macMin1;

    private Integer macMax1;

    private Integer macMin2;

    private Integer macMax2;

    @TableField(exist = false)
    private String firstCode;


    @Override
    protected Serializable pkVal() {
        return this.id;
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

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getLinebody() {
        return linebody;
    }

    public void setLinebody(String linebody) {
        this.linebody = linebody;
    }

    public String getSingleMonthWork() {
        return singleMonthWork;
    }

    public void setSingleMonthWork(String singleMonthWork) {
        this.singleMonthWork = singleMonthWork;
    }

    public String getDoubleMonthWork() {
        return doubleMonthWork;
    }

    public void setDoubleMonthWork(String doubleMonthWork) {
        this.doubleMonthWork = doubleMonthWork;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getfGroup() {
        return fGroup;
    }

    public void setfGroup(String fGroup) {
        this.fGroup = fGroup;
    }

    public String getRespon() {
        return respon;
    }

    public void setRespon(String respon) {
        this.respon = respon;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getMacMin1() {
        return macMin1;
    }

    public void setMacMin1(Integer macMin1) {
        this.macMin1 = macMin1;
    }

    public Integer getMacMax1() {
        return macMax1;
    }

    public void setMacMax1(Integer macMax1) {
        this.macMax1 = macMax1;
    }

    public Integer getMacMin2() {
        return macMin2;
    }

    public void setMacMin2(Integer macMin2) {
        this.macMin2 = macMin2;
    }

    public Integer getMacMax2() {
        return macMax2;
    }

    public void setMacMax2(Integer macMax2) {
        this.macMax2 = macMax2;
    }

    public String getFirstCode() {
        return firstCode;
    }

    public void setFirstCode(String firstCode) {
        this.firstCode = firstCode;
    }

    public DfGroup() {
    }
}
