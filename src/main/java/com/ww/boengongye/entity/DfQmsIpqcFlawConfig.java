package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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
 * @since 2023-03-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfQmsIpqcFlawConfig extends Model<DfQmsIpqcFlawConfig> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 检测类型
     */
    public String testType;

    /**
     * 工序
     */
    public String process;

    /**
     * 项目
     */
    public String project;

    /**
     * 大区域
     */
    public String bigArea;

    /**
     * 缺陷名称
     */
    public String flawName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;
    @TableField(exist = false)
    public String color;

    @TableField(exist = false)
    public List<DfQmsIpqcFlawConfig> saveList;
    @TableField(exist = false)
    public List<DfQmsIpqcFlawConfig> rmoveList;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public DfQmsIpqcFlawConfig() {
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

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getBigArea() {
        return bigArea;
    }

    public void setBigArea(String bigArea) {
        this.bigArea = bigArea;
    }

    public String getFlawName() {
        return flawName;
    }

    public void setFlawName(String flawName) {
        this.flawName = flawName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DfQmsIpqcFlawConfig that = (DfQmsIpqcFlawConfig) o;
        return Objects.equals(testType, that.testType) && Objects.equals(process, that.process) && Objects.equals(project, that.project) && Objects.equals(bigArea, that.bigArea) && Objects.equals(flawName, that.flawName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testType, process, project, bigArea, flawName);
    }
}
