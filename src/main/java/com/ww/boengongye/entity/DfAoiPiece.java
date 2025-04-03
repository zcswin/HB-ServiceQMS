package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * AOI玻璃单片信息表
 * </p>
 *
 * @author zhao
 * @since 2023-08-10
 */
@ApiModel("AOI玻璃单片信息表")
@Data
public class DfAoiPiece extends Model<DfAoiPiece> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("AOI玻璃单片信息id")
    private Integer id;

    /**
     * 样本ID
     */
    @ApiModelProperty("样本ID")
    private String pieceid;

    /**
     * 二维码信息
     */
    @ApiModelProperty("二维码信息")
    private String name;

    /**
     * 框架ID，如果没有，可与样本ID相同
     */
    @ApiModelProperty("框架ID，如果没有，可与样本ID相同")
    private String frameid;

    /**
     * 检测时间
     */
    @ApiModelProperty("检测时间")
    private String time;

    /**
     * 整片判定结果（0是OK，1是NG）
     */
    @ApiModelProperty("整片判定结果（0是OK，1是NG）")
    private String qualityid;

    /**
     * 设备IP
     */
    @ApiModelProperty("设备IP")
    private String ip;

    /**
     * 设备号
     */
    @ApiModelProperty("设备号")
    @TableField(exist = false)
    private String machineCode;

    /**
     * 时间戳
     */
    @ApiModelProperty("时间戳")
    private String timesec;

    /**
     * AOI参数组
     */
    @ApiModelProperty("AOI参数组")
    private String recipe;

    /**
     * QC复判结果
     */
    @ApiModelProperty("QC复判结果")
    @TableField("RE_result")
    private String reResult;

    /**
     * QC复判结果记录
     */
    @ApiModelProperty("QC复判结果记录")
    @TableField("RE_log")
    private String reLog;

    /**
     * QC复判时间
     */
    @ApiModelProperty("QC复判时间")
    @TableField("RE_time")
    private String reTime;


    @TableField(exist = false)
    private String rE_time;


    /**
     * 明码
     */
    @ApiModelProperty("明码")
    private String barCode;


    /**
     * QC工号
     */
    @ApiModelProperty("QC工号")
    private String username;

    /**
     * QC使用的对检机IP
     */
    @ApiModelProperty("QC使用的对检机IP")
    private String clientip;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp updateTime;

    @ApiModelProperty("修改人")
    private String updateName;
    /**
     *  颜色
     */
    @ApiModelProperty("颜色")
    private String color;

    /**
     *  项目
     */
    @ApiModelProperty("项目")
    private String project;

    /**
     *  机台
     */
    @ApiModelProperty("机台")
    private String machine;

    /**
     *  工厂
     */
    @ApiModelProperty("工厂")
    private String factory;

    /**
     *  线体
     */
    @ApiModelProperty("线体")
    private String lineBody;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPieceid() {
        return pieceid;
    }

    public void setPieceid(String pieceid) {
        this.pieceid = pieceid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrameid() {
        return frameid;
    }

    public void setFrameid(String frameid) {
        this.frameid = frameid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getQualityid() {
        return qualityid;
    }

    public void setQualityid(String qualityid) {
        this.qualityid = qualityid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getTimesec() {
        return timesec;
    }

    public void setTimesec(String timesec) {
        this.timesec = timesec;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getReResult() {
        return reResult;
    }

    public void setReResult(String reResult) {
        this.reResult = reResult;
    }

    public String getReLog() {
        return reLog;
    }

    public void setReLog(String reLog) {
        this.reLog = reLog;
    }

    public String getReTime() {
        return reTime;
    }

    public void setReTime(String reTime) {
        this.reTime = reTime;
    }

    public String getrE_time() {
        return rE_time;
    }

    public void setrE_time(String rE_time) {
        this.rE_time = rE_time;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClientip() {
        return clientip;
    }

    public void setClientip(String clientip) {
        this.clientip = clientip;
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

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getLineBody() {
        return lineBody;
    }

    public void setLineBody(String lineBody) {
        this.lineBody = lineBody;
    }

    @Override
    public String toString() {
        return "DfAoiPiece{" +
                "id=" + id +
                ", pieceid='" + pieceid + '\'' +
                ", name='" + name + '\'' +
                ", frameid='" + frameid + '\'' +
                ", time='" + time + '\'' +
                ", qualityid='" + qualityid + '\'' +
                ", ip='" + ip + '\'' +
                ", machineCode='" + machineCode + '\'' +
                ", timesec='" + timesec + '\'' +
                ", recipe='" + recipe + '\'' +
                ", reResult='" + reResult + '\'' +
                ", reLog='" + reLog + '\'' +
                ", reTime='" + reTime + '\'' +
                ", barCode='" + barCode + '\'' +
                ", username='" + username + '\'' +
                ", clientip='" + clientip + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", updateName='" + updateName + '\'' +
                ", color='" + color + '\'' +
                ", project='" + project + '\'' +
                ", machine='" + machine + '\'' +
                ", factory='" + factory + '\'' +
                ", lineBody='" + lineBody + '\'' +
                '}';
    }
}
