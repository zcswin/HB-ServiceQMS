package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * AOI员工考试等级
 * </p>
 *
 * @author guangyao
 * @since 2023-10-23
 */
@Data
@ApiModel("AOI员工考试等级")
public class DfAoiUserGrade extends Model<DfAoiUserGrade> {

    private static final long serialVersionUID = 1L;

    /**
     * 员工考试等级id
     */
    @ApiModelProperty("员工考试等级id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 员工工号
     */
    @ApiModelProperty("员工考试等级id")
    private String userCode;

    /**
     * 成绩
     */
    @ApiModelProperty("员工考试等级id")
    private Double grade;

    /**
     * 考核时间
     */
    @ApiModelProperty("员工考试等级id")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp testTime;

    /**
     * 创建时间
     */
    @ApiModelProperty("员工考试等级id")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @Override
    public String toString() {
        return "DfAoiUserGrade{" +
            "id=" + id +
            ", userCode=" + userCode +
            ", grade=" + grade +
            ", testTime=" + testTime +
            ", createTime=" + createTime +
        "}";
    }
}
