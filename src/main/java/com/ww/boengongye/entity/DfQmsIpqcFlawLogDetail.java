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
 * @since 2024-06-21
 */
public class DfQmsIpqcFlawLogDetail extends Model<DfQmsIpqcFlawLogDetail> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * df_qms_ipqc_flaw_log的id
     */
    private Integer parentId;

    /**
     * 工序名
     */
    private String process;

    /**
     * 生产时间
     */
    private LocalDateTime productTime;

    /**
     * 架子号
     */
    private String clampCode;

    /**
     * 机台号
     */
    private String machineCode;

    private LocalDateTime createTime;

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
    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
    public LocalDateTime getProductTime() {
        return productTime;
    }

    public void setProductTime(LocalDateTime productTime) {
        this.productTime = productTime;
    }
    public String getClampCode() {
        return clampCode;
    }

    public void setClampCode(String clampCode) {
        this.clampCode = clampCode;
    }
    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
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
        return "DfQmsIpqcFlawLogDetail{" +
            "id=" + id +
            ", parentId=" + parentId +
            ", process=" + process +
            ", productTime=" + productTime +
            ", clampCode=" + clampCode +
            ", machineCode=" + machineCode +
            ", createTime=" + createTime +
        "}";
    }
}
