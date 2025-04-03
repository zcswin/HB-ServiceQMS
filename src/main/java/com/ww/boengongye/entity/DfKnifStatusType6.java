package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 砂轮刀具类型6的信息保存
 * </p>
 *
 * @author zhao
 * @since 2023-10-27
 */
@Data
@ApiModel("砂轮刀具类型6的信息")
public class DfKnifStatusType6 extends Model<DfKnifStatusType6> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp createTime;

    /**
     * 机台编号
     */
    @ApiModelProperty("机台编号")
    private String macCode;

    /**
     * 刀具位置编号
     */
    @ApiModelProperty("刀具位置编号")
    private String nNumTool;

    /**
     * 刀具切削次数
     */
    @ApiModelProperty("刀具切削次数")
    private Integer toolCutNum;

    @ApiModelProperty("工序")
    private String process;

}
