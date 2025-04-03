package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhao
 * @since 2025-02-18
 */
@Data
public class DfAuditDetailNode extends Model<DfAuditDetailNode> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 节点名称
     */
    private String processNode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp createTime;

    /**
     * 结果
     */
    private String result;



    @Override
    public String toString() {
        return "DfAuditDetailNode{" +
            "id=" + id +
            ", parentId=" + parentId +
            ", processNode=" + processNode +
            ", createTime=" + createTime +
            ", result=" + result +
        "}";
    }
}
