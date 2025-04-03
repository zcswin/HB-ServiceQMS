package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 工位维护
 * </p>
 *
 * @author guangyao
 * @since 2023-08-04
 */
@Data
public class DfAoiSeatProtect extends Model<DfAoiSeatProtect> {

    private static final long serialVersionUID = 1L;

    /**
     * 工位维护id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 员工id
     */
    private String userId;

    /**
     * 员工工号
     */
    @TableField(exist = false)
    private String userCode;

    /**
     * 员工姓名
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 工厂id
     */
    private Integer factoryId;

    /**
     * 工厂名称
     */
    @TableField(exist = false)
    private String factoryName;

    /**
     * 线体id
     */
    private Integer lineBobyId;

    /**
     * 线体名称
     */
    @TableField(exist = false)
    private String lineBobyName;

    /**
     * 工位
     */
    private Integer seat;

    /**
     * 班别
     */
    @TableField("classCategory")
    private String classCategory;

    /**
     * 工序
     */
    @TableField(exist = false)
    private String userProcess;

    /**
     * 考试等级
     */
    @TableField(exist = false)
    private String userGrade;

    /**
     * 入职日期
     */
    @TableField(exist = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String userCreateTime;

    /**
     * 劳动关系
     */
    @TableField(exist = false)
    private String userLaborRelation;

    /**
     * 最新修改日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;

    /**
     * 修改人
     */
    private String updateName;

    @Override
    public String toString() {
        return "DfAoiSeatProtect{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", userCode='" + userCode + '\'' +
                ", userName='" + userName + '\'' +
                ", factoryId=" + factoryId +
                ", factoryName='" + factoryName + '\'' +
                ", lineBobyId=" + lineBobyId +
                ", lineBobyName='" + lineBobyName + '\'' +
                ", seat=" + seat +
                ", classCategory='" + classCategory + '\'' +
                ", userProcess='" + userProcess + '\'' +
                ", userGrade='" + userGrade + '\'' +
                ", userCreateTime='" + userCreateTime + '\'' +
                ", userLaborRelation='" + userLaborRelation + '\'' +
                ", updateTime=" + updateTime +
                ", updateName='" + updateName + '\'' +
                '}';
    }
}
