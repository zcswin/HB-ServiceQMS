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
 * 工序_不良项配置
 * </p>
 *
 * @author zhao
 * @since 2022-10-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DfProcessRelationBaditem extends Model<DfProcessRelationBaditem> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer processId;

    private Integer badItemId;

    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
