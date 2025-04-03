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
 * 外观管控频率
 * </p>
 *
 * @author zhao
 * @since 2023-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfWaigContFrequ extends Model<DfWaigContFrequ> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 工序
     */
    private String process;

    /**
     * 管控频率
     */
    private String controlFrequency;

    /**
     * 首件管控频率
     */
    private String faiFrequency;

    /**
     * 首件件数
     */
    private Integer faiNum;

    /**
     * 过程管控频率
     */
    private String cpkFrequency;

    /**
     * 过程件数
     */
    private Integer cpkNum;

    /**
     * 管控不良项目
     */
    private String ngItem;

    /**
     * 允收水准
     */
    private String acceptLevel;

    /**
     * 允收良率
     */
    private String acceptOkRate;

    /**
     * 允收良率OK动作
     */
    private String okTodo;

    /**
     * 备注
     */
    private String remarks;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
