package com.ww.boengongye.mapper;

import com.ww.boengongye.entity.DfBadItem;
import com.ww.boengongye.entity.DfProcessRelationBaditem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 工序_不良项配置 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-10-13
 */
public interface DfProcessRelationBaditemMapper extends BaseMapper<DfProcessRelationBaditem> {

    @Select("select bad.id, bad.name from df_process_relation_baditem rel " +
            "left join df_bad_item bad on rel.bad_item_id = bad.id " +
            "where rel.process_id = #{process_id}")
    List<DfBadItem> listSelectedBadItem(@Param("process_id")Integer processId);

    @Select("select id, name from df_bad_item where id not in ( " +
            "select bad.id from df_process_relation_baditem rel " +
            "left join df_bad_item bad on rel.bad_item_id = bad.id " +
            "where rel.process_id = #{process_id})")
    List<DfBadItem> listUnselectedBadItem(@Param("process_id")Integer processId);
}
