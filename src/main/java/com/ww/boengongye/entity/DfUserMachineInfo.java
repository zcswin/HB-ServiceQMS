package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 员工机台绑定
 * </p>
 *
 * @author zhao
 * @since 2023-10-19
 */
@Data
@AllArgsConstructor
@ApiModel("员工机台绑定")
public class DfUserMachineInfo extends Model<DfUserMachineInfo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    private LocalDateTime createTime;

    /**
     * 机台号
     */
    private String machineCode;

    /**
     * 用户编号
     */
    private String userName;

    /**
     * 用户名
     */
    private String userRealName;

    /**
     * 登记时间
     */
    private String loginTime;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
