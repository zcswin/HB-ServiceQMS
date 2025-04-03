package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 单位缺陷机会数
 * </p>
 *
 * @author guangyao
 * @since 2023-08-26
 */
public class DfAoiUnitDefect extends Model<DfAoiUnitDefect> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 区域
     */
    private String area;

    /**
     * 机会数
     */
    private Integer count;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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
        return "DfAoiUnitDefect{" +
            "id=" + id +
            ", area=" + area +
            ", count=" + count +
            ", projectId=" + projectId +
            ", createTime=" + createTime +
        "}";
    }
}
