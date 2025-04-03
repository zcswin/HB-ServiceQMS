package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 审批流程线数据
 * </p>
 *
 * @author zhao
 * @since 2022-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfLinks extends Model<DfLinks> {

    public static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 绑定的流程
     */
    public Integer parentId;

    /**
     * 流程id
     */
    public Integer appprovalProcessId;

    /**
     * 起点id
     */
    public String pageSourceId;

    /**
     * 锚点起点
     */
    public String pageSourceAnchor;

    /**
     * 终点id
     */
    public String pageTargetId;

    /**
     * 锚点终点
     */
    public String pageTargetAnchor;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Timestamp createTime;

    /**
     * 标题
     */
    public String title;

    /**
     * 字体颜色
     */
    public String fontColor;

    /**
     * 线颜色
     */
    public String lineColor;

    /**
     * 线类型
     */
    public String lineType;
    /**
     * 线ID
     */
    public String connectionId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public DfLinks() {
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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getAppprovalProcessId() {
        return appprovalProcessId;
    }

    public void setAppprovalProcessId(Integer appprovalProcessId) {
        this.appprovalProcessId = appprovalProcessId;
    }

    public String getPageSourceId() {
        return pageSourceId;
    }

    public void setPageSourceId(String pageSourceId) {
        this.pageSourceId = pageSourceId;
    }

    public String getPageSourceAnchor() {
        return pageSourceAnchor;
    }

    public void setPageSourceAnchor(String pageSourceAnchor) {
        this.pageSourceAnchor = pageSourceAnchor;
    }

    public String getPageTargetId() {
        return pageTargetId;
    }

    public void setPageTargetId(String pageTargetId) {
        this.pageTargetId = pageTargetId;
    }

    public String getPageTargetAnchor() {
        return pageTargetAnchor;
    }

    public void setPageTargetAnchor(String pageTargetAnchor) {
        this.pageTargetAnchor = pageTargetAnchor;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getLineColor() {
        return lineColor;
    }

    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }
}
