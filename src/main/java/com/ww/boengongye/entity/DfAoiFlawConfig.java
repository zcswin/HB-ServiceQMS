package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2023-10-09
 */
public class DfAoiFlawConfig extends Model<DfAoiFlawConfig> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 检测类型
     */
    private String testType;

    /**
     * 工序
     */
    private String process;

    /**
     * 项目
     */
    private String project;

    /**
     * 大区域
     */
    private String bigArea;

    /**
     * 缺陷名称
     */
    private String flawName;

    private LocalDateTime createTime;

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
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DfAoiFlawConfig{" +
            "id=" + id +
            ", testType=" + testType +
            ", process=" + process +
            ", project=" + project +
            ", bigArea=" + bigArea +
            ", flawName=" + flawName +
            ", createTime=" + createTime +
        "}";
    }
}
