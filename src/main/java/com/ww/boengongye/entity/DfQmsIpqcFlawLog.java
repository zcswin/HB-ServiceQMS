package com.ww.boengongye.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

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
@Data
public class DfQmsIpqcFlawLog extends Model<DfQmsIpqcFlawLog> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 玻璃码
     */
    private String barCode;

    /**
     * 工序
     */
    private String process;

    /**
     * 缺陷
     */
    private String flawName;

    /**
     * fa
     */
    private String fa;

    /**
     * ca
     */
    private String ca;

    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
