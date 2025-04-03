package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 稽查发起人员表
 * </p>
 *
 * @author zhao
 * @since 2023-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfFlowAuditor extends Model<DfFlowAuditor> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 发起人账号
     */
    private String manCode;

    /**
     * 发起人名称
     */
    private String manName;

    /**
     * 可发起问题类型，如稽查、外观、尺寸、ORT
     */
    private String type;

    /**
     * 可发起工序
     */
    private String process;

    /**
     * 可发起等级
     */
    private String level;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 等级三审批 Y:是 N:否
     */
    private String approval;
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

    public String getManCode() {
        return manCode;
    }

    public void setManCode(String manCode) {
        this.manCode = manCode;
    }

    public String getManName() {
        return manName;
    }

    public void setManName(String manName) {
        this.manName = manName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public DfFlowAuditor() {
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }
}
