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
 * @since 2023-06-06
 */
public class DfProblemCa extends Model<DfProblemCa> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 项目分类 尺寸、外观、稽查
     */
    private String projectType;

    /**
     * 分类
     */
    private String type;

    /**
     * 问题点
     */
    private String problem;

    /**
     * 问题点解决方法
     */
    private String ca;

    private LocalDateTime createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }
    public String getCa() {
        return ca;
    }

    public void setCa(String ca) {
        this.ca = ca;
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
        return "DfProblemCa{" +
            "id=" + id +
            ", projectType=" + projectType +
            ", type=" + type +
            ", problem=" + problem +
            ", ca=" + ca +
            ", createTime=" + createTime +
        "}";
    }
}
