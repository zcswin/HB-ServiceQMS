package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 *
 * </p>
 *
 * @author zhao
 * @since 2023-10-24
 */
@Data
@ApiModel("责任部门")
public class DfReliableDepartment extends Model<DfReliableDepartment> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("部门名称")
    private String name;


    private Timestamp createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
